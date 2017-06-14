package com.skepticalone.mecachecker.components;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.behaviours.DetailFragmentBehaviour;
import com.skepticalone.mecachecker.behaviours.InvolvesPayment;

import org.joda.time.DateTime;

import java.math.BigDecimal;

class PaymentData extends AbstractData implements SwitchListItemViewHolder.Callbacks {

    private final Callbacks mCallbacks;
    private BigDecimal mPayment;
    @NonNull
    private final View.OnClickListener
            mPaymentListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCallbacks.showDialogFragment(
                    MoneyDialogFragment.newInstance(mPayment, mCallbacks.getMoneyDescriptor(), mCallbacks.getContentUri(), mCallbacks.getColumnNameMoney()),
                    LifecycleConstants.MONEY_DIALOG
            );
        }
    },
            mCommentListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallbacks.showDialogFragment(
                            PlainTextDialogFragment.newInstance(mComment, R.string.comment, mCallbacks.getContentUri(), mCallbacks.getColumnNameComment()),
                            LifecycleConstants.COMMENT_DIALOG
                    );
                }
            };
    @Nullable
    private String mComment;
    @Nullable
    private DateTime mClaimed, mPaid;

    PaymentData(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    boolean isClaimed() {
        return mClaimed != null;
    }

    @Override
    public void readFromPositionedCursor(@NonNull Cursor cursor) {
        mPayment = BigDecimal.valueOf(cursor.getInt(mCallbacks.getColumnIndexMoney()), 2);
        mComment = cursor.isNull(mCallbacks.getColumnIndexComment()) ? null : cursor.getString(mCallbacks.getColumnIndexComment());
        mClaimed = cursor.isNull(mCallbacks.getColumnIndexClaimed()) ? null : new DateTime(cursor.getLong(mCallbacks.getColumnIndexClaimed()));
        mPaid = (!isClaimed() || cursor.isNull(mCallbacks.getColumnIndexPaid())) ? null : new DateTime(cursor.getLong(mCallbacks.getColumnIndexPaid()));
    }

    @Nullable
    @Override
    ViewHolderType getViewHolderType(int position) {
        if (position == mCallbacks.getRowNumberMoney() || position == mCallbacks.getRowNumberComment()) {
            return ViewHolderType.PLAIN;
        } else if (position == mCallbacks.getRowNumberClaimed() || position == mCallbacks.getRowNumberPaid()) {
            return ViewHolderType.SWITCH;
        } else {
            return null;
        }
    }

    @Override
    boolean bindToHolder(Context context, PlainListItemViewHolder holder, int position) {
        if (position == mCallbacks.getRowNumberMoney()) {
            holder.rootBind(context, mCallbacks.getMoneyIcon(), mCallbacks.getMoneyDescriptor(), context.getString(R.string.currency_format, mPayment), mPaymentListener);
        } else if (position == mCallbacks.getRowNumberComment()) {
            holder.rootBind(context, R.drawable.ic_comment_black_24dp, R.string.comment, mComment, mCommentListener);
        } else return false;
        return true;
    }

    @Override
    boolean bindToHolder(Context context, SwitchListItemViewHolder holder, int position) {
        if (position == mCallbacks.getRowNumberClaimed()) {
            holder.bindClaimed(context, mClaimed, mPaid != null);
        } else if (position == mCallbacks.getRowNumberPaid()) {
            holder.bindPaid(context, mPaid);
        } else return false;
        return true;
    }

    @Override
    public void onCheckedChanged(SwitchListItemViewHolder.SwitchType switchType, boolean isChecked) {
        String columnName;
        switch (switchType) {
            case PAID:
                columnName = mCallbacks.getColumnNamePaid();
                break;
            case CLAIMED:
                columnName = mCallbacks.getColumnNameClaimed();
                break;
            default:
                throw new IllegalStateException();
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(columnName, isChecked ? System.currentTimeMillis() : null);
        mCallbacks.update(contentValues);
    }

    interface Callbacks extends DetailFragmentBehaviour, InvolvesPayment {
    }
}