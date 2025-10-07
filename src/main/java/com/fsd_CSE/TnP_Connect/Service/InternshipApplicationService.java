package com.fsd_CSE.TnP_Connect.Service;
import com.fsd_CSE.TnP_Connect.DTO.InternshipApplicationRequest;
import com.fsd_CSE.TnP_Connect.DTO.InternshipApplicationResponse;
import com.fsd_CSE.TnP_Connect.Enitities.Internship;
import com.fsd_CSE.TnP_Connect.Enitities.InternshipApplication;
import com.fsd_CSE.TnP_Connect.Enitities.Student;
import com.fsd_CSE.TnP_Connect.Repository.InternshipApplicationRepository;
import com.fsd_CSE.TnP_Connect.Repository.InternshipRepository;
import com.fsd_CSE.TnP_Connect.Repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

// =====================================================================================
// InternshipApplicationService.java (Business logic for applying)
// =====================================================================================
@Service
public class InternshipApplicationService {
    private static final Logger log = LoggerFactory.getLogger(InternshipApplicationService.class);

    @Autowired private InternshipApplicationRepository applicationRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private InternshipRepository internshipRepository;

    public InternshipApplicationResponse applyForInternship(InternshipApplicationRequest request) {
        log.info("Student ID: {} is applying for Internship ID: {}", request.getStudentId(), request.getInternshipId());

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found with ID: " + request.getStudentId()));

        Internship internship = internshipRepository.findById(request.getInternshipId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Internship not found with ID: " + request.getInternshipId()));

        InternshipApplication application = new InternshipApplication();
        application.setStudent(student);
        application.setInternship(internship);
        application.setStatus("APPLIED"); // Default status

        InternshipApplication savedApplication = applicationRepository.save(application);
        log.info("Successfully created application with ID: {}", savedApplication.getId());
        return convertToResponse(savedApplication);
    }

    private InternshipApplicationResponse convertToResponse(InternshipApplication application) {
        InternshipApplicationResponse response = new InternshipApplicationResponse();
        response.setApplicationId(application.getId());
        response.setStatus(application.getStatus());
        response.setAppliedAt(application.getAppliedAt());
        response.setStudentName(application.getStudent().getName());
        response.setInternshipRole(application.getInternship().getRole());
        response.setInternshipCompany(application.getInternship().getCompany());
        return response;
    }
}
