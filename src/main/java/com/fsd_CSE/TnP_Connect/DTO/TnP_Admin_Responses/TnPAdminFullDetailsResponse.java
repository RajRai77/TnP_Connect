package com.fsd_CSE.TnP_Connect.DTO.TnP_Admin_Responses;

import lombok.Data;

import java.util.List;

@Data
public class TnPAdminFullDetailsResponse {
    // Simple fields from TnPAdminResponse
    private Integer id;
    private String name;
    private String email;
    private String role;
    private String designation;

    // Nested lists of created items
    private List<InternshipSummary> createdInternships;
    private List<NotificationSummary> createdNotifications;
    private List<ResourceSummary> createdResources;
    private List<SessionSummary> createdSessions;
    private List<NoteSummary> uploadedNotes;
    private List<ContestSummary> createdContests;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public List<InternshipSummary> getCreatedInternships() {
        return createdInternships;
    }

    public void setCreatedInternships(List<InternshipSummary> createdInternships) {
        this.createdInternships = createdInternships;
    }

    public List<NotificationSummary> getCreatedNotifications() {
        return createdNotifications;
    }

    public void setCreatedNotifications(List<NotificationSummary> createdNotifications) {
        this.createdNotifications = createdNotifications;
    }

    public List<ResourceSummary> getCreatedResources() {
        return createdResources;
    }

    public void setCreatedResources(List<ResourceSummary> createdResources) {
        this.createdResources = createdResources;
    }

    public List<SessionSummary> getCreatedSessions() {
        return createdSessions;
    }

    public void setCreatedSessions(List<SessionSummary> createdSessions) {
        this.createdSessions = createdSessions;
    }

    public List<NoteSummary> getUploadedNotes() {
        return uploadedNotes;
    }

    public void setUploadedNotes(List<NoteSummary> uploadedNotes) {
        this.uploadedNotes = uploadedNotes;
    }

    public List<ContestSummary> getCreatedContests() {
        return createdContests;
    }

    public void setCreatedContests(List<ContestSummary> createdContests) {
        this.createdContests = createdContests;
    }
}
