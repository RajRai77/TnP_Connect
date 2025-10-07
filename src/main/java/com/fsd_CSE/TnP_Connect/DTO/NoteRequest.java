package com.fsd_CSE.TnP_Connect.DTO;

public class NoteRequest {


// FILE 1: NoteRequest.java (DTO for uploading a note)

    private String title;
    private String description;
    private String fileUrl;
    private String targetBranch;
    private Integer targetYear;
    private Integer uploadedByAdminId;

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getTargetBranch() {
        return targetBranch;
    }

    public void setTargetBranch(String targetBranch) {
        this.targetBranch = targetBranch;
    }

    public Integer getTargetYear() {
        return targetYear;
    }

    public void setTargetYear(Integer targetYear) {
        this.targetYear = targetYear;
    }

    public Integer getUploadedByAdminId() {
        return uploadedByAdminId;
    }

    public void setUploadedByAdminId(Integer uploadedByAdminId) {
        this.uploadedByAdminId = uploadedByAdminId;
    }
}