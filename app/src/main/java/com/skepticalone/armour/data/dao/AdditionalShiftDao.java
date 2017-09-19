package com.skepticalone.armour.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.skepticalone.armour.data.db.AppDatabase;
import com.skepticalone.armour.data.db.Contract;
import com.skepticalone.armour.data.entity.AdditionalShiftEntity;
import com.skepticalone.armour.data.entity.ShiftData;
import com.skepticalone.armour.data.util.InstantConverter;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;

import java.util.List;

@Dao
public abstract class AdditionalShiftDao extends ItemDao<AdditionalShiftEntity> {

    @NonNull
    private static final String GET_LAST_SHIFT_END =
            "SELECT " + Contract.COLUMN_NAME_SHIFT_END + " FROM " +
            Contract.AdditionalShifts.TABLE_NAME +
            " ORDER BY " +
            Contract.COLUMN_NAME_SHIFT_END +
            " DESC LIMIT 1";
    @NonNull
    private final PayableDaoHelper payableDaoHelper;

    AdditionalShiftDao(@NonNull AppDatabase database) {
        super(database);
        payableDaoHelper = new PayableDaoHelper(this);
    }

    @NonNull
    public final PayableDaoHelper getPayableDaoHelper() {
        return payableDaoHelper;
    }

    public final void setTimesSync(long id, @NonNull Instant start, @NonNull Instant end) {
        SupportSQLiteStatement setTimesStatement = getDatabase().compileStatement("UPDATE " +
                Contract.AdditionalShifts.TABLE_NAME +
                " SET " +
                Contract.COLUMN_NAME_SHIFT_START +
                " = ?, " +
                Contract.COLUMN_NAME_SHIFT_END +
                " = ? WHERE " +
                BaseColumns._ID +
                " = ?");
        setTimesStatement.bindLong(1, start.getEpochSecond());
        setTimesStatement.bindLong(2, end.getEpochSecond());
        setTimesStatement.bindLong(3, id);
        updateInTransaction(setTimesStatement);
    }

    @NonNull
    @Override
    final String getTableName() {
        return Contract.AdditionalShifts.TABLE_NAME;
    }

    synchronized public final long insertSync(@NonNull Pair<LocalTime, LocalTime> times, @NonNull ZoneId zoneId, int paymentInCents) {
        SupportSQLiteStatement insertStatement = getDatabase().compileStatement("INSERT INTO " +
                Contract.AdditionalShifts.TABLE_NAME +
                " (" +
                Contract.COLUMN_NAME_PAYMENT +
                ", " +
                Contract.COLUMN_NAME_SHIFT_START +
                ", " +
                Contract.COLUMN_NAME_SHIFT_END +
                ") VALUES (?,?,?)");
        insertStatement.bindLong(1, paymentInCents);
        Cursor cursor = getDatabase().query(GET_LAST_SHIFT_END, null);
        @Nullable final Instant lastShiftEnd = cursor.moveToFirst() ? InstantConverter.epochSecondToInstant(cursor.getLong(0)) : null;
        cursor.close();
        ShiftData shiftData = ShiftData.withEarliestStart(times.first, times.second, lastShiftEnd, zoneId, false);
        insertStatement.bindLong(2, shiftData.getStart().getEpochSecond());
        insertStatement.bindLong(3, shiftData.getEnd().getEpochSecond());
        getDatabase().beginTransaction();
        try {
            long id = insertStatement.executeInsert();
            getDatabase().setTransactionSuccessful();
            return id;
        } finally {
            getDatabase().endTransaction();
        }
    }

    @Override
    @Query("SELECT * FROM " +
            Contract.AdditionalShifts.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    public abstract LiveData<AdditionalShiftEntity> getItem(long id);

    @Override
    @Query("SELECT * FROM " +
            Contract.AdditionalShifts.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    abstract AdditionalShiftEntity getItemInternalSync(long id);

    @Override
    @Query("SELECT * FROM " + Contract.AdditionalShifts.TABLE_NAME + " ORDER BY " + Contract.COLUMN_NAME_SHIFT_START)
    public abstract LiveData<List<AdditionalShiftEntity>> getItems();

}
