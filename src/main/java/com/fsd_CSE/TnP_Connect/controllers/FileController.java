package com.fsd_CSE.TnP_Connect.controllers;
import com.fsd_CSE.TnP_Connect.ExceptionHandling.FileNotFoundException;
import com.fsd_CSE.TnP_Connect.ExceptionHandling.FileStorageException;
import com.fsd_CSE.TnP_Connect.config.FileStorageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Objects;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    // --- Logic from Service moved here ---
    private final Path rootLocation;

    // The Controller now directly depends on the properties
    @Autowired
    public FileController(FileStorageProperties properties) {
        this.rootLocation = Paths.get(properties.getUploadDir()).toAbsolutePath().normalize();
    }

    // init() method from Service moved here
    @PostConstruct
    public void init() {
        try {
            // Create the root directory
            Files.createDirectories(rootLocation);
            // Create the sub-directories
            Files.createDirectories(rootLocation.resolve("profilePic"));
            Files.createDirectories(rootLocation.resolve("notes"));
            Files.createDirectories(rootLocation.resolve("resources"));
            log.info("Initialized file storage directories at: {}", rootLocation);
        } catch (IOException e) {
            log.error("Could not initialize storage", e);
            throw new FileStorageException("Could not initialize storage", e);
        }
    }

    // --- ENDPOINT 1: Upload File ---
    @PostMapping(value = "/upload/{subDirectory}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadFile(
            // We also change @RequestParam to @RequestPart for clarity
            @RequestPart("file") MultipartFile file,
            @PathVariable String subDirectory) {

        log.info("Receiving file upload for directory: {}", subDirectory);

        String fileUrl = storeFile(file, subDirectory);
        return ResponseEntity.ok(Map.of("url", fileUrl));
    }

    // --- ENDPOINT 2: Serve File ---
    @GetMapping("/files/{subDirectory}/{filename:.+}")
    public ResponseEntity<Resource> serveFile(
            @PathVariable String subDirectory,
            @PathVariable String filename) {

        // Call the private helper method (logic from service)
        Resource file = loadFileAsResource(filename, subDirectory);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = Files.probeContentType(file.getFile().toPath());
        } catch (IOException e) {
            log.warn("Could not determine file type for: {}", filename);
        }
        // Default content type
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    // =====================================================================================
    // HELPER METHODS (Copied from FileStorageService)
    // These are now private methods within the controller.
    // =====================================================================================

    private String storeFile(MultipartFile file, String subDirectory) {
        try {
            if (file.isEmpty()) {
                throw new FileStorageException("Failed to store empty file.");
            }

            // 1. Sanitize original filename
            String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

            // --- THIS IS THE FIX ---
            // Replace all spaces, colons, and other unsafe characters with underscores
            String sanitizedFilename = originalFilename.replaceAll("[^a-zA-Z0-9\\._-]", "_");
            // --- END OF FIX ---

            // 2. Create a unique filename
            String uniqueFilename = UUID.randomUUID().toString() + "_" + sanitizedFilename;

            // 3. Resolve the target path
            Path subDirPath = rootLocation.resolve(subDirectory).normalize();
            if (!subDirPath.startsWith(rootLocation)) {
                throw new FileStorageException("Cannot store file outside current directory.");
            }
            Path targetLocation = subDirPath.resolve(uniqueFilename);

            // 4. Save the file
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
                log.info("Stored file at: {}", targetLocation);
            }

            // 5. Return the *URL* to access this file
            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/files/")
                    .path(subDirectory)
                    .path("/")
                    .path(uniqueFilename)
                    .toUriString();

        } catch (IOException e) {
            log.error("Failed to store file", e);
            throw new FileStorageException("Failed to store file.", e);
        }
    }

    private Resource loadFileAsResource(String filename, String subDirectory) {
        try {
            Path file = rootLocation.resolve(subDirectory).resolve(filename).normalize();
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                log.warn("Could not read file: {}", filename);
                throw new FileNotFoundException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            log.error("Malformed URL for file: {}", filename, e);
            throw new FileNotFoundException("Could not read file: " + filename, e);
        }
    }
}
