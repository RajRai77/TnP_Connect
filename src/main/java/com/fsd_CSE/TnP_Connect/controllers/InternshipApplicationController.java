package com.fsd_CSE.TnP_Connect.controllers;
import com.fsd_CSE.TnP_Connect.DTO.InternshipApplicationRequest;
import com.fsd_CSE.TnP_Connect.DTO.InternshipApplicationResponse;
import com.fsd_CSE.TnP_Connect.Service.InternshipApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


// InternshipApplicationController.java (API endpoint for applying)

@RestController
@RequestMapping("/api/applications")
public class InternshipApplicationController {
    @Autowired private InternshipApplicationService applicationService;

    @PostMapping("/")
    public ResponseEntity<InternshipApplicationResponse> applyForInternship(@RequestBody InternshipApplicationRequest request) {
        InternshipApplicationResponse response = applicationService.applyForInternship(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
