package com.app.ptjasamutumineralindonesia.detail;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.app.ptjasamutumineralindonesia.detail.attendancecard.AttendanceCard;
import com.app.ptjasamutumineralindonesia.detail.draftSurvey.DraftSurveyManuals;

public class AdapterFragmentSurveyor extends FragmentStatePagerAdapter {

    private int numOfTabs;
    private String idAssignment;
    private String idAssignmentDocNumber;
    public AdapterFragmentSurveyor(FragmentManager fm, int numOfTabs, String idAssignment, String idAssignmentDocNumber) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.idAssignment = idAssignment;
        this.idAssignmentDocNumber = idAssignmentDocNumber;
    }

    public AdapterFragmentSurveyor(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return AttendanceCard.newInstance(idAssignment, idAssignmentDocNumber);
            case 1:
                return DraftSurveyManuals.newInstance(idAssignment, idAssignmentDocNumber);
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return numOfTabs;
    }
}
