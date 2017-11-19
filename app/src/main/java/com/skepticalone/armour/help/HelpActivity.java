package com.skepticalone.armour.help;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.skepticalone.armour.ui.common.MainActivity;

public final class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, HelpFragment.newInstance(getIntent().getIntExtra(MainActivity.ITEM_TYPE, 0)))
                    .commit();
        }
//        @LayoutRes final int layout;
//        @StringRes final int title;
//        switch (getIntent().getIntExtra(MainActivity.ITEM_TYPE, 0)) {
//            case R.id.rostered:
//                layout = R.layout.help_activity_rostered_shifts;
//                title = R.string.rostered_shifts;
//                break;
//            case R.id.additional:
//                layout = R.layout.help_activity_additional_shifts;
//                title = R.string.additional_shifts;
//                break;
//            case R.id.cross_cover:
//                layout = R.layout.help_activity_cross_cover;
//                title = R.string.cross_cover_shifts;
//                break;
//            case R.id.expenses:
//                layout = R.layout.help_activity_expenses;
//                title = R.string.expenses;
//                break;
//            default:
//                throw new IllegalStateException();
//        }
//        setContentView(layout);
//        getSupportActionBar().setTitle(getString(R.string.help_qualified, getString(title)));
    }
}
