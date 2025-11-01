package com.fsd_CSE.TnP_Connect.Enitities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "students")
@Data
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Integer id;

    @Column(name = "tnp_roll_no")
    private String tnprollNo;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    private String branch;
    private Integer year;

    @Column(precision = 4, scale = 2)
    private BigDecimal cgpa;

    @Column(columnDefinition = "TEXT")
    private String skills;

    @Column(name = "profile_pic_url")
    private String profilePicUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createdAt;

    // Relationships
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // Tells Swagger this is output-only
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InternshipApplication> internshipApplications;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // Tells Swagger this is output-only
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SessionRegistration> sessionRegistrations;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTnprollNo() {
        return tnprollNo;
    }

    public void setTnprollNo(String tnprollNo) {
        this.tnprollNo = tnprollNo;
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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
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

    public java.math.BigDecimal getCgpa() {
        return cgpa;
    }

    public void setCgpa(java.math.BigDecimal cgpa) {
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

    public java.time.OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.time.OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public java.util.List<InternshipApplication> getInternshipApplications() {
        return internshipApplications;
    }

    public void setInternshipApplications(java.util.List<InternshipApplication> internshipApplications) {
        this.internshipApplications = internshipApplications;
    }

    public java.util.List<SessionRegistration> getSessionRegistrations() {
        return sessionRegistrations;
    }

    public void setSessionRegistrations(java.util.List<SessionRegistration> sessionRegistrations) {
        this.sessionRegistrations = sessionRegistrations;
    }


}