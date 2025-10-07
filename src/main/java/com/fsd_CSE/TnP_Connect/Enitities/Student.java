package com.fsd_CSE.TnP_Connect.Enitities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "students")
@Data
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Integer id;

    // We will clarify this field's purpose later
    @Column(name = "tnp_roll_no")
    private String tnprollNo;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    private String branch;

    private Integer year;

    // Using BigDecimal for precision is better for CGPA
    @Column(precision = 3, scale = 2)
    private BigDecimal cgpa;

    @Column(columnDefinition = "TEXT")
    private String skills;

    @Column(name = "profile_pic_url")
    private String profilePicUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createdAt;
    // --- Manually Added Getters and Setters ---

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

//    public java.util.List<InternshipApplication> getInternshipApplications() {
//        return internshipApplications;
//    }
//
//    public void setInternshipApplications(java.util.List<InternshipApplication> internshipApplications) {
//        this.internshipApplications = internshipApplications;
//    }
//
//    public java.util.List<SessionRegistration> getSessionRegistrations() {
//        return sessionRegistrations;
//    }
//
//    public void setSessionRegistrations(java.util.List<SessionRegistration> sessionRegistrations) {
//        this.sessionRegistrations = sessionRegistrations;
//    }

    // --- Relationships ---
    // Note: Your IDE will show errors on the next two lines until we create those files.
    // That is expected.
//    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<InternshipApplication> internshipApplications;
//
//    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<SessionRegistration> sessionRegistrations;
}