package com.fsd_CSE.TnP_Connect.Enitities;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;


@Entity
@Table(name = "tnp_admins")
public class TnPAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    private String role;
    private String designation;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createdAt;

    // --- Relationships ---
    // This admin can create many items.
    @OneToMany(mappedBy = "createdByAdmin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<Internship> createdInternships;

    @OneToMany(mappedBy = "postedByAdmin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<Notification> createdNotifications;

    @OneToMany(mappedBy = "createdByAdmin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<Resource> createdResources;

    @OneToMany(mappedBy = "createdByAdmin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<Session> createdSessions;

    @OneToMany(mappedBy = "uploadedByAdmin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<Notes> uploadedNotes;

    @OneToMany(mappedBy = "createdByAdmin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<Contest> createdContests;

    // --- Manually Added Getters and Setters ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    //This getter will be used when we have to add new API
    //getter and setter for Relationships
    public java.util.List<Internship> getCreatedInternships() {
        return createdInternships;
    }
    public void setCreatedInternships(java.util.List<Internship> createdInternships) {
        this.createdInternships = createdInternships;
    }
    public java.util.List<Notification> getCreatedNotifications() {
        return createdNotifications;
    }
    public void setCreatedNotifications(java.util.List<Notification> createdNotifications) {
        this.createdNotifications = createdNotifications;
    }
    public java.util.List<Resource> getCreatedResources() {
        return createdResources;
    }
    public void setCreatedResources(java.util.List<Resource> createdResources) {
        this.createdResources = createdResources;
    }
    public java.util.List<Session> getCreatedSessions() {
        return createdSessions;
    }
    public void setCreatedSessions(java.util.List<Session> createdSessions) {
        this.createdSessions = createdSessions;
    }
    public java.util.List<Notes> getUploadedNotes() {
        return uploadedNotes;
    }
    public void setUploadedNotes(java.util.List<Notes> uploadedNotes) {
        this.uploadedNotes = uploadedNotes;
    }
    public java.util.List<Contest> getCreatedContests() {
        return createdContests;
    }
    public void setCreatedContests(java.util.List<Contest> createdContests) {
        this.createdContests = createdContests;
    }

}