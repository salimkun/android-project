package com.app.ptjasamutumineralindonesia.detail.attendancecard;

public class AttendanceResult {
    private String id;
    private String documentNumber;
    private String documentDate;
    private String documentStatus;
    private String bargeId;
    private String assignmentWorkOrderId;
    private String bargeName;
    private String assignmentWorkOrderDocumentNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(String documentDate) {
        this.documentDate = documentDate;
    }

    public String getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(String documentStatus) {
        this.documentStatus = documentStatus;
    }

    public String getBargeId() {
        return bargeId;
    }

    public void setBargeId(String bargeId) {
        this.bargeId = bargeId;
    }

    public String getAssignmentWorkOrderId() {
        return assignmentWorkOrderId;
    }

    public void setAssignmentWorkOrderId(String assignmentWorkOrderId) {
        this.assignmentWorkOrderId = assignmentWorkOrderId;
    }

    public String getBargeName() {
        return bargeName;
    }

    public void setBargeName(String bargeName) {
        this.bargeName = bargeName;
    }

    public String getAssignmentWorkOrderDocumentNumber() {
        return assignmentWorkOrderDocumentNumber;
    }

    public void setAssignmentWorkOrderDocumentNumber(String assignmentWorkOrderDocumentNumber) {
        this.assignmentWorkOrderDocumentNumber = assignmentWorkOrderDocumentNumber;
    }
}
