package com.fsd_CSE.TnP_Connect.DTO;


import java.time.OffsetDateTime;

// FILE 1: ContestRequest.java (DTO for creating a contest)
// =====================================================================================
public class ContestRequest {
    private String title;
    private String platform;
    private String contestUrl;
    private String description;
    private OffsetDateTime startDatetime;
    private OffsetDateTime endDatetime;
    private Integer createdByAdminId;

    // Getters and Setters...
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }
    public String getContestUrl() { return contestUrl; }
    public void setContestUrl(String contestUrl) { this.contestUrl = contestUrl; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public OffsetDateTime getStartDatetime() { return startDatetime; }
    public void setStartDatetime(OffsetDateTime startDatetime) { this.startDatetime = startDatetime; }
    public OffsetDateTime getEndDatetime() { return endDatetime; }
    public void setEndDatetime(OffsetDateTime endDatetime) { this.endDatetime = endDatetime; }
    public Integer getCreatedByAdminId() { return createdByAdminId; }
    public void setCreatedByAdminId(Integer createdByAdminId) { this.createdByAdminId = createdByAdminId; }
}

