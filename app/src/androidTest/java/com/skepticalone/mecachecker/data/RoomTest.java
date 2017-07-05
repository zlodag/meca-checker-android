package com.skepticalone.mecachecker.data;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.skepticalone.mecachecker.db.AppDatabase;
import com.skepticalone.mecachecker.db.dao.ExpenseDao;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class RoomTest {
    private ExpenseDao mExpenseDao;
    private AppDatabase mDb;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        mExpenseDao = mDb.expenseDao();
    }

    @After
    public void closeDb() throws IOException {
        mDb.close();
    }
//
//    @Test
//    public void writeExpenseAndReadInList() throws Exception {
//        ExpenseEntity expense = new ExpenseEntity("One big expense", BigDecimal.valueOf(4500, 2), "  A happy day   ", null, null);
//        mExpenseDao.insertExpense(expense);
//        mExpenseDao.getExpenses().getValue();
//        assertThat(mExpenseDao.getExpenses().getValue().size(), is(1));
//        assertThat(mExpenseDao.getExpense(1).getValue().getTitle(), is(expense.getTitle()));
////        assertThat(mExpenseDao.getExpense(1).getComment(), is("A happy day"));
//    }

}
