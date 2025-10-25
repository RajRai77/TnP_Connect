package com.fsd_CSE.TnP_Connect.controllers;
import com.fsd_CSE.TnP_Connect.DTO.NotificationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.fsd_CSE.TnP_Connect.Enitities.*; // Importing all entities
import com.fsd_CSE.TnP_Connect.ExceptionHandling.ResourceNotFoundException;
import com.fsd_CSE.TnP_Connect.Repository.NotificationRepository;
import com.fsd_CSE.TnP_Connect.Repository.TnPAdminRepository; // Needed for create logic

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException; // Added back temporarily for BAD_REQUEST

import java.util.stream.Collectors;

// NotificationController.java (API Endpoints)

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    // --- DEPENDENCIES ---
    @Autowired private NotificationRepository notificationRepository;
    @Autowired private TnPAdminRepository tnpAdminRepository;

    // --- LOGGER ---
    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

    // --- ENDPOINT 1: Create Notification ---
    // NOW ACCEPTS THE Notification ENTITY DIRECTLY
    @PostMapping("/")
    public ResponseEntity<NotificationResponse> createNotification(@RequestBody Notification requestNotification) {

        // --- LOGIC MOVED FROM SERVICE & ADAPTED ---
        // Get admin ID from nested object
        Integer adminId;
        if (requestNotification.getPostedByAdmin() != null && requestNotification.getPostedByAdmin().getId() != null) {
            adminId = requestNotification.getPostedByAdmin().getId();
        } else {
            adminId = null;
            // Use ResponseStatusException for simple Bad Request errors
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "postedByAdmin object with id is required.");
        }

        log.info("Attempting to create notification by admin ID: {}", adminId);

        TnPAdmin admin = tnpAdminRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with ID: " + adminId));

        // Create a new entity to save, copying necessary fields
        Notification notification = new Notification();
        notification.setTitle(requestNotification.getTitle());
        notification.setContent(requestNotification.getContent());
        notification.setTargetBranch(requestNotification.getTargetBranch());
        notification.setTargetYear(requestNotification.getTargetYear());
        notification.setLink(requestNotification.getLink());
        notification.setEventDate(requestNotification.getEventDate());
        notification.setCategory(requestNotification.getCategory());
        notification.setPostedByAdmin(admin); // Set relationship using fetched admin

        Notification savedNotification = notificationRepository.save(notification);
        log.info("Admin ID {} created new notification with ID {}", admin.getId(), savedNotification.getId());

        // Convert to safe response
        return new ResponseEntity<>(convertToResponse(savedNotification), HttpStatus.CREATED);
    }

    // --- ENDPOINT 2: Get All Notifications (with Filtering) ---
    @GetMapping("/")
    public ResponseEntity<List<NotificationResponse>> getAllNotifications(
            @RequestParam(required = false) String branch,
            @RequestParam(required = false) Integer year) {
        log.info("Fetching notifications. Filter by branch: {}, year: {}", branch, year);

        // --- LOGIC MOVED FROM SERVICE ---
        List<Notification> allNotifications = notificationRepository.findAll();

        List<NotificationResponse> responses = allNotifications.stream()
                .filter(n -> branch == null || "ALL".equalsIgnoreCase(n.getTargetBranch()) || branch.equalsIgnoreCase(n.getTargetBranch()))
                .filter(n -> year == null || n.getTargetYear() == null || n.getTargetYear() == 0 || year.equals(n.getTargetYear()))
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    // --- ENDPOINT 3: Get Notification by ID ---
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getNotificationById(@PathVariable Integer id) {
        log.info("Fetching notification with ID: {}", id);
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with ID: " + id));
        return ResponseEntity.ok(convertToResponse(notification));
    }

    // --- ENDPOINT 4: Delete Notification ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Integer id) {
        log.warn("Attempting to delete notification with ID: {}", id);

        // --- LOGIC MOVED FROM SERVICE ---
        if (!notificationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Notification not found with ID: " + id);
        }
        notificationRepository.deleteById(id);
        log.info("Successfully deleted notification with ID: {}", id);

        return ResponseEntity.noContent().build();
    }


    // =================================================================================
    // HELPER METHOD (Moved from Service)
    // =================================================================================
    private NotificationResponse convertToResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setTitle(notification.getTitle());
        response.setContent(notification.getContent());
        response.setTargetBranch(notification.getTargetBranch());
        response.setTargetYear(notification.getTargetYear());
        response.setLink(notification.getLink());
        response.setEventDate(notification.getEventDate());
        response.setCategory(notification.getCategory());
        response.setCreatedAt(notification.getCreatedAt());
        if (notification.getPostedByAdmin() != null) {
            response.setPostedByAdminName(notification.getPostedByAdmin().getName());
        }
        return response;
    }
}
