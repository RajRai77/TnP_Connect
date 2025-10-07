package com.fsd_CSE.TnP_Connect.DTO;


// =====================================================================================
//  TnPAdminRequest.java (DTO for creating an admin)
// =====================================================================================

public class TnPAdminRequest {
    private String name;
    private String email;
    private String password;
    private String role;
    private String designation;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
}