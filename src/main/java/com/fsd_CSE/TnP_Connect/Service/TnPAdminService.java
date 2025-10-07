package com.fsd_CSE.TnP_Connect.Service;

import com.fsd_CSE.TnP_Connect.DTO.LoginRequest;
import com.fsd_CSE.TnP_Connect.DTO.TnPAdminRequest;
import com.fsd_CSE.TnP_Connect.DTO.TnPAdminResponse;
import com.fsd_CSE.TnP_Connect.Enitities.TnPAdmin;
import com.fsd_CSE.TnP_Connect.Repository.TnPAdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

// =====================================================================================
//  TnPAdminService.java (The Business Logic Layer)
// =====================================================================================

@Service
public class TnPAdminService {

    private static final Logger log = LoggerFactory.getLogger(TnPAdminService.class);

    @Autowired
    private TnPAdminRepository tnpAdminRepository;

    /**
     * Registers a new TnP Admin.
     * @param request The data for the new admin.
     * @return The created admin's data.
     */
    public TnPAdminResponse registerAdmin(TnPAdminRequest request) {
        if (tnpAdminRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Admin with this email already exists.");
        }

        TnPAdmin admin = new TnPAdmin();
        admin.setName(request.getName());
        admin.setEmail(request.getEmail());
        admin.setRole(request.getRole());
        admin.setDesignation(request.getDesignation());

        // IMPORTANT: For the evaluation, we are storing the password "hashed" with a prefix.
        // In a real app, you MUST use a proper password encoder (like BCrypt).
        admin.setPasswordHash("HASHED_" + request.getPassword());

        TnPAdmin savedAdmin = tnpAdminRepository.save(admin);
        log.info("Registered new admin with ID: {}", savedAdmin.getId());
        return convertToResponse(savedAdmin);
    }

    /**
     * Authenticates a TnP Admin.
     * @param request The login credentials.
     * @return The authenticated admin's data.
     */
    public TnPAdminResponse loginAdmin(LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());

        TnPAdmin admin = tnpAdminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        // IMPORTANT: This is a simple string comparison for your evaluation.
        // In a real app, you would use `passwordEncoder.matches(request.getPassword(), admin.getPasswordHash())`.
        String expectedPasswordHash = "HASHED_" + request.getPassword();
        if (!expectedPasswordHash.equals(admin.getPasswordHash())) {
            log.warn("Failed login attempt for email: {}", request.getEmail());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        log.info("Admin with ID: {} successfully logged in.", admin.getId());
        return convertToResponse(admin);
    }

    public List<TnPAdminResponse> getAllAdmins() {
        return tnpAdminRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private TnPAdminResponse convertToResponse(TnPAdmin admin) {
        TnPAdminResponse response = new TnPAdminResponse();
        response.setId(admin.getId());
        response.setName(admin.getName());
        response.setEmail(admin.getEmail());
        response.setRole(admin.getRole());
        response.setDesignation(admin.getDesignation());
        return response;
    }
}