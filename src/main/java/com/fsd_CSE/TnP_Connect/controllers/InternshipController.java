package com.fsd_CSE.TnP_Connect.controllers;
import com.fsd_CSE.TnP_Connect.DTO.InternshipRequest;
import com.fsd_CSE.TnP_Connect.DTO.InternshipResponse;
import com.fsd_CSE.TnP_Connect.Service.InternshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// =====================================================================================
// InternshipController.java (API endpoints for internships)
// =====================================================================================
@RestController
@RequestMapping("/api/internships")
public class InternshipController {
    @Autowired private InternshipService internshipService;

    @PostMapping("/")
    public ResponseEntity<InternshipResponse> createInternship(@RequestBody InternshipRequest request) {
        InternshipResponse response = internshipService.createInternship(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<InternshipResponse>> getAllInternships() {
        List<InternshipResponse> internships = internshipService.getAllInternships();
        return ResponseEntity.ok(internships);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInternship(@PathVariable Integer id) {
        internshipService.deleteInternship(id);
        return ResponseEntity.noContent().build();
    }
}