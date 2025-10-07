package com.fsd_CSE.TnP_Connect.DTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

// =====================================================================================
//  StudentResponse.java (DTO for outgoing data)
//  This class defines the shape of the JSON you send back to the user.
//  It has NO password field for security.
// =====================================================================================

@Data
public class StudentResponse {
    private Integer id;
    private String name;
    private String email;
    private String branch;
    private Integer year;
    private BigDecimal cgpa;
    private String skills;
    private String profilePicUrl;

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    public BigDecimal getCgpa() { return cgpa; }
    public void setCgpa(BigDecimal cgpa) { this.cgpa = cgpa; }
    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }
    public String getProfilePicUrl() { return profilePicUrl; }
    public void setProfilePicUrl(String profilePicUrl) { this.profilePicUrl = profilePicUrl; }

}
