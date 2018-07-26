package com.example.wepopandroid1.myapplicationtest;

import android.app.Activity;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * Created by User on 1/9/2017.
 */
public class SmartPoster implements ParsedNdefRecord {
    private final TextRecord mTitleRecord;
    private final UriRecord mUriRecord;
    private final RecommendedAction mAction;
    private final String mType;

    public SmartPoster(UriRecord mUriRecord,TextRecord mTitleRecord,  RecommendedAction mAction, String mType) {
        this.mTitleRecord = Preconditions.checkNotNull(mTitleRecord);
        this.mUriRecord = mUriRecord;
        this.mAction =Preconditions.checkNotNull(mAction);
        this.mType = mType;
    }

    public UriRecord getUriRecord() {
        return mUriRecord;
    }

    public TextRecord getTitle() {
        return mTitleRecord;
    }

    public static SmartPoster parse(NdefRecord record) {
        Preconditions.checkArgument(record.getTnf() == NdefRecord.TNF_WELL_KNOWN);
        Preconditions.checkArgument(Arrays.equals(record.getType(), NdefRecord.RTD_SMART_POSTER));
        try {
            NdefMessage subRecords = new NdefMessage(record.getPayload());
            return parse(subRecords.getRecords());
        } catch (FormatException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static <T> T getFirstIfExists(Iterable<?> elements, Class<T> type) {
        Iterable<T> filtered = Iterables.filter(elements, type);
        T instance = null;
        if (!Iterables.isEmpty(filtered)) {
            instance = Iterables.get(filtered, 0);
        }
        return instance;
    }

    private static final byte[] ACTION_RECORD_TYPE = new byte[] {'a', 'c', 't'};

    private static RecommendedAction parseRecommendedAction(NdefRecord[] records) {
        NdefRecord record = getByType(ACTION_RECORD_TYPE, records);
        if (record == null) {
            return RecommendedAction.UNKNOWN;
        }
        byte action = record.getPayload()[0];
        if (RecommendedAction.LOOKUP.containsKey(action)) {
            return RecommendedAction.LOOKUP.get(action);
        }
        return RecommendedAction.UNKNOWN;
    }
    private static NdefRecord getByType(byte[] type, NdefRecord[] records) {
        for (NdefRecord record : records) {
            if (Arrays.equals(type, record.getType())) {
                return record;
            }
        }
        return null;
    }

    private static SmartPoster parse(NdefRecord[] records) {
        try {
            Iterable<ParsedNdefRecord> record = NdefMessageParser.getRecords(records);
            UriRecord uri = Iterables.getOnlyElement(Iterables.filter(record, UriRecord.class));
            TextRecord title = getFirstIfExists(record, TextRecord.class);
            RecommendedAction action = parseRecommendedAction(records);
            String type = parseType(records);
            return new SmartPoster(uri, title, action, type);
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static final byte[] TYPE_TYPE = new byte[] {'t'};

    private static String parseType(NdefRecord[] records) {
        NdefRecord type = getByType(TYPE_TYPE, records);
        if (type == null) {
            return null;
        }
        return new String(type.getPayload(), Charsets.UTF_8);
    }

    @Override
    public View getView(Activity activity, LayoutInflater inflater, ViewGroup parent, int offset) {
        return null;
    }

    public static boolean isPoster(NdefRecord record) {
        try {
            parse(record);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private enum RecommendedAction {
        UNKNOWN((byte) -1), DO_ACTION((byte) 0), SAVE_FOR_LATER((byte) 1), OPEN_FOR_EDITING(
                (byte) 2);

        private static final ImmutableMap<Byte, RecommendedAction> LOOKUP;
        static {
            ImmutableMap.Builder<Byte, RecommendedAction> builder = ImmutableMap.builder();
            for (RecommendedAction action : RecommendedAction.values()) {
                builder.put(action.getByte(), action);
            }
            LOOKUP = builder.build();
        }

        private final byte mAction;

        private RecommendedAction(byte val) {
            this.mAction = val;
        }

        private byte getByte() {
            return mAction;
        }
    }
}
