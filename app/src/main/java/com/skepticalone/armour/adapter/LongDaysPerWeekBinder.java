package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.ComplianceDataLongDaysPerWeek;
import com.skepticalone.armour.util.AppConstants;

final class LongDaysPerWeekBinder extends ComplianceDataBinder<ComplianceDataLongDaysPerWeek> {

    LongDaysPerWeekBinder(@NonNull Callbacks callbacks, @NonNull ComplianceDataLongDaysPerWeek complianceDataLongDaysPerWeek) {
        super(callbacks, complianceDataLongDaysPerWeek);
    }

    @Override
    int getPrimaryIcon() {
        return R.drawable.ic_long_day_black_24dp;
    }

    @NonNull
    @Override
    String getFirstLine(@NonNull Context context) {
        return context.getString(R.string.long_days_per_week);
    }

    @NonNull
    @Override
    String getSecondLine(@NonNull Context context) {
        int longDays = getData().getIndex() + 1;
        return context.getResources().getQuantityString(R.plurals.long_days, longDays, longDays);
    }

    @NonNull
    @Override
    String getMessage(@NonNull Context context) {
        return context.getString(R.string.meca_maximum_long_days_over_week, AppConstants.MAXIMUM_LONG_DAYS_PER_WEEK, AppConstants.MAXIMUM_HOURS_IN_NORMAL_DAY);
    }

    @Override
    boolean areContentsTheSame(@NonNull ComplianceDataLongDaysPerWeek A, @NonNull ComplianceDataLongDaysPerWeek B) {
        return A.getIndex() == B.getIndex();
    }

}
