package com.fsd_CSE.TnP_Connect.Service;
import com.fsd_CSE.TnP_Connect.DTO.SessionRegistrationRequest;
import com.fsd_CSE.TnP_Connect.DTO.SessionRegistrationResponse;
import com.fsd_CSE.TnP_Connect.Enitities.Session;
import com.fsd_CSE.TnP_Connect.Enitities.SessionRegistration;
import com.fsd_CSE.TnP_Connect.Enitities.Student;
import com.fsd_CSE.TnP_Connect.ExceptionHandling.ResourceNotFoundException;
import com.fsd_CSE.TnP_Connect.Repository.SessionRegistrationRepository;
import com.fsd_CSE.TnP_Connect.Repository.SessionRepository;
import com.fsd_CSE.TnP_Connect.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SessionRegistrationService {
    @Autowired private SessionRegistrationRepository registrationRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private SessionRepository sessionRepository;

    public SessionRegistrationResponse registerForSession(SessionRegistrationRequest request) {
        // Check if student is already registered
        registrationRepository.findByStudentIdAndSessionId(request.getStudentId(), request.getSessionId())
                .ifPresent(reg -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Student is already registered for this session.");
                });

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + request.getStudentId()));

        Session session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with ID: " + request.getSessionId()));

        SessionRegistration newRegistration = new SessionRegistration();
        newRegistration.setStudent(student);
        newRegistration.setSession(session);
        newRegistration.setStatus("REGISTERED");

        return convertToResponse(registrationRepository.save(newRegistration));
    }

    private SessionRegistrationResponse convertToResponse(SessionRegistration registration) {
        SessionRegistrationResponse response = new SessionRegistrationResponse();
        response.setRegistrationId(registration.getId());
        response.setStatus(registration.getStatus());
        response.setRegisteredAt(registration.getRegisteredAt());
        if (registration.getStudent() != null) {
            response.setStudentName(registration.getStudent().getName());
        }
        if (registration.getSession() != null) {
            response.setSessionTitle(registration.getSession().getTitle());
        }
        return response;
    }
}
