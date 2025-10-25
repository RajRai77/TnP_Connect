package com.fsd_CSE.TnP_Connect.controllers;
import com.fsd_CSE.TnP_Connect.DTO.InternshipResponse;
import com.fsd_CSE.TnP_Connect.DTO.StudentApplicantSummary;
import com.fsd_CSE.TnP_Connect.Repository.InternshipRepository;
import com.fsd_CSE.TnP_Connect.Repository.TnPAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fsd_CSE.TnP_Connect.Enitities.*; // Importing all entities
import com.fsd_CSE.TnP_Connect.ExceptionHandling.ResourceNotFoundException;
// Needed for create logic

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;

// For the summary class

import java.util.List;
import java.util.stream.Collectors;

// InternshipController.java (API endpoints for internships)

@RestController
@RequestMapping("/api/internships")
public class InternshipController {
    // --- DEPENDENCIES ---
    @Autowired private InternshipRepository internshipRepository;
    @Autowired private TnPAdminRepository tnpAdminRepository; // Needed for create logic

    // --- LOGGER (Moved from Service) ---
    private static final Logger log = LoggerFactory.getLogger(InternshipController.class);

    // --- ENDPOINT 1: Create Internship ---
    // NOW ACCEPTS THE Internship ENTITY
    // Requires createdByAdminId in the JSON body
    @PostMapping("/")
    public ResponseEntity<InternshipResponse> createInternship(@RequestBody Internship requestInternship) {

        // --- LOGIC MOVED FROM SERVICE ---
        Integer adminId = requestInternship.getCreatedByAdmin() != null ? requestInternship.getCreatedByAdmin().getId() : null; // Get admin ID from nested object
        if (adminId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "createdByAdminId is required.");
        }
        log.info("Attempting to create internship posted by admin ID: {}", adminId);

        TnPAdmin admin = tnpAdminRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with ID: " + adminId));

        // Create a new Internship object to save, copy fields from request
        Internship internship = new Internship();
        internship.setRole(requestInternship.getRole());
        internship.setCompany(requestInternship.getCompany());
        internship.setStipend(requestInternship.getStipend());
        internship.setEligibility(requestInternship.getEligibility());
        internship.setDeadline(requestInternship.getDeadline());
        internship.setDescription(requestInternship.getDescription());
        internship.setStatus(requestInternship.getStatus());
        internship.setCreatedByAdmin(admin); // Set the relationship

        Internship savedInternship = internshipRepository.save(internship);
        log.info("Successfully created internship with ID: {}", savedInternship.getId());

        // Convert to safe response
        return new ResponseEntity<>(convertToResponse(savedInternship), HttpStatus.CREATED);
    }

    // --- ENDPOINT 2: Get All Internships ---
    @GetMapping("/")
    public ResponseEntity<List<InternshipResponse>> getAllInternships() {
        log.info("Fetching all internships");

        // --- LOGIC MOVED FROM SERVICE ---
        List<Internship> internships = internshipRepository.findAll();

        List<InternshipResponse> responses = internships.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    // --- ENDPOINT 3: Get Internship By ID (Simple Details) ---
    @GetMapping("/{id}")
    public ResponseEntity<InternshipResponse> getInternshipById(@PathVariable Integer id) {
        log.info("Fetching internship with ID: {}", id);
        Internship internship = internshipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Internship not found with ID: " + id));
        return ResponseEntity.ok(convertToResponse(internship));
    }


    // --- ENDPOINT 4: Delete Internship ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInternship(@PathVariable Integer id) {
        log.warn("Attempting to delete internship with ID: {}", id);

        // --- LOGIC MOVED FROM SERVICE ---
        if(!internshipRepository.existsById(id)) {
            // Use custom exception for consistency
            throw new ResourceNotFoundException("Internship not found with ID: " + id);
        }
        internshipRepository.deleteById(id);
        log.info("Deleted internship with ID: {}", id);

        return ResponseEntity.noContent().build();
    }

    // =================================================================================
    // NEW RELATIONSHIP ENDPOINT
    // =================================================================================

    // --- ENDPOINT 5: Get all applicants for a specific internship ---
    @GetMapping("/{id}/applicants")
    public ResponseEntity<List<StudentApplicantSummary>> getInternshipApplicants(@PathVariable Integer id) {
        log.info("Fetching applicants for internship ID: {}", id);
        // 1. Find the internship
        Internship internship = internshipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Internship not found with ID: " + id));

        // 2. Get the list of applications from the entity
        List<InternshipApplication> applications = internship.getApplications();

        // 3. Convert to the summary class
        List<StudentApplicantSummary> summaries = applications.stream()
                .map(this::convertAppToApplicantSummary)
                .collect(Collectors.toList());

        return ResponseEntity.ok(summaries);
    }


    // =================================================================================
    // HELPER METHODS (Moved from Service)
    // =================================================================================

    // Helper to convert Entity to SIMPLE Response
    private InternshipResponse convertToResponse(Internship internship) {
        InternshipResponse response = new InternshipResponse();
        response.setId(internship.getId());
        response.setRole(internship.getRole());
        response.setCompany(internship.getCompany());
        response.setStipend(internship.getStipend());
        response.setEligibility(internship.getEligibility());
        response.setDeadline(internship.getDeadline());
        response.setDescription(internship.getDescription());
        response.setStatus(internship.getStatus());
        response.setCreatedAt(internship.getCreatedAt());
        if (internship.getCreatedByAdmin() != null) {
            response.setCreatedByAdminName(internship.getCreatedByAdmin().getName());
        }
        return response;
    }

    // NEW Helper to convert Application to Applicant Summary
    private StudentApplicantSummary convertAppToApplicantSummary(InternshipApplication app) {
        StudentApplicantSummary summary = new StudentApplicantSummary();
        summary.setApplicationStatus(app.getStatus());
        summary.setAppliedAt(app.getAppliedAt());
        if (app.getStudent() != null) {
            summary.setStudentId(app.getStudent().getId());
            summary.setStudentName(app.getStudent().getName());
            summary.setStudentEmail(app.getStudent().getEmail());
            summary.setStudentBranch(app.getStudent().getBranch());
        }
        return summary;
    }
}