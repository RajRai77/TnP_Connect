package com.fsd_CSE.TnP_Connect.controllers;
import com.fsd_CSE.TnP_Connect.DTO.SessionRegistrationRequest;
import com.fsd_CSE.TnP_Connect.DTO.SessionRegistrationResponse;
import com.fsd_CSE.TnP_Connect.Service.SessionRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registrations")
public class SessionRegistrationController {
    @Autowired private SessionRegistrationService registrationService;

    @PostMapping("/")
    public ResponseEntity<SessionRegistrationResponse> registerForSession(@RequestBody SessionRegistrationRequest request) {
        return new ResponseEntity<>(registrationService.registerForSession(request), HttpStatus.CREATED);
    }
}