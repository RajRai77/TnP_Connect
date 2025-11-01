package com.fsd_CSE.TnP_Connect;

import com.fsd_CSE.TnP_Connect.config.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.context.properties.EnableConfigurationProperties; // <-- Import this

@SpringBootApplication
@EnableConfigurationProperties(FileStorageProperties.class) // <-- ADD THIS ANNOTATION
public class TnPConnectApplication {

	public static void main(String[] args) {
		SpringApplication.run(TnPConnectApplication.class, args);
	}

}

