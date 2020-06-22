package com.app.ptjasamutumineralindonesia.detail;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.app.ptjasamutumineralindonesia.detail.attendancecard.AttendanceCard;
import com.app.ptjasamutumineralindonesia.detail.sampledispatch.SampleDispatch;
import com.app.ptjasamutumineralindonesia.detail.samplingmassbasis.SamplingMassBasis;
import com.app.ptjasamutumineralindonesia.detail.samplingtimebasis.SamplingTimeBasis;

public class AdapterFragmentDetail extends FragmentStatePagerAdapter {

    private int numOfTabs;
    private String idAssignment;
    private String idAssignmentDocNumber;
    public AdapterFragmentDetail(FragmentManager fm, int numOfTabs, String idAssignment, String idAssignmentDocNumber) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.idAssignment = idAssignment;
        this.idAssignmentDocNumber = idAssignmentDocNumber;
    }

    public AdapterFragmentDetail(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return AttendanceCard.newInstance(idAssignment, idAssignmentDocNumber);
            case 1:
                return SamplingMassBasis.newInstance(idAssignment, idAssignmentDocNumber);
            case 2:
                return SamplingTimeBasis.newInstance(idAssignment, idAssignmentDocNumber);
            case 3:
                return SampleDispatch.newInstance(idAssignment, idAssignmentDocNumber);
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return numOfTabs;
    }
}
