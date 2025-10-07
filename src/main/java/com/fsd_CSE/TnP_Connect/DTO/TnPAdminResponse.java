package com.fsd_CSE.TnP_Connect.DTO;


// =====================================================================================
//  TnPAdminResponse.java (DTO for sending admin data back)
//  Note: No password field for security.
// =====================================================================================

public class TnPAdminResponse {
    private Integer id;
    private String name;
    private String email;
    private String role;
    private String designation;

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
}