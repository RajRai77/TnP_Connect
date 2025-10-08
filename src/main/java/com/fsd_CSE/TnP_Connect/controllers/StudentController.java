package com.fsd_CSE.TnP_Connect.controllers;
import com.fsd_CSE.TnP_Connect.DTO.StudentRequest;
import com.fsd_CSE.TnP_Connect.DTO.StudentResponse;
import com.fsd_CSE.TnP_Connect.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

//  StudentController.java (The API Endpoint Layer)


@RestController
@RequestMapping("/api/students") // Base URL for all student-related APIs
public class StudentController {

    @Autowired
    private StudentService studentService;

    // Endpoint: POST /api/students
    // Creates a new student
    @PostMapping("/")
    public ResponseEntity<StudentResponse> createStudent(@RequestBody StudentRequest studentRequest) {
        StudentResponse createdStudent = studentService.createStudent(studentRequest);
        return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
    }

    // Endpoint: GET /api/students
    // Gets all students
    @GetMapping("/")
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        List<StudentResponse> students = studentService.getAllStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    // Endpoint: GET /api/students/{id}
    // Gets a single student by their ID
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Integer id) {
        StudentResponse student = studentService.getStudentById(id);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }



    // Endpoint: PUT /api/students/{id}
    // Updates an existing student
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(@PathVariable Integer id, @RequestBody StudentRequest studentRequest) {
        StudentResponse updatedStudent = studentService.updateStudent(id, studentRequest);
        return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
    }

    // Endpoint: PATCH /api/students/{id}
    // Partially updates a student resource
    @PatchMapping("/{id}")
    public ResponseEntity<StudentResponse> patchStudent(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
        StudentResponse patchedStudent = studentService.patchStudent(id, updates);
        return new ResponseEntity<>(patchedStudent, HttpStatus.OK);
    }


    // Endpoint: DELETE /api/students/{id}
    // Deletes a student
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Integer id) {
        studentService.deleteStudent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content is standard for successful deletes
    }

    // Endpoint: GET /api/students/{id}/skills
    // Gets a student's skills
    @GetMapping("/{id}/skills")
    public ResponseEntity<Map<String, String>> getStudentSkills(@PathVariable Integer id) {
        Map<String, String> skills = studentService.getStudentSkills(id);
        return new ResponseEntity<>(skills, HttpStatus.OK);
    }

    // Endpoint: GET /api/students/search?branch=CSE&cgpa=8.0
    // Searches/filters students
    @GetMapping("/search")
    public ResponseEntity<List<StudentResponse>> searchStudents(
            @RequestParam(required = false) String branch,
            @RequestParam(required = false) BigDecimal cgpa) {
        List<StudentResponse> students = studentService.searchStudents(branch, cgpa);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }


}
