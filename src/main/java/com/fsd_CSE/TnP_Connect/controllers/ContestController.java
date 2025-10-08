package com.fsd_CSE.TnP_Connect.controllers;
import com.fsd_CSE.TnP_Connect.DTO.ContestRequest;
import com.fsd_CSE.TnP_Connect.DTO.ContestResponse;
import com.fsd_CSE.TnP_Connect.Service.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


// ContestController.java (API Endpoints)

@RestController
@RequestMapping("/api/contests")
class ContestController {

    @Autowired
    private ContestService contestService;

    @PostMapping("/")
    public ResponseEntity<ContestResponse> createContest(@RequestBody ContestRequest request) {
        ContestResponse response = contestService.createContest(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<ContestResponse>> getAllContests() {
        return ResponseEntity.ok(contestService.getAllContests());
    }

    // NOTE: A DELETE endpoint would be added here in a full application
}
