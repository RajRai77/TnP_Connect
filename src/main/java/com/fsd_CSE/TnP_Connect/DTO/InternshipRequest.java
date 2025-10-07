package com.fsd_CSE.TnP_Connect.DTO;

import java.time.LocalDate;

// FILE 1: InternshipRequest.java (DTO for creating/updating an internship)
public class InternshipRequest {
    private String role;
    private String company;
    private String stipend;
    private String eligibility;
    private LocalDate deadline;
    private String description;
    private String status;
    private Integer createdByAdminId; // ID of the admin creating this post

    // Getters and Setters
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public String getStipend() { return stipend; }
    public void setStipend(String stipend) { this.stipend = stipend; }
    public String getEligibility() { return eligibility; }
    public void setEligibility(String eligibility) { this.eligibility = eligibility; }
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getCreatedByAdminId() { return createdByAdminId; }
    public void setCreatedByAdminId(Integer createdByAdminId) { this.createdByAdminId = createdByAdminId; }
}
