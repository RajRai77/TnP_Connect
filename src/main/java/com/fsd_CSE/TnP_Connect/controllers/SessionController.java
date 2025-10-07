package com.fsd_CSE.TnP_Connect.controllers;
import com.fsd_CSE.TnP_Connect.DTO.SessionRequest;
import com.fsd_CSE.TnP_Connect.DTO.SessionResponse;
import com.fsd_CSE.TnP_Connect.Service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/sessions")
public class SessionController {
    @Autowired private SessionService sessionService;

    @PostMapping("/")
    public ResponseEntity<SessionResponse> createSession(@RequestBody SessionRequest request) {
        return new ResponseEntity<>(sessionService.createSession(request), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<SessionResponse>> getAllSessions() {
        return ResponseEntity.ok(sessionService.getAllSessions());
    }
}