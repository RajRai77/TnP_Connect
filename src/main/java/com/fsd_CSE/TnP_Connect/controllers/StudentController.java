package com.fsd_CSE.TnP_Connect.controllers;
import com.fsd_CSE.TnP_Connect.DTO.InternshipApplicationSummary;
import com.fsd_CSE.TnP_Connect.DTO.SessionRegistrationSummary;
import com.fsd_CSE.TnP_Connect.DTO.StudentFullDetailsResponse;
import com.fsd_CSE.TnP_Connect.DTO.StudentResponse;
import com.fsd_CSE.TnP_Connect.Enitities.InternshipApplication;
import com.fsd_CSE.TnP_Connect.Enitities.SessionRegistration;
import com.fsd_CSE.TnP_Connect.Enitities.Student;
import com.fsd_CSE.TnP_Connect.ExceptionHandling.ResourceNotFoundException;
import com.fsd_CSE.TnP_Connect.Repository.StudentRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody; // Alias to avoid name clash


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//  StudentController.java (The API Endpoint Layer)


@RestController
@RequestMapping("/api/students") // Base URL for all student-related APIs
public class StudentController {


    @Autowired
    private StudentRepository studentRepository;

    private static final Logger log = LoggerFactory.getLogger(StudentController.class);


    // --- ENDPOINT 1: Create Student ---
    // NOW uses the new simpleHash() method
    @PostMapping("/")
    public ResponseEntity<StudentResponse> createStudent(@RequestBody Student requestStudent) {
        log.info("Creating new student with email: {}", requestStudent.getEmail());

        if (studentRepository.findByEmail(requestStudent.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already in use.");
        }

        Student newStudent = new Student();
        newStudent.setName(requestStudent.getName());
        newStudent.setEmail(requestStudent.getEmail());
        newStudent.setBranch(requestStudent.getBranch());
        newStudent.setYear(requestStudent.getYear());
        newStudent.setCgpa(requestStudent.getCgpa());
        newStudent.setSkills(requestStudent.getSkills());
        newStudent.setProfilePicUrl(requestStudent.getProfilePicUrl());
        newStudent.setTnprollNo(requestStudent.getTnprollNo());

        // --- NEW HASHING LOGIC ---
        // Assumes plain text password is sent in the 'passwordHash' field of the request
        newStudent.setPasswordHash(simpleHash(requestStudent.getPasswordHash()));

        Student savedStudent = studentRepository.save(newStudent);
        log.info("Created new student with ID: {}", savedStudent.getId());

        return new ResponseEntity<>(convertToResponse(savedStudent), HttpStatus.CREATED);
    }


    // --- ENDPOINT 2: Get Student By ID (Simple) ---
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Integer id) {
        log.info("Fetching student with ID: {}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        return ResponseEntity.ok(convertToResponse(student));
    }

    // --- All other endpoints (3-8) are unchanged ---
    // ... (getAllStudents, updateStudent, patchStudent, deleteStudent, getStudentSkills, searchStudents) ...

    // --- ENDPOINT 3: Get All Students ---
    @GetMapping("/")
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        log.info("Fetching all students");
        List<Student> students = studentRepository.findAll();
        List<StudentResponse> studentResponses = students.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(studentResponses);
    }

    // --- ENDPOINT 4: Update Student (Full Replace) ---
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(@PathVariable Integer id, @RequestBody Student requestStudent) {
        log.info("Updating (PUT) student with ID: {}", id);
        Student studentToUpdate = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        studentToUpdate.setName(requestStudent.getName());
        studentToUpdate.setEmail(requestStudent.getEmail());
        studentToUpdate.setBranch(requestStudent.getBranch());
        studentToUpdate.setYear(requestStudent.getYear());
        studentToUpdate.setCgpa(requestStudent.getCgpa());
        studentToUpdate.setSkills(requestStudent.getSkills());
        studentToUpdate.setProfilePicUrl(requestStudent.getProfilePicUrl());
        studentToUpdate.setTnprollNo(requestStudent.getTnprollNo());
        Student updatedStudent = studentRepository.save(studentToUpdate);
        log.info("Successfully updated (PUT) student with ID: {}", id);
        return ResponseEntity.ok(convertToResponse(updatedStudent));
    }

    // --- ENDPOINT 5: Patch Student (Partial Update) ---
    @PatchMapping("/{id}")
    public ResponseEntity<StudentResponse> patchStudent(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
        log.info("Partially updating (PATCH) student with ID: {}", id);
        Student studentToPatch = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        updates.forEach((key, value) -> {
            switch (key) {
                case "name": studentToPatch.setName((String) value); break;
                case "email": studentToPatch.setEmail((String) value); break;
                case "branch": studentToPatch.setBranch((String) value); break;
                case "year": studentToPatch.setYear((Integer) value); break;
                case "cgpa": studentToPatch.setCgpa(BigDecimal.valueOf(((Number) value).doubleValue())); break;
                case "skills": studentToPatch.setSkills((String) value); break;
                case "profilePicUrl": studentToPatch.setProfilePicUrl((String) value); break;
                case "tnprollNo": studentToPatch.setTnprollNo((String) value); break;
            }
        });
        Student updatedStudent = studentRepository.save(studentToPatch);
        log.info("Successfully patched student with ID: {}", id);
        return ResponseEntity.ok(convertToResponse(updatedStudent));
    }

    // --- ENDPOINT 6: Delete Student ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Integer id) {
        log.warn("Attempting to delete student with ID: {}", id);
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with ID: " + id);
        }
        studentRepository.deleteById(id);
        log.info("Successfully deleted student with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    // --- ENDPOINT 7: Get Student Skills ---
    @GetMapping("/{id}/skills")
    public ResponseEntity<Map<String, String>> getStudentSkills(@PathVariable Integer id) {
        log.info("Fetching skills for student with ID: {}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        return ResponseEntity.ok(Map.of("skills", student.getSkills()));
    }

    // --- ENDPOINT 8: Search Students ---
    @GetMapping("/search")
    public ResponseEntity<List<StudentResponse>> searchStudents(
            @RequestParam(required = false) String branch,
            @RequestParam(required = false) BigDecimal cgpa) {
        log.info("Searching students with branch: {} and minimum CGPA: {}", branch, cgpa);
        List<Student> allStudents = studentRepository.findAll();
        return ResponseEntity.ok(allStudents.stream()
                .filter(student -> branch == null || student.getBranch().equalsIgnoreCase(branch))
                .filter(student -> cgpa == null || student.getCgpa().compareTo(cgpa) >= 0)
                .map(this::convertToResponse)
                .collect(Collectors.toList()));
    }


    // =================================================================================
    // NEW RELATIONSHIP ENDPOINTS (As requested)
    // =================================================================================

    // --- ENDPOINT 9: Get all applications for a specific student ---
    @GetMapping("/{id}/internship-applications")
    public ResponseEntity<List<InternshipApplicationSummary>> getStudentApplications(@PathVariable Integer id) { // Renamed
        log.info("Fetching internship applications for student ID: {}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        List<InternshipApplication> applications = student.getInternshipApplications();

        List<InternshipApplicationSummary> summaries = applications.stream() // Renamed
                .map(this::convertAppToSummary) // Using helper
                .collect(Collectors.toList());

        return ResponseEntity.ok(summaries); // Renamed
    }

    // --- ENDPOINT 10: Get all session registrations for a specific student ---
    @GetMapping("/{id}/session-registrations")
    public ResponseEntity<List<SessionRegistrationSummary>> getStudentSessionRegistrations(@PathVariable Integer id) { // Renamed
        log.info("Fetching session registrations for student ID: {}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        List<SessionRegistration> registrations = student.getSessionRegistrations();

        List<SessionRegistrationSummary> summaries = registrations.stream() // Renamed
                .map(this::convertRegToSummary) // Using helper
                .collect(Collectors.toList());

        return ResponseEntity.ok(summaries); // Renamed
    }

    // --- NEW ENDPOINT 11: Get Full Student Details (Your Request) ---
    @GetMapping("/{id}/full-details")
    public ResponseEntity<StudentFullDetailsResponse> getStudentFullDetails(@PathVariable Integer id) {
        log.info("Fetching FULL details for student ID: {}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        // 1. Create the main response object
        StudentFullDetailsResponse response = new StudentFullDetailsResponse();

        // 2. Copy simple fields from student
        response.setId(student.getId());
        response.setName(student.getName());
        response.setEmail(student.getEmail());
        response.setBranch(student.getBranch());
        response.setYear(student.getYear());
        response.setCgpa(student.getCgpa());
        response.setSkills(student.getSkills());
        response.setProfilePicUrl(student.getProfilePicUrl());
        response.setTnprollNo(student.getTnprollNo());

        // 3. Populate internship applications
        List<InternshipApplicationSummary> appSummaries = student.getInternshipApplications().stream() // Renamed
                .map(this::convertAppToSummary) // Renamed
                .collect(Collectors.toList());
        response.setInternshipApplications(appSummaries);

        // 4. Populate session registrations
        List<SessionRegistrationSummary> regSummaries = student.getSessionRegistrations().stream() // Renamed
                .map(this::convertRegToSummary) // Renamed
                .collect(Collectors.toList());
        response.setSessionRegistrations(regSummaries);

        return ResponseEntity.ok(response);
    }

    // --- NEW ENDPOINT 12: Secure Password Change ---
    @PatchMapping("/{id}/change-password")
    public ResponseEntity<Map<String, String>> changePassword(
            @PathVariable Integer id,
            // Use the fully qualified name for Swagger's RequestBody annotation
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Requires old password, new password, and OTP.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "object", requiredProperties = {"oldPassword", "newPassword", "otp"}),
                            examples = {
                                    @ExampleObject(
                                            name = "Password Change Example",
                                            value = "{\"oldPassword\": \"currentSecret123\", \"newPassword\": \"newSecret456\", \"otp\": \"123456\"}"
                                    )
                            }
                    )
            )
            // Use Spring's RequestBody annotation for the actual parameter binding
            @org.springframework.web.bind.annotation.RequestBody Map<String, String> passwordRequest) {

        String oldPassword = passwordRequest.get("oldPassword");
        String newPassword = passwordRequest.get("newPassword");
        String otp = passwordRequest.get("otp");
        if (oldPassword == null || newPassword == null || otp == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body must contain oldPassword, newPassword, and otp.");
        }

        log.info("Attempting password change for student ID: {}", id);
        log.warn("OTP validation is currently skipped! (TODO)");

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        if (!student.getPasswordHash().equals(simpleHash(oldPassword))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Old password does not match.");
        }

        student.setPasswordHash(simpleHash(newPassword));
        studentRepository.save(student);

        log.info("Successfully changed password for student ID: {}", id);
        return ResponseEntity.ok(Map.of("message", "Password changed successfully."));
    }

//    // --- NEW ENDPOINT 13: Secure Email Change ---
//    @PatchMapping("/{id}/change-email")
//    public ResponseEntity<Map<String, String>> changeEmail(
//            @PathVariable Integer id,
//            // Use the fully qualified name for Swagger's RequestBody annotation
//            @io.swagger.v3.oas.annotations.parameters.RequestBody(
//                    description = "Requires the new email address and OTP.",
//                    required = true,
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(type = "object", requiredProperties = {"newEmail", "otp"}),
//                            examples = {
//                                    @ExampleObject(
//                                            name = "Email Change Example",
//                                            value = "{\"newEmail\": \"new.email@example.com\", \"otp\": \"654321\"}"
//                                    )
//                            }
//                    )
//            )
//            // Use Spring's RequestBody annotation for the actual parameter binding
//            @org.springframework.web.bind.annotation.RequestBody Map<String, String> emailRequest) {
//
//        String newEmail = emailRequest.get("newEmail");
//        String otp = emailRequest.get("otp");
//        if (newEmail == null || otp == null) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body must contain newEmail and otp.");
//        }
//
//        log.info("Attempting email change for student ID: {} to {}", id, newEmail);
//        log.warn("OTP validation is currently skipped! (TODO)");
//
//        if (studentRepository.findByEmail(newEmail).isPresent()) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "New email is already in use.");
//        }
//
//        Student student = studentRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id)); // Corrected exception variable
//
//        student.setEmail(newEmail);
//        studentRepository.save(student);
//
//        log.info("Successfully changed email for student ID: {}", id);
//        return ResponseEntity.ok(Map.of("message", "Email changed successfully."));
//    }







    // =================================================================================
    // HELPER METHODS (Moved from Service)
    // =================================================================================

    // Your simple, non-secure hashing function as requested
    private String simpleHash(String password) {
        if (password == null || password.isEmpty()) {
            return null;
        }
        // Example: reverse the string and add a simple "salt"
        // THIS IS NOT SECURE, for demonstration only.
        return new StringBuilder(password).reverse().toString() + ".TnP_Salt";
    }

    private StudentResponse convertToResponse(Student student) {
        StudentResponse response = new StudentResponse();
        response.setId(student.getId());
        response.setName(student.getName());
        response.setEmail(student.getEmail());
        response.setBranch(student.getBranch());
        response.setYear(student.getYear());
        response.setCgpa(student.getCgpa());
        response.setSkills(student.getSkills());
        response.setProfilePicUrl(student.getProfilePicUrl());
        return response;
    }

    // New helper for converting applications
    private InternshipApplicationSummary convertAppToSummary(InternshipApplication app) { // Renamed
        InternshipApplicationSummary summary = new InternshipApplicationSummary(); // Renamed
        summary.setApplicationId(app.getId());
        summary.setStatus(app.getStatus());
        summary.setAppliedAt(app.getAppliedAt());
        if (app.getInternship() != null) {
            summary.setInternshipRole(app.getInternship().getRole());
            summary.setInternshipCompany(app.getInternship().getCompany());
        }
        return summary;
    }

    // New helper for converting registrations
    private SessionRegistrationSummary convertRegToSummary(SessionRegistration reg) { // Renamed
        SessionRegistrationSummary summary = new SessionRegistrationSummary(); // Renamed
        summary.setRegistrationId(reg.getId());
        summary.setStatus(reg.getStatus());
        summary.setRegisteredAt(reg.getRegisteredAt());
        if (reg.getSession() != null) {
            summary.setSessionTitle(reg.getSession().getTitle());
            summary.setSessionDatetime(reg.getSession().getSessionDatetime());
        }
        return summary;
    }




}
