package com.skepticalone.mecachecker.data.viewModel;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.dao.CrossCoverCustomDao;
import com.skepticalone.mecachecker.data.db.AppDatabase;
import com.skepticalone.mecachecker.data.entity.CrossCoverEntity;

import org.joda.time.LocalDate;

public final class CrossCoverViewModel extends ItemViewModel<CrossCoverEntity> {

    @NonNull
    private final PayableViewModelHelper payableViewModelHelper;

    public CrossCoverViewModel(@NonNull Application application) {
        super(application);
        this.payableViewModelHelper = new PayableViewModelHelper(getDao().getPayableDaoHelper());
    }

    @NonNull
    public PayableViewModelHelper getPayableViewModelHelper() {
        return payableViewModelHelper;
    }

    @NonNull
    @Override
    CrossCoverCustomDao getDao() {
        return AppDatabase.getInstance(getApplication()).crossCoverCustomDao();
    }

    public void saveNewDate(@NonNull final CrossCoverEntity item, @NonNull final LocalDate date) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    getDao().setDateSync(item.getId(), date);
                } catch (SQLiteConstraintException e) {
                    postOverlappingShifts();
                }
            }
        });
    }

    public void addNewCrossCoverShift() {
        runAsync(new Runnable() {
            @Override
            public void run() {
                postSelectedId(getDao().insertSync(PreferenceManager.getDefaultSharedPreferences(getApplication()).getInt(getApplication().getString(R.string.key_default_cross_cover_payment), getApplication().getResources().getInteger(R.integer.default_cross_cover_payment))));
//
//                synchronized (CrossCoverViewModel.this) {
//                    postSelectedId(getDao().insertItemSync(new CrossCoverEntity(
//                            CrossCoverEntity.getNewDate(getDao().getLastCrossCoverDateSync()),
//                            PaymentData.fromPayment(PreferenceManager.getDefaultSharedPreferences(getApplication()).getInt(getApplication().getString(R.string.key_default_cross_cover_payment), getApplication().getResources().getInteger(R.integer.default_cross_cover_payment))),
//                            null
//                    )));
//                }
            }
        });
    }
}
