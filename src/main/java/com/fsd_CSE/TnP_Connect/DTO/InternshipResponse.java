package com.fsd_CSE.TnP_Connect.DTO;

import java.time.LocalDate;
import java.time.OffsetDateTime;


// =====================================================================================
// InternshipResponse.java (DTO for sending internship data back)
// =====================================================================================
public class InternshipResponse {
    private Integer id;
    private String role;
    private String company;
    private String stipend;
    private String eligibility;
    private LocalDate deadline;
    private String description;
    private String status;
    private OffsetDateTime createdAt;
    private String createdByAdminName; // Show the admin's name

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
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
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public String getCreatedByAdminName() { return createdByAdminName; }
    public void setCreatedByAdminName(String createdByAdminName) { this.createdByAdminName = createdByAdminName; }
}
