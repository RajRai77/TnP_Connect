package com.fsd_CSE.TnP_Connect.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file") // Binds to "file.upload-dir"
public class FileStorageProperties {
    private String uploadDir = "./uploads"; // Default value

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}