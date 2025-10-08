package com.fsd_CSE.TnP_Connect.DTO;

public class LoginRequest {


//  LoginRequest.java (DTO specifically for the login endpoint)


        private String email;
        private String password;

        // Getters and Setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

}
