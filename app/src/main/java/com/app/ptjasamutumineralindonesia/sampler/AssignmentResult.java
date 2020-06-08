package com.app.ptjasamutumineralindonesia.sampler;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AssignmentResult {
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public void setDocumentDate(String documentDate) {
        this.documentDate = documentDate;
    }

    public void setDocumentStatus(String documentStatus) {
        this.documentStatus = documentStatus;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public void setAssignmentLetterId(String assignmentLetterId) {
        this.assignmentLetterId = assignmentLetterId;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public void setAssignmentLetterDocumentNumber(String assignmentLetterDocumentNumber) {
        this.assignmentLetterDocumentNumber = assignmentLetterDocumentNumber;
    }

    public void setAssignmentLetter(AssignmentLetterResult assignmentLetter) {
        this.assignmentLetter = assignmentLetter;
    }

    public String getId() {
        return id;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public String getDocumentDate() {
        return documentDate;
    }

    public String getDocumentStatus() {
        return documentStatus;
    }

    public String getDescription() {
        return description;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getReason() {
        return reason;
    }

    public String getWorkType() {
        return workType;
    }

    public String getStatus() {
        return status;
    }

    public String getWorkerId() {
        return workerId;
    }

    public String getAssignmentLetterId() {
        return assignmentLetterId;
    }

    public String getWorkerName() {
        return workerName;
    }

    public String getAssignmentLetterDocumentNumber() {
        return assignmentLetterDocumentNumber;
    }

    public AssignmentLetterResult getAssignmentLetter() {
        return assignmentLetter;
    }

    private String documentNumber;
    private String documentDate;
    private String documentStatus;
    private String description;
    private String startDate;
    private String endDate;
    private String reason;
    private String workType;
    private String status;
    private String workerId;
    private String assignmentLetterId;
    private String workerName;
    private String assignmentLetterDocumentNumber;
    private AssignmentLetterResult assignmentLetter;
}
