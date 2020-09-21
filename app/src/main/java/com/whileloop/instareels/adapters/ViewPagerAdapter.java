package com.whileloop.instareels.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.whileloop.instareels.views.Downloads;
import com.whileloop.instareels.views.Home;

public class ViewPagerAdapter extends FragmentStateAdapter {


    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        return position == 0 ? new Home() : new Downloads();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

