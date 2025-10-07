package com.fsd_CSE.TnP_Connect.DTO;


import java.time.LocalDate;

// FILE 1: NotificationRequest.java (DTO for creating a notification)
// =====================================================================================
public class NotificationRequest {
    private String title;
    private String content;
    private String targetBranch;
    private Integer targetYear;
    private String link;
    private LocalDate eventDate;
    private String category;
    private Integer postedByAdminId;

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getTargetBranch() { return targetBranch; }
    public void setTargetBranch(String targetBranch) { this.targetBranch = targetBranch; }
    public Integer getTargetYear() { return targetYear; }
    public void setTargetYear(Integer targetYear) { this.targetYear = targetYear; }
    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }
    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Integer getPostedByAdminId() { return postedByAdminId; }
    public void setPostedByAdminId(Integer postedByAdminId) { this.postedByAdminId = postedByAdminId; }
}
