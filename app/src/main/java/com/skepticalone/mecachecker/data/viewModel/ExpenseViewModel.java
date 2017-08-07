package com.skepticalone.mecachecker.data.viewModel;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.entity.ExpenseEntity;

public final class ExpenseViewModel extends ItemViewModel<ExpenseEntity> implements PayableItemViewModelContract<ExpenseEntity>, SingleAddItemViewModelContract<ExpenseEntity> {
//
//    @NonNull
//    private final PayableViewModelHelper payableViewModelHelper;
//
//    public ExpenseViewModel(@NonNull Application application) {
//        super(application);
//        this.payableViewModelHelper = new PayableViewModelHelper(getDao().getPayableDaoHelper());
//    }
//
//    @NonNull
//    public PayableViewModelHelper getPayableViewModelHelper() {
//        return payableViewModelHelper;
//    }
//
//    @NonNull
//    @Override
//    ExpenseCustomDao getDao() {
//        return AppDatabase.getInstance(getApplication()).expenseCustomDao();
//    }
//
    public void addNewExpense() {
        runAsync(new Runnable() {
            @Override
            public void run() {
                postSelectedId(getDao().insertSync(getApplication().getString(R.string.new_expense_title)));
//                postSelectedId(getDao().insertItemSync(new ExpenseEntity(
//                        getApplication().getString(R.string.new_expense_title),
//                        PaymentData.fromPayment(0),
//                        null
//                )));
            }
        });
    }
//
    public void saveNewTitle(@NonNull final String newTitle) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                getDao().setTitleSync(getCurrentItemId(), newTitle);
            }
        });
    }
//
//    @Override
//    public void saveNewComment(final long id, @Nullable final String newComment) {
//        runAsync(new Runnable() {
//            @Override
//            public void run() {
//                getDao().setCommentSync(id, newComment);
//            }
//        });
//    }
}
