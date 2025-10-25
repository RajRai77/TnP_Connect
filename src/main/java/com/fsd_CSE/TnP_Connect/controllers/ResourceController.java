package com.fsd_CSE.TnP_Connect.controllers;

import com.fsd_CSE.TnP_Connect.DTO.ResourceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fsd_CSE.TnP_Connect.Enitities.*; // Importing all entities
import com.fsd_CSE.TnP_Connect.ExceptionHandling.ResourceNotFoundException;
import com.fsd_CSE.TnP_Connect.Repository.ResourceRepository;
import com.fsd_CSE.TnP_Connect.Repository.TnPAdminRepository; // Needed for create logic

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException; // Added back temporarily for BAD_REQUEST

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    // --- DEPENDENCIES ---
    @Autowired private ResourceRepository resourceRepository;
    @Autowired private TnPAdminRepository tnpAdminRepository;

    // --- LOGGER ---
    private static final Logger log = LoggerFactory.getLogger(ResourceController.class);

    // --- ENDPOINT 1: Create Resource ---
    // NOW ACCEPTS THE Resource ENTITY DIRECTLY
    @PostMapping("/")
    public ResponseEntity<ResourceResponse> createResource(@RequestBody Resource requestResource) {

        // --- LOGIC MOVED FROM SERVICE & ADAPTED ---
        // Get admin ID from nested object
        Integer adminId;
        if (requestResource.getCreatedByAdmin() != null && requestResource.getCreatedByAdmin().getId() != null) {
            adminId = requestResource.getCreatedByAdmin().getId();
        } else {
            adminId = null;
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "createdByAdmin object with id is required.");
        }
        log.info("Admin ID: {} is creating a new resource", adminId);

        TnPAdmin admin = tnpAdminRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with ID: " + adminId));

        // Create a new entity to save, copying necessary fields
        Resource resource = new Resource();
        resource.setTitle(requestResource.getTitle());
        resource.setType(requestResource.getType());
        resource.setFileUrl(requestResource.getFileUrl());
        resource.setDescription(requestResource.getDescription());
        resource.setCreatedByAdmin(admin); // Set relationship using fetched admin

        Resource savedResource = resourceRepository.save(resource);
        log.info("Successfully created resource with ID: {}", savedResource.getId());

        // Convert to safe response
        return new ResponseEntity<>(convertToResponse(savedResource), HttpStatus.CREATED);
    }

    // --- ENDPOINT 2: Get All Resources ---
    @GetMapping("/")
    public ResponseEntity<List<ResourceResponse>> getAllResources() {
        log.info("Fetching all resources");

        // --- LOGIC MOVED FROM SERVICE ---
        List<Resource> resources = resourceRepository.findAll();

        List<ResourceResponse> responses = resources.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    // --- NEW ENDPOINT 3: Get Resource by ID ---
    @GetMapping("/{id}")
    public ResponseEntity<ResourceResponse> getResourceById(@PathVariable Integer id) {
        log.info("Fetching resource with ID: {}", id);
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + id));
        return ResponseEntity.ok(convertToResponse(resource));
    }

    // --- ENDPOINT 4: Delete Resource ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable Integer id) {
        log.warn("Attempting to delete resource with ID: {}", id);

        // --- LOGIC MOVED FROM SERVICE ---
        if (!resourceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resource not found with ID: " + id);
        }
        resourceRepository.deleteById(id);
        log.info("Successfully deleted resource with ID: {}", id);

        return ResponseEntity.noContent().build();
    }


    // =================================================================================
    // HELPER METHOD (Moved from Service)
    // =================================================================================
    private ResourceResponse convertToResponse(Resource resource) {
        ResourceResponse response = new ResourceResponse();
        response.setId(resource.getId());
        response.setTitle(resource.getTitle());
        response.setType(resource.getType());
        response.setFileUrl(resource.getFileUrl());
        response.setDescription(resource.getDescription());
        response.setCreatedAt(resource.getCreatedAt());
        if (resource.getCreatedByAdmin() != null) {
            response.setCreatedByAdminName(resource.getCreatedByAdmin().getName());
        }
        return response;
    }
}
