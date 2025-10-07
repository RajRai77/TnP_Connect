package com.fsd_CSE.TnP_Connect.Service;
import com.fsd_CSE.TnP_Connect.DTO.NotificationRequest;
import com.fsd_CSE.TnP_Connect.DTO.NotificationResponse;
import com.fsd_CSE.TnP_Connect.Enitities.Notification;
import com.fsd_CSE.TnP_Connect.Enitities.TnPAdmin;
import com.fsd_CSE.TnP_Connect.ExceptionHandling.ResourceNotFoundException;
import com.fsd_CSE.TnP_Connect.Repository.NotificationRepository;
import com.fsd_CSE.TnP_Connect.Repository.TnPAdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


// FILE 3: NotificationService.java (Business Logic)
// =====================================================================================
@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @Autowired private NotificationRepository notificationRepository;
    @Autowired private TnPAdminRepository tnpAdminRepository;

    public NotificationResponse createNotification(NotificationRequest request) {
        TnPAdmin admin = tnpAdminRepository.findById(request.getPostedByAdminId())
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with ID: " + request.getPostedByAdminId()));

        Notification notification = new Notification();
        notification.setTitle(request.getTitle());
        notification.setContent(request.getContent());
        notification.setTargetBranch(request.getTargetBranch());
        notification.setTargetYear(request.getTargetYear());
        notification.setLink(request.getLink());
        notification.setEventDate(request.getEventDate());
        notification.setCategory(request.getCategory());
        notification.setPostedByAdmin(admin);

        Notification savedNotification = notificationRepository.save(notification);
        log.info("Admin ID {} created new notification with ID {}", admin.getId(), savedNotification.getId());
        return convertToResponse(savedNotification);
    }

    public List<NotificationResponse> getAllNotifications(String branch, Integer year) {
        log.info("Fetching notifications. Filter by branch: {}, year: {}", branch, year);
        List<Notification> allNotifications = notificationRepository.findAll();

        // In-memory filtering. For huge datasets, a database query would be more efficient.
        return allNotifications.stream()
                .filter(n -> branch == null || "ALL".equalsIgnoreCase(n.getTargetBranch()) || branch.equalsIgnoreCase(n.getTargetBranch()))
                .filter(n -> year == null || n.getTargetYear() == 0 || year.equals(n.getTargetYear()))
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public void deleteNotification(Integer id) {
        log.warn("Attempting to delete notification with ID: {}", id);
        if (!notificationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Notification not found with ID: " + id);
        }
        notificationRepository.deleteById(id);
        log.info("Successfully deleted notification with ID: {}", id);
    }

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
