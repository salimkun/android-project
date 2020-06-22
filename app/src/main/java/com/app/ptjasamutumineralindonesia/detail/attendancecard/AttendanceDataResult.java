package com.app.ptjasamutumineralindonesia.detail.attendancecard;

public class AttendanceDataResult {
    private String id;
    private String remarks;
    private String timesheetId;
    private String timesheetTime;
    private String weatherType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTimesheetId() {
        return timesheetId;
    }

    public void setTimesheetId(String timesheetId) {
        this.timesheetId = timesheetId;
    }

    public String getTimesheetTime() {
        return timesheetTime;
    }

    public void setTimesheetTime(String timesheetTime) {
        this.timesheetTime = timesheetTime;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }
}
