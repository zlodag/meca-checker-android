package com.skepticalone.armour.data.viewModel;

import android.support.annotation.NonNull;

import com.skepticalone.armour.data.dao.PaymentDao;

import org.threeten.bp.Instant;

import java.math.BigDecimal;

final class PayableViewModelHelper {

    @NonNull
    private final PaymentDao dao;

    PayableViewModelHelper(@NonNull PaymentDao dao) {
        this.dao = dao;
    }

    final void saveNewPayment(final long id, @NonNull final BigDecimal payment) {
        ItemViewModel.runAsync(new Runnable() {
            @Override
            public void run() {
                dao.setPaymentSync(id, payment);
            }
        });
    }

    final void setClaimed(final long id, final boolean claimed) {
        ItemViewModel.runAsync(new Runnable() {
            @Override
            public void run() {
                dao.setClaimedSync(id, claimed ? Instant.now() : null);
            }
        });
    }

    final void setPaid(final long id, final boolean paid) {
        ItemViewModel.runAsync(new Runnable() {
            @Override
            public void run() {
                dao.setPaidSync(id, paid ? Instant.now() : null);
            }
        });
    }

}
