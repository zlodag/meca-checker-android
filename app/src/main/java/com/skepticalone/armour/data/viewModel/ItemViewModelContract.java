package com.skepticalone.armour.data.viewModel;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.skepticalone.armour.data.dao.ItemDao;

import java.util.List;

public interface ItemViewModelContract<FinalItem> {
    @NonNull
    LiveData<List<FinalItem>> fetchItems();
    void selectItem(long id);
    @NonNull
    LiveData<FinalItem> getCurrentItem();
    void saveNewComment(@Nullable String comment);
    @NonNull
    LiveData<Integer> getErrorMessage();
//    void deleteItem(long id);
    @NonNull
    LiveData<View.OnClickListener> getDeletedItemRestorer();
    @NonNull
    LiveData<FinalItem> fetchItem(long id);

}
