package com.fsd_CSE.TnP_Connect.DTO;

// ResourceRequest.java (DTO for creating a resource)

public class ResourceRequest {
    private String title;
    private String type;
    private String fileUrl;
    private String description;
    private Integer createdByAdminId;

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getCreatedByAdminId() { return createdByAdminId; }
    public void setCreatedByAdminId(Integer createdByAdminId) { this.createdByAdminId = createdByAdminId; }
}
