package com.example.emedic.Doctor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        PacientiFragment stFragment = new PacientiFragment();
        position = position+1;
        Bundle bundle = new Bundle();
        bundle.putString("message", "Fragment: "+position);
        stFragment.setArguments(bundle);
        return stFragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        position = position+1;

        return "Fragment "+position;
    }
}
