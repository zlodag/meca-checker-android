package com.skepticalone.mecachecker.data.viewModel;

import android.app.Application;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.dao.ExpenseDao;
import com.skepticalone.mecachecker.data.db.AppDatabase;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.data.util.PaymentData;

import org.joda.time.DateTime;

import java.math.BigDecimal;


public final class ExpenseViewModel extends ItemViewModel<ExpenseEntity, ExpenseDao>
        implements TitleViewModelContract, PayableViewModelContract<ExpenseEntity> {

    public ExpenseViewModel(Application application) {
        super(application);
    }

    @NonNull
    @Override
    ExpenseDao onCreateDao(@NonNull AppDatabase database) {
        return database.expenseDao();
    }

    @Override
    public void saveNewTitle(@NonNull String newTitle) {
        ExpenseEntity expense = getCurrentItem().getValue();
        if (expense != null) {
            getDao().setTitleSync(expense.getId(), newTitle);
        }
    }

    @Override
    public void saveNewPayment(@NonNull BigDecimal payment) {
        ExpenseEntity expense = getCurrentItem().getValue();
        if (expense != null) {
            getDao().setPaymentSync(expense.getId(), payment);
        }
    }

    @Override
    public void setClaimed(boolean claimed) {
        ExpenseEntity expense = getCurrentItem().getValue();
        if (expense != null) {
            getDao().setClaimedSync(expense.getId(), claimed ? DateTime.now() : null);
        }
    }

    @Override
    public void setPaid(boolean paid) {
        ExpenseEntity expense = getCurrentItem().getValue();
        if (expense != null) {
            getDao().setPaidSync(expense.getId(), paid ? DateTime.now() : null);
        }
    }

    public void addNewExpense() {
        getDao().insertItemSync(new ExpenseEntity(
                getApplication().getString(R.string.new_expense_title),
                new PaymentData(0),
                null
        ));
    }
}
