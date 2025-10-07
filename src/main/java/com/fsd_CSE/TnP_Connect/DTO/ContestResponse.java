package com.fsd_CSE.TnP_Connect.DTO;

import java.time.OffsetDateTime;

// FILE 2: ContestResponse.java (DTO for viewing a contest)
// =====================================================================================
public class ContestResponse {
    private Integer id;
    private String title;
    private String platform;
    private String contestUrl;
    private String description;
    private OffsetDateTime startDatetime;
    private OffsetDateTime endDatetime;
    private String status; // Dynamic status: UPCOMING, LIVE, COMPLETED
    private String createdByAdminName;

    // Getters and Setters...
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
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
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCreatedByAdminName() { return createdByAdminName; }
    public void setCreatedByAdminName(String createdByAdminName) { this.createdByAdminName = createdByAdminName; }
}