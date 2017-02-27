package com.skepticalone.mecachecker.components;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TimePicker;

import com.skepticalone.mecachecker.data.ShiftProvider;
import com.skepticalone.mecachecker.util.AppConstants;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private static final String SHIFT_ID = "SHIFT_ID";
    private static final String START = "START";
    private static final String END = "END";
    private static final String IS_START = "IS_START";
    private final Calendar calendar = Calendar.getInstance();
    private ShiftOverlapListener mListener;

    public static TimePickerFragment create(long shiftId, long start, long end, boolean isStart) {
        Bundle arguments = new Bundle();
        arguments.putLong(SHIFT_ID, shiftId);
        arguments.putLong(START, start);
        arguments.putLong(END, end);
        arguments.putBoolean(IS_START, isStart);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (ShiftOverlapListener) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        calendar.setTimeInMillis(getArguments().getLong(getArguments().getBoolean(IS_START) ? START : END));
        return new TimePickerDialog(
                getActivity(),
                this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
        );
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
        minutes = AppConstants.getSteppedMinutes(minutes);
        boolean isStart = getArguments().getBoolean(IS_START);
        long start = getArguments().getLong(START);
        if (isStart){
            calendar.setTimeInMillis(start);
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minutes);
            start = calendar.getTimeInMillis();
            calendar.setTimeInMillis(getArguments().getLong(END));
            hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            minutes = calendar.get(Calendar.MINUTE);
        }
        calendar.setTimeInMillis(start);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minutes);
        if (calendar.getTimeInMillis() <= start) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        if (getActivity().getContentResolver().update(ShiftProvider.shiftUri(getArguments().getLong(SHIFT_ID)), ShiftProvider.getContentValues(start, calendar.getTimeInMillis()), null, null) == 0) {
            mListener.onShiftOverlap();
        }
    }

}