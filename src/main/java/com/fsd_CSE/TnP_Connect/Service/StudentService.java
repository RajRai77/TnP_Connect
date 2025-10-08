package com.fsd_CSE.TnP_Connect.Service;

import com.fsd_CSE.TnP_Connect.DTO.StudentRequest;
import com.fsd_CSE.TnP_Connect.DTO.StudentResponse;
import com.fsd_CSE.TnP_Connect.Enitities.Student;
import com.fsd_CSE.TnP_Connect.Repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentService {

    //Logger
    private static final Logger log = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    private StudentRepository studentRepository;

    // We will inject a password encoder later for security
    // @Autowired
    // private PasswordEncoder passwordEncoder;

    /**
     * Creates a new student in the database.
     * @param studentRequest Data for the new student.
     * @return The created student's data.
     */
    public StudentResponse createStudent(StudentRequest studentRequest) {
        // Check if email already exists
        if (studentRepository.findByEmail(studentRequest.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already in use.");
        }

        Student newStudent = new Student();
        newStudent.setName(studentRequest.getName());
        newStudent.setEmail(studentRequest.getEmail());
        newStudent.setBranch(studentRequest.getBranch());
        newStudent.setYear(studentRequest.getYear());
        newStudent.setCgpa(studentRequest.getCgpa());
        newStudent.setSkills(studentRequest.getSkills());
        newStudent.setProfilePicUrl(studentRequest.getProfilePicUrl());

        // IMPORTANT: Never store plain text passwords. We'll hash them later.
        // For now, we'll store it as is, but add a HASHED_ prefix to remember.
        // String hashedPassword = passwordEncoder.encode(studentRequest.getPassword());
        newStudent.setPasswordHash("HASHED_" + studentRequest.getPassword());

        Student savedStudent = studentRepository.save(newStudent);
        log.info("Created new student with ID: {} and email: {}", savedStudent.getId(), savedStudent.getEmail());

        return convertToResponse(savedStudent);
    }

    /**
     * Retrieves all students from the database.
     * @return A list of all students.
     */
    public List<StudentResponse> getAllStudents() {
        log.info("Fetching all students.");
        return studentRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a single student by their ID.
     * @param id The ID of the student.
     * @return The student's data.
     */
    public StudentResponse getStudentById(Integer id) {
        log.info("Fetching student with ID: {}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found with id: " + id));
        return convertToResponse(student);
    }

    /**
     * Updates an existing student's information.
     * @param id The ID of the student to update.
     * @param studentRequest The new data for the student.
     * @return The updated student's data.
     */
    public StudentResponse updateStudent(Integer id, StudentRequest studentRequest) {
        log.info("Updating student with ID: {}", id);
        Student studentToUpdate = studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found with id: " + id));

        // Update fields
        studentToUpdate.setName(studentRequest.getName());
        studentToUpdate.setEmail(studentRequest.getEmail());
        studentToUpdate.setBranch(studentRequest.getBranch());
        studentToUpdate.setYear(studentRequest.getYear());
        studentToUpdate.setCgpa(studentRequest.getCgpa());
        studentToUpdate.setSkills(studentRequest.getSkills());
        studentToUpdate.setProfilePicUrl(studentRequest.getProfilePicUrl());

        // Password update should typically be a separate, more secure process
        if (studentRequest.getPassword() != null && !studentRequest.getPassword().isEmpty()) {
            studentToUpdate.setPasswordHash("HASHED_" + studentRequest.getPassword());
        }

        Student updatedStudent = studentRepository.save(studentToUpdate);
        log.info("Successfully updated student with ID: {}", id);
        return convertToResponse(updatedStudent);
    }

    /**
     * Deletes a student from the database.
     * @param id The ID of the student to delete.
     */
    public void deleteStudent(Integer id) {
        log.warn("Attempting to delete student with ID: {}", id);
        if (!studentRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
        log.info("Successfully deleted student with ID: {}", id);
    }

    /**
     * Retrieves just the skills for a specific student.
     * @param id The ID of the student.
     * @return A map containing the student's skills.
     */
    public Map<String, String> getStudentSkills(Integer id) {
        log.info("Fetching skills for student with ID: {}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found with id: " + id));
        return Map.of("skills", student.getSkills());
    }

    /**
     * Searches for students based on optional filter criteria.
     * @param branch The branch to filter by (optional).
     * @param cgpa The minimum CGPA to filter by (optional).
     * @return A list of matching students.
     */
    public List<StudentResponse> searchStudents(String branch, BigDecimal cgpa) {
        log.info("Searching students with branch: {} and minimum CGPA: {}", branch, cgpa);
        List<Student> allStudents = studentRepository.findAll();

        // This is a simple in-memory filter.
        // In future for large datasets, a repository query is better.
        return allStudents.stream()
                .filter(student -> branch == null || student.getBranch().equalsIgnoreCase(branch))
                .filter(student -> cgpa == null || student.getCgpa().compareTo(cgpa) >= 0)
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    // Helper method to convert a Student Entity to a StudentResponse DTO
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
}

