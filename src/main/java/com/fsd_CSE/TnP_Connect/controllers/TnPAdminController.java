package com.fsd_CSE.TnP_Connect.controllers;
import com.fsd_CSE.TnP_Connect.DTO.LoginRequest;
import com.fsd_CSE.TnP_Connect.DTO.TnPAdminRequest;
import com.fsd_CSE.TnP_Connect.DTO.TnPAdminResponse;
import com.fsd_CSE.TnP_Connect.Service.TnPAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/admins")
public class TnPAdminController {

    @Autowired
    private TnPAdminService tnpAdminService;

    /**
     * Endpoint to register a new admin account.
     */
    @PostMapping("/register")
    public ResponseEntity<TnPAdminResponse> registerAdmin(@RequestBody TnPAdminRequest request) {
        TnPAdminResponse newAdmin = tnpAdminService.registerAdmin(request);
        return new ResponseEntity<>(newAdmin, HttpStatus.CREATED);
    }

    /**
     * Endpoint for an admin to log in.
     */
    @PostMapping("/login")
    public ResponseEntity<TnPAdminResponse> loginAdmin(@RequestBody LoginRequest request) {
        TnPAdminResponse admin = tnpAdminService.loginAdmin(request);
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }

    /**
     * Endpoint to get all registered admins.
     */
    @GetMapping("/")
    public ResponseEntity<List<TnPAdminResponse>> getAllAdmins() {
        List<TnPAdminResponse> admins = tnpAdminService.getAllAdmins();
        return ResponseEntity.ok(admins);
    }
}
