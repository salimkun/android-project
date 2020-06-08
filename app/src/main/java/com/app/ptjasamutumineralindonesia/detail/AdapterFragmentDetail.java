package com.app.ptjasamutumineralindonesia.detail;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class AdapterFragmentDetail extends FragmentStatePagerAdapter {

    private int numOfTabs;
    public AdapterFragmentDetail(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    public AdapterFragmentDetail(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AttendanceCard();
            case 1:
                return new SamplingMassBasis();
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return numOfTabs;
    }
}
