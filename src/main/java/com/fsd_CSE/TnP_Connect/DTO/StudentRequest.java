package com.fsd_CSE.TnP_Connect.DTO;

import java.math.BigDecimal;

// =====================================================================================
//  StudentRequest.java (DTO for incoming data)
//  This class defines the shape of the JSON you expect when creating a new student.
// =====================================================================================

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class StudentRequest {
    @Getter
    @Setter
    private String name;
    private String email;
    private String password; // Plain text password from the user
    private String branch;
    private Integer year;
    private BigDecimal cgpa;
    private String skills;
    private String profilePicUrl;

    // Getters and Setters are needed. Lombok's @Data handles this automatically.
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
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
