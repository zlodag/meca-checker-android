package com.skepticalone.mecachecker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


class ShiftDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 17;
    private static final String DATABASE_NAME = "shifts.db";

    ShiftDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ShiftContract.RosteredShifts.SQL_CREATE_TABLE);
        db.execSQL(ShiftContract.RosteredShifts.SQL_CREATE_INDEX_START);
        db.execSQL(ShiftContract.RosteredShifts.SQL_CREATE_TRIGGER_BEFORE_INSERT);
        db.execSQL(ShiftContract.RosteredShifts.SQL_CREATE_TRIGGER_BEFORE_UPDATE);
        db.execSQL(ShiftContract.AdditionalShifts.SQL_CREATE_TABLE);
        db.execSQL(ShiftContract.AdditionalShifts.SQL_CREATE_INDEX_START);
        db.execSQL(ShiftContract.AdditionalShifts.SQL_CREATE_TRIGGER_BEFORE_INSERT);
        db.execSQL(ShiftContract.AdditionalShifts.SQL_CREATE_TRIGGER_BEFORE_UPDATE);
        db.execSQL(ShiftContract.CrossCoverShifts.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ShiftContract.RosteredShifts.SQL_DROP_TABLE);
        db.execSQL(ShiftContract.AdditionalShifts.SQL_DROP_TABLE);
        db.execSQL(ShiftContract.CrossCoverShifts.SQL_DROP_TABLE);
        onCreate(db);
    }
}
