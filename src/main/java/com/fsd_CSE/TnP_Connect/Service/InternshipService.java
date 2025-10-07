package com.fsd_CSE.TnP_Connect.Service;
import com.fsd_CSE.TnP_Connect.DTO.InternshipRequest;
import com.fsd_CSE.TnP_Connect.DTO.InternshipResponse;
import com.fsd_CSE.TnP_Connect.Enitities.Internship;
import com.fsd_CSE.TnP_Connect.Enitities.TnPAdmin;
import com.fsd_CSE.TnP_Connect.Repository.InternshipRepository;
import com.fsd_CSE.TnP_Connect.Repository.TnPAdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


// =====================================================================================
// InternshipService.java (Business logic for internships)
// =====================================================================================
@Service
public class InternshipService {
    private static final Logger log = LoggerFactory.getLogger(InternshipService.class);

    @Autowired private InternshipRepository internshipRepository;
    @Autowired private TnPAdminRepository tnpAdminRepository;

    public InternshipResponse createInternship(InternshipRequest request) {
        log.info("Attempting to create internship posted by admin ID: {}", request.getCreatedByAdminId());

        TnPAdmin admin = tnpAdminRepository.findById(request.getCreatedByAdminId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin not found with ID: " + request.getCreatedByAdminId()));

        Internship internship = new Internship();
        internship.setRole(request.getRole());
        internship.setCompany(request.getCompany());
        internship.setStipend(request.getStipend());
        internship.setEligibility(request.getEligibility());
        internship.setDeadline(request.getDeadline());
        internship.setDescription(request.getDescription());
        internship.setStatus(request.getStatus());
        internship.setCreatedByAdmin(admin); // Set the relationship

        Internship savedInternship = internshipRepository.save(internship);
        log.info("Successfully created internship with ID: {}", savedInternship.getId());
        return convertToResponse(savedInternship);
    }

    public List<InternshipResponse> getAllInternships() {
        return internshipRepository.findAll().stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public void deleteInternship(Integer id) {
        if(!internshipRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Internship not found with ID: " + id);
        }
        internshipRepository.deleteById(id);
        log.info("Deleted internship with ID: {}", id);
    }

    private InternshipResponse convertToResponse(Internship internship) {
        InternshipResponse response = new InternshipResponse();
        response.setId(internship.getId());
        response.setRole(internship.getRole());
        response.setCompany(internship.getCompany());
        //... set all other fields
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
}
