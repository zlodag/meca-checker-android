package com.skepticalone.armour.data.compliance;

import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.AppConstants;

import java.util.List;

final class RowDurationOverDay extends RowDurationOverPeriod {
    RowDurationOverDay(boolean isChecked, @NonNull Shift.Data shiftData, @NonNull List<RosteredShift> previousShifts) {
        super(isChecked, shiftData, shiftData.getEnd().minusDays(1), previousShifts);
    }

    @Override
    int getMaximumHoursOverPeriod() {
        return AppConstants.MAXIMUM_HOURS_OVER_DAY;
    }

}