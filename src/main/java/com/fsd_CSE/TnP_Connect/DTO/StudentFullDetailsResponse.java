package com.fsd_CSE.TnP_Connect.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class StudentFullDetailsResponse {

    private Integer id;
    private String name;
    private String email;
    private String branch;
    private Integer year;
    private BigDecimal cgpa;
    private String skills;
    private String profilePicUrl;
    private String tnprollNo; // Added your field

    // --- NEW: The nested lists of related data ---
    private List<InternshipApplicationSummary> internshipApplications; // Renamed
    private List<SessionRegistrationSummary> sessionRegistrations; // Renamed

    // Getters and Setters for all fields...
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

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getCgpa() {
        return cgpa;
    }

    public void setCgpa(BigDecimal cgpa) {
        this.cgpa = cgpa;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getTnprollNo() {
        return tnprollNo;
    }

    public void setTnprollNo(String tnprollNo) {
        this.tnprollNo = tnprollNo;
    }

    public List<InternshipApplicationSummary> getInternshipApplications() {
        return internshipApplications;
    } // Renamed

    public void setInternshipApplications(List<InternshipApplicationSummary> apps) {
        this.internshipApplications = apps;
    } // Renamed

    public List<SessionRegistrationSummary> getSessionRegistrations() {
        return sessionRegistrations;
    } // Renamed

    public void setSessionRegistrations(List<SessionRegistrationSummary> regs) {
        this.sessionRegistrations = regs;
    } // Renamed


}
