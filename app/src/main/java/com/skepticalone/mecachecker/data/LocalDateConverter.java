package com.skepticalone.mecachecker.data;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.NonNull;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

final class LocalDateConverter {

    @TypeConverter
    public static long dateToMillis(@NonNull LocalDate date) {
        return date.toDateTimeAtStartOfDay(DateTimeZone.UTC).getMillis();
    }

    @TypeConverter
    @NonNull
    public static LocalDate millisToDate(long millis) {
        return new LocalDate(millis, DateTimeZone.UTC);
    }

}
