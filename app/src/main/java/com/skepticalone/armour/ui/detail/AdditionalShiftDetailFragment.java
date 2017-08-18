package com.skepticalone.armour.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.adapter.AdditionalShiftDetailAdapter;
import com.skepticalone.armour.data.entity.AdditionalShiftEntity;
import com.skepticalone.armour.data.viewModel.AdditionalShiftViewModel;
import com.skepticalone.armour.ui.dialog.AdditionalShiftCommentDialogFragment;
import com.skepticalone.armour.ui.dialog.AdditionalShiftDateDialogFragment;
import com.skepticalone.armour.ui.dialog.AdditionalShiftPaymentDialogFragment;
import com.skepticalone.armour.ui.dialog.AdditionalShiftTimeDialogFragment;
import com.skepticalone.armour.ui.dialog.CommentDialogFragment;
import com.skepticalone.armour.ui.dialog.PaymentDialogFragment;
import com.skepticalone.armour.util.ShiftUtil;

public final class AdditionalShiftDetailFragment
        extends PayableDetailFragment<AdditionalShiftEntity>
        implements AdditionalShiftDetailAdapter.Callbacks {

    private AdditionalShiftDetailAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        adapter = new AdditionalShiftDetailAdapter(this, ShiftUtil.Calculator.getInstance(context));
    }

    @NonNull
    @Override
    protected AdditionalShiftDetailAdapter getAdapter() {
        return adapter;
    }

    @NonNull
    @Override
    protected AdditionalShiftViewModel getViewModel() {
        return ViewModelProviders.of(getActivity()).get(AdditionalShiftViewModel.class);
    }

    @Override
    public void changeDate() {
        showDialogFragment(new AdditionalShiftDateDialogFragment());
    }

    @Override
    public void changeTime(boolean start) {
        showDialogFragment(AdditionalShiftTimeDialogFragment.newInstance(start));
    }

    @NonNull
    @Override
    PaymentDialogFragment<AdditionalShiftEntity> createPaymentDialogFragment() {
        return new AdditionalShiftPaymentDialogFragment();
    }

    @NonNull
    @Override
    CommentDialogFragment<AdditionalShiftEntity> createCommentDialogFragment() {
        return new AdditionalShiftCommentDialogFragment();
    }

}