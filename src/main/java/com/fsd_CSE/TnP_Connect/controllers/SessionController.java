package com.fsd_CSE.TnP_Connect.controllers;
import com.fsd_CSE.TnP_Connect.DTO.SessionRequest;
import com.fsd_CSE.TnP_Connect.DTO.SessionResponse;
import com.fsd_CSE.TnP_Connect.DTO.StudentRegistrationSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fsd_CSE.TnP_Connect.Enitities.*; // Importing all entities
import com.fsd_CSE.TnP_Connect.ExceptionHandling.ResourceNotFoundException;
import com.fsd_CSE.TnP_Connect.Repository.SessionRepository;
import com.fsd_CSE.TnP_Connect.Repository.TnPAdminRepository; // Needed for create logic

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/sessions")
public class SessionController {
    // --- DEPENDENCIES ---
    @Autowired private SessionRepository sessionRepository;
    @Autowired private TnPAdminRepository tnpAdminRepository;

    // --- LOGGER ---
    private static final Logger log = LoggerFactory.getLogger(SessionController.class);

    // --- ENDPOINT 1: Create Session ---
    @PostMapping("/")
    public ResponseEntity<SessionResponse> createSession(@RequestBody SessionRequest request) {
        log.info("Attempting to create session by admin ID: {}", request.getCreatedByAdminId());

        // --- LOGIC MOVED FROM SERVICE ---
        TnPAdmin admin = tnpAdminRepository.findById(request.getCreatedByAdminId())
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with ID: " + request.getCreatedByAdminId()));

        Session session = new Session();
        session.setTitle(request.getTitle());
        session.setDescription(request.getDescription());
        session.setSpeaker(request.getSpeaker());
        session.setTargetBranch(request.getTargetBranch());
        session.setTargetYear(request.getTargetYear());
        session.setSessionDatetime(request.getSessionDatetime());
        session.setJoinUrl(request.getJoinUrl());
        session.setCreatedByAdmin(admin);

        Session savedSession = sessionRepository.save(session);
        log.info("Successfully created session with ID: {}", savedSession.getId());

        // Convert to safe response
        return new ResponseEntity<>(convertToResponse(savedSession), HttpStatus.CREATED);
    }

    // --- ENDPOINT 2: Get All Sessions (Simple Details) ---
    @GetMapping("/")
    public ResponseEntity<List<SessionResponse>> getAllSessions() {
        log.info("Fetching all sessions");

        // --- LOGIC MOVED FROM SERVICE ---
        List<Session> sessions = sessionRepository.findAll();

        List<SessionResponse> responses = sessions.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    // --- NEW ENDPOINT 3: Get Session by ID (Simple Details) ---
    @GetMapping("/{id}")
    public ResponseEntity<SessionResponse> getSessionById(@PathVariable Integer id) {
        log.info("Fetching session with ID: {}", id);
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with ID: " + id));
        return ResponseEntity.ok(convertToResponse(session));
    }

    // --- NEW ENDPOINT 4: Delete Session ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Integer id) {
        log.warn("Attempting to delete session with ID: {}", id);
        if (!sessionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Session not found with ID: " + id);
        }
        sessionRepository.deleteById(id);
        log.info("Successfully deleted session with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    // =================================================================================
    // NEW RELATIONSHIP ENDPOINT
    // =================================================================================

    // --- NEW ENDPOINT 5: Get all registrations for a specific session ---
    @GetMapping("/{id}/registrations")
    public ResponseEntity<List<StudentRegistrationSummary>> getSessionRegistrations(@PathVariable Integer id) {
        log.info("Fetching registrations for session ID: {}", id);
        // 1. Find the session
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with ID: " + id));

        // 2. Get the list of registrations from the entity
        List<SessionRegistration> registrations = session.getRegistrations();

        // 3. Convert to the summary class
        List<StudentRegistrationSummary> summaries = registrations.stream()
                .map(this::convertRegToStudentSummary)
                .collect(Collectors.toList());

        return ResponseEntity.ok(summaries);
    }


    // =================================================================================
    // HELPER METHODS (Moved from Service)
    // =================================================================================

    // Helper to convert Entity to SIMPLE Response
    private SessionResponse convertToResponse(Session session) {
        SessionResponse response = new SessionResponse();
        response.setId(session.getId());
        response.setTitle(session.getTitle());
        response.setDescription(session.getDescription());
        response.setSpeaker(session.getSpeaker());
        response.setTargetBranch(session.getTargetBranch());
        response.setTargetYear(session.getTargetYear());
        response.setSessionDatetime(session.getSessionDatetime());
        response.setJoinUrl(session.getJoinUrl());
        if (session.getCreatedByAdmin() != null) {
            response.setCreatedByAdminName(session.getCreatedByAdmin().getName());
        }
        // Calculate registration count safely
        if (session.getRegistrations() != null) {
            response.setRegistrationCount(session.getRegistrations().size());
        } else {
            response.setRegistrationCount(0);
        }
        return response;
    }

    // NEW Helper to convert Registration to Student Summary for this endpoint
    private StudentRegistrationSummary convertRegToStudentSummary(SessionRegistration reg) {
        StudentRegistrationSummary summary = new StudentRegistrationSummary();
        summary.setRegistrationId(reg.getId());
        summary.setRegistrationStatus(reg.getStatus());
        summary.setRegisteredAt(reg.getRegisteredAt());
        if (reg.getStudent() != null) {
            summary.setStudentId(reg.getStudent().getId());
            summary.setStudentName(reg.getStudent().getName());
            summary.setStudentEmail(reg.getStudent().getEmail());
            summary.setStudentBranch(reg.getStudent().getBranch());
        }
        return summary;
    }
}