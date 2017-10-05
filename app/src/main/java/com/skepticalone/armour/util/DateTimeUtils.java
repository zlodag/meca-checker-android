package com.skepticalone.armour.util;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeFormatterBuilder;
import org.threeten.bp.format.FormatStyle;
import org.threeten.bp.format.TextStyle;
import org.threeten.bp.temporal.ChronoField;

import java.util.Locale;

public final class DateTimeUtils {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
    private static final DateTimeFormatter fullDateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    private static final DateTimeFormatter dayFormatter = new DateTimeFormatterBuilder().appendText(ChronoField.DAY_OF_WEEK, TextStyle.SHORT).toFormatter();
    private final static int MINUTES_PER_HOUR = 60;
//    private static final PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
//            .appendYears()
//            .appendSuffix(" year", " years")
//            .appendSeparator(", ")
//            .appendMonths()
//            .appendSuffix(" month", " months")
//            .appendSeparator(", ")
//            .appendWeeks()
//            .appendSuffix(" week", " weeks")
//            .appendSeparator(", ")
//            .appendDays()
//            .appendSuffix(" day", " days")
//            .appendSeparator(", ")
//            .appendHours()
//            .appendSuffix(" hour", " hours")
//            .appendSeparator(", ")
//            .appendMinutes()
//            .appendSuffix(" minute", " minutes")
//            .toFormatter();
//    private static final PeriodFormatter weeksAgoFormatter = new PeriodFormatterBuilder()
//            .appendWeeks()
//            .appendSuffix(" week ago", " weeks ago")
//            .toFormatter();


    private static String getQualifiedString(@NonNull String main, @NonNull String qualifier) {
        return main + " (" + qualifier + ")";
    }

    public static String getTimeString(@NonNull LocalTime time) {
        return timeFormatter.format(time);
    }

    public static String getEndTimeString(@NonNull LocalDateTime endDateTime, @NonNull LocalDate startDate) {
        String endTimeString = timeFormatter.format(endDateTime);
        if (!endDateTime.toLocalDate().isEqual(startDate)) {
            endTimeString = getQualifiedString(endTimeString, dayFormatter.format(endDateTime));
        }
        return endTimeString;
    }

    public static String getDateTimeString(@NonNull LocalDateTime dateTime) {
        return dateTimeFormatter.format(dateTime);
    }

    public static String getFullDateString(@NonNull LocalDate date) {
        return fullDateFormatter.format(date);
    }

    private static String getSpanString(@NonNull String start, @NonNull String end) {
        return start + " - " + end;
    }

    public static String getWeekendDateSpanString(@NonNull LocalDate saturday) {
        return getSpanString(dateFormatter.format(saturday), dateFormatter.format(saturday.plusDays(1)));
    }

    public static String getTimeSpanString(@NonNull LocalDateTime start, @NonNull LocalDateTime end) {
        return getSpanString(timeFormatter.format(start), getEndTimeString(end, start.toLocalDate()));
    }

    public static String getTimeSpanString(@NonNull LocalTime start, @NonNull LocalTime end) {
        return getSpanString(timeFormatter.format(start), timeFormatter.format(end));
    }

    public static String getWeeksAgo(@NonNull LocalDate lastWeekendWorked, @NonNull LocalDate currentWeekend) {
        return String.format(Locale.US, "Weeks ago: %d", (currentWeekend.toEpochDay() - lastWeekendWorked.toEpochDay()) / 7);
//        return weeksAgoFormatter.format(Period.between(lastWeekendWorked, currentWeekend).to());
    }
//
//    public static String getPeriodString(@NonNull Period period) {
//        return period.toString();
//    }

    public static String getDurationString(@NonNull Context context, @NonNull Duration duration) {
        long hours = duration.getSeconds() / 3600, minutes = duration.getSeconds() % 3600 / 60;
        if (hours > 0 && minutes > 0) {
            return context.getString(R.string.duration_in_hours_and_minutes,
                    context.getResources().getQuantityString(R.plurals.hours, (int) hours, hours),
                    context.getResources().getQuantityString(R.plurals.minutes, (int) minutes, minutes)
            );
        } else if (hours > 0) {
            return context.getResources().getQuantityString(R.plurals.hours, (int) hours, hours);
        } else {
            return context.getResources().getQuantityString(R.plurals.minutes, (int) minutes, minutes);
        }
    }

    public static int calculateHours(int totalMinutes) {
        return totalMinutes / MINUTES_PER_HOUR;
    }

    public static int calculateMinutes(int totalMinutes) {
        return totalMinutes % MINUTES_PER_HOUR;
    }

    public static int getTotalMinutes(int hour, int minute) {
        return hour * MINUTES_PER_HOUR + minute;
    }

    public static int getTotalMinutes(@NonNull LocalTime time) {
        return getTotalMinutes(time.getHour(), time.getMinute());
    }
}
