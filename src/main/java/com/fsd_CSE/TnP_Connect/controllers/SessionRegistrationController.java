package com.fsd_CSE.TnP_Connect.controllers;
import com.fsd_CSE.TnP_Connect.DTO.SessionRegistrationRequest;
import com.fsd_CSE.TnP_Connect.DTO.SessionRegistrationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fsd_CSE.TnP_Connect.Enitities.*; // Importing all entities
import com.fsd_CSE.TnP_Connect.ExceptionHandling.ResourceNotFoundException;
import com.fsd_CSE.TnP_Connect.Repository.SessionRegistrationRepository;
import com.fsd_CSE.TnP_Connect.Repository.SessionRepository;
import com.fsd_CSE.TnP_Connect.Repository.StudentRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/registrations")
public class SessionRegistrationController {
    // --- DEPENDENCIES ---
    @Autowired private SessionRegistrationRepository registrationRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private SessionRepository sessionRepository;

    // --- LOGGER ---
    private static final Logger log = LoggerFactory.getLogger(SessionRegistrationController.class);

    // --- ENDPOINT 1: Register for Session ---
    @PostMapping("/")
    public ResponseEntity<SessionRegistrationResponse> registerForSession(@RequestBody SessionRegistrationRequest request) {
        log.info("Student ID: {} is attempting to register for Session ID: {}", request.getStudentId(), request.getSessionId());

        // --- LOGIC MOVED FROM SERVICE ---

        // Check if student is already registered
        registrationRepository.findByStudentIdAndSessionId(request.getStudentId(), request.getSessionId())
                .ifPresent(reg -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Student is already registered for this session.");
                });

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + request.getStudentId()));

        Session session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with ID: " + request.getSessionId()));

        SessionRegistration newRegistration = new SessionRegistration();
        newRegistration.setStudent(student);
        newRegistration.setSession(session);
        newRegistration.setStatus("REGISTERED"); // Default status

        SessionRegistration savedRegistration = registrationRepository.save(newRegistration);
        log.info("Successfully created registration with ID: {}", savedRegistration.getId());

        // Convert to safe response
        return new ResponseEntity<>(convertToResponse(savedRegistration), HttpStatus.CREATED);
    }

    // --- NEW ENDPOINT 2: Get All Registrations (Admin View) ---
    @GetMapping("/")
    public ResponseEntity<List<SessionRegistrationResponse>> getAllRegistrations() {
        log.info("Fetching all session registrations");
        List<SessionRegistration> registrations = registrationRepository.findAll();
        List<SessionRegistrationResponse> responses = registrations.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    // --- NEW ENDPOINT 3: Get Registration by ID ---
    @GetMapping("/{id}")
    public ResponseEntity<SessionRegistrationResponse> getRegistrationById(@PathVariable Integer id) {
        log.info("Fetching registration with ID: {}", id);
        SessionRegistration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found with ID: " + id));
        return ResponseEntity.ok(convertToResponse(registration));
    }

    // --- NEW ENDPOINT 4: Update Registration Status (Admin Task) ---
    @PatchMapping("/{id}/status")
    public ResponseEntity<SessionRegistrationResponse> updateRegistrationStatus(
            @PathVariable Integer id,
            @RequestBody Map<String, String> statusUpdate) {

        String newStatus = statusUpdate.get("status");
        if (newStatus == null || newStatus.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status field is required.");
        }

        log.info("Updating status for registration ID: {} to {}", id, newStatus);

        SessionRegistration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found with ID: " + id));

        // Add validation for allowed status values if needed (e.g., REGISTERED, ATTENDED, CANCELLED)
        registration.setStatus(newStatus.toUpperCase()); // Store status consistently

        SessionRegistration updatedRegistration = registrationRepository.save(registration);
        log.info("Successfully updated status for registration ID: {}", id);

        return ResponseEntity.ok(convertToResponse(updatedRegistration));
    }

    // --- NEW ENDPOINT 5: Delete Registration (Withdraw / Admin Cleanup) ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegistration(@PathVariable Integer id) {
        log.warn("Attempting to delete registration with ID: {}", id);
        if (!registrationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Registration not found with ID: " + id);
        }
        registrationRepository.deleteById(id);
        log.info("Successfully deleted registration with ID: {}", id);
        return ResponseEntity.noContent().build();
    }


    // =================================================================================
    // HELPER METHOD (Moved from Service)
    // =================================================================================
    private SessionRegistrationResponse convertToResponse(SessionRegistration registration) {
        SessionRegistrationResponse response = new SessionRegistrationResponse();
        response.setRegistrationId(registration.getId());
        response.setStatus(registration.getStatus());
        response.setRegisteredAt(registration.getRegisteredAt());
        // Safely get related data
        if (registration.getStudent() != null) {
            response.setStudentName(registration.getStudent().getName());
        }
        if (registration.getSession() != null) {
            response.setSessionTitle(registration.getSession().getTitle());
        }
        return response;
    }
}