package com.fsd_CSE.TnP_Connect.controllers;
import com.fsd_CSE.TnP_Connect.DTO.LoginRequest;
import com.fsd_CSE.TnP_Connect.DTO.TnPAdminResponse;
import com.fsd_CSE.TnP_Connect.DTO.TnP_Admin_Responses.*;
import com.fsd_CSE.TnP_Connect.Repository.TnPAdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fsd_CSE.TnP_Connect.Enitities.*; // Importing all entities
import com.fsd_CSE.TnP_Connect.ExceptionHandling.ResourceNotFoundException;

import org.springframework.web.server.ResponseStatusException;

// For the summary classes

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/admins")
public class TnPAdminController {

    // --- DEPENDENCIES ---
    @Autowired
    private TnPAdminRepository tnpAdminRepository;

    // --- LOGGER (Moved from Service) ---
    private static final Logger log = LoggerFactory.getLogger(TnPAdminController.class);

    /**
     * Endpoint to register a new admin account.
     * NOW ACCEPTS THE TnPAdmin ENTITY.
     */
    @PostMapping("/register")
    public ResponseEntity<TnPAdminResponse> registerAdmin(@RequestBody TnPAdmin adminRequest) {
        log.info("Registering new admin with email: {}", adminRequest.getEmail());

        // --- LOGIC MOVED FROM SERVICE ---
        if (tnpAdminRepository.findByEmail(adminRequest.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Admin with this email already exists.");
        }

        TnPAdmin admin = new TnPAdmin();
        admin.setName(adminRequest.getName());
        admin.setEmail(adminRequest.getEmail());
        admin.setRole(adminRequest.getRole());
        admin.setDesignation(adminRequest.getDesignation());

        // Use the simpleHash function (assumes plain text in 'passwordHash' field)
        admin.setPasswordHash(simpleHash(adminRequest.getPasswordHash()));

        TnPAdmin savedAdmin = tnpAdminRepository.save(admin);
        log.info("Registered new admin with ID: {}", savedAdmin.getId());

        // Convert to safe response
        return new ResponseEntity<>(convertToResponse(savedAdmin), HttpStatus.CREATED);
    }

    /**
     * Endpoint for an admin to log in.
     */
    @PostMapping("/login")
    public ResponseEntity<TnPAdminResponse> loginAdmin(@RequestBody LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());

        // --- LOGIC MOVED FROM SERVICE ---
        TnPAdmin admin = tnpAdminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        // Use the same simpleHash for checking
        String expectedPasswordHash = simpleHash(request.getPassword());
        if (!expectedPasswordHash.equals(admin.getPasswordHash())) {
            log.warn("Failed login attempt for email: {}", request.getEmail());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        log.info("Admin with ID: {} successfully logged in.", admin.getId());
        return new ResponseEntity<>(convertToResponse(admin), HttpStatus.OK);
    }

    /**
     * Endpoint to get all registered admins (simple details).
     */
    @GetMapping("/")
    public ResponseEntity<List<TnPAdminResponse>> getAllAdmins() {
        log.info("Fetching all admins (simple details)");

        // --- LOGIC MOVED FROM SERVICE ---
        List<TnPAdmin> admins = tnpAdminRepository.findAll();

        List<TnPAdminResponse> responses = admins.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    // =================================================================================
    // NEW "FULL DETAILS" ENDPOINT (Your Request)
    // =================================================================================
    @GetMapping("/{id}/full-details")
    public ResponseEntity<TnPAdminFullDetailsResponse> getAdminFullDetails(@PathVariable Integer id) {
        log.info("Fetching FULL details for admin ID: {}", id);
        TnPAdmin admin = tnpAdminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + id));

        // 1. Create the main response object
        TnPAdminFullDetailsResponse response = new TnPAdminFullDetailsResponse();

        // 2. Copy simple fields from admin
        response.setId(admin.getId());
        response.setName(admin.getName());
        response.setEmail(admin.getEmail());
        response.setRole(admin.getRole());
        response.setDesignation(admin.getDesignation());

        // 3. Populate ALL nested lists
        response.setCreatedInternships(
                admin.getCreatedInternships().stream()
                        .map(this::convertInternshipToSummary)
                        .collect(Collectors.toList())
        );
        response.setCreatedNotifications(
                admin.getCreatedNotifications().stream()
                        .map(this::convertNotificationToSummary)
                        .collect(Collectors.toList())
        );
        response.setCreatedResources(
                admin.getCreatedResources().stream()
                        .map(this::convertResourceToSummary)
                        .collect(Collectors.toList())
        );
        response.setCreatedSessions(
                admin.getCreatedSessions().stream()
                        .map(this::convertSessionToSummary)
                        .collect(Collectors.toList())
        );
        response.setUploadedNotes(
                admin.getUploadedNotes().stream()
                        .map(this::convertNoteToSummary)
                        .collect(Collectors.toList())
        );
        response.setCreatedContests(
                admin.getCreatedContests().stream()
                        .map(this::convertContestToSummary)
                        .collect(Collectors.toList())
        );

        return ResponseEntity.ok(response);
    }

    // =================================================================================
    // HELPER METHODS (Moved from Service)
    // =================================================================================

    // Simple, non-secure hashing function
    private String simpleHash(String password) {
        if (password == null || password.isEmpty()) {
            return null;
        }
        return new StringBuilder(password).reverse().toString() + ".TnP_Salt";
    }

    // Helper to convert to the SIMPLE response
    private TnPAdminResponse convertToResponse(TnPAdmin admin) {
        TnPAdminResponse response = new TnPAdminResponse();
        response.setId(admin.getId());
        response.setName(admin.getName());
        response.setEmail(admin.getEmail());
        response.setRole(admin.getRole());
        response.setDesignation(admin.getDesignation());
        return response;
    }

    // --- NEW HELPER METHODS FOR SUMMARY CLASSES ---
    private InternshipSummary convertInternshipToSummary(Internship internship) {
        InternshipSummary summary = new InternshipSummary();
        summary.setId(internship.getId());
        summary.setRole(internship.getRole());
        summary.setCompany(internship.getCompany());
        return summary;
    }

    private NotificationSummary convertNotificationToSummary(Notification notification) {
        NotificationSummary summary = new NotificationSummary();
        summary.setId(notification.getId());
        summary.setTitle(notification.getTitle());
        summary.setCategory(notification.getCategory());
        return summary;
    }

    private ResourceSummary convertResourceToSummary(Resource resource) {
        ResourceSummary summary = new ResourceSummary();
        summary.setId(resource.getId());
        summary.setTitle(resource.getTitle());
        summary.setType(resource.getType());
        return summary;
    }

    private SessionSummary convertSessionToSummary(Session session) {
        SessionSummary summary = new SessionSummary();
        summary.setId(session.getId());
        summary.setTitle(session.getTitle());
        summary.setSpeaker(session.getSpeaker());
        summary.setSessionDatetime(session.getSessionDatetime());
        return summary;
    }

    private NoteSummary convertNoteToSummary(Notes note) { // Assuming 'Note'
        NoteSummary summary = new NoteSummary();
        summary.setId(note.getId());
        summary.setTitle(note.getTitle());
        summary.setTargetBranch(note.getTargetBranch());
        summary.setTargetYear(note.getTargetYear());
        return summary;
    }

    private ContestSummary convertContestToSummary(Contest contest) {
        ContestSummary summary = new ContestSummary();
        summary.setId(contest.getId());
        summary.setTitle(contest.getTitle());
        summary.setPlatform(contest.getPlatform());
        summary.setStartDatetime(contest.getStartDatetime());
        return summary;
    }
}
