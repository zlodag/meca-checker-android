package com.skepticalone.armour.data.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.skepticalone.armour.data.db.Contract;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;

@Entity(tableName = Contract.RosteredShifts.TABLE_NAME, indices = {@Index(value = {Contract.COLUMN_NAME_SHIFT_START}), @Index(value = {Contract.COLUMN_NAME_SHIFT_END})})
public final class RawRosteredShiftEntity extends RawShift {

    @Nullable
    @Embedded(prefix = Contract.RosteredShifts.LOGGED_PREFIX)
    private final ShiftData loggedShiftData;

    @SuppressWarnings("SameParameterValue")
    public RawRosteredShiftEntity(
            long id,
            @Nullable String comment,
            @NonNull ShiftData shiftData,
            @Nullable ShiftData loggedShiftData
    ) {
        super(id, comment, shiftData);
        this.loggedShiftData = loggedShiftData;
    }

    @NonNull
    public static RawRosteredShiftEntity from(@Nullable Instant lastShiftEnd, @NonNull Pair<LocalTime, LocalTime> times, @NonNull ZoneId zoneId, boolean skipWeekends) {
        return new RawRosteredShiftEntity(
                NO_ID,
                null,
                ShiftData.withEarliestStartAfterMinimumDurationBetweenShifts(times.first, times.second, lastShiftEnd, zoneId, skipWeekends),
                null
        );
    }

    @Nullable
    public ShiftData getLoggedShiftData() {
        return loggedShiftData;
    }

}
