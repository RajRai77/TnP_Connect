package com.fsd_CSE.TnP_Connect.Service;
import com.fsd_CSE.TnP_Connect.DTO.SessionRequest;
import com.fsd_CSE.TnP_Connect.DTO.SessionResponse;
import com.fsd_CSE.TnP_Connect.Enitities.Session;
import com.fsd_CSE.TnP_Connect.Enitities.TnPAdmin;
import com.fsd_CSE.TnP_Connect.ExceptionHandling.ResourceNotFoundException;
import com.fsd_CSE.TnP_Connect.Repository.SessionRepository;
import com.fsd_CSE.TnP_Connect.Repository.TnPAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class SessionService {
    @Autowired private SessionRepository sessionRepository;
    @Autowired private TnPAdminRepository tnpAdminRepository;

    public SessionResponse createSession(SessionRequest request) {
        TnPAdmin admin = tnpAdminRepository.findById(request.getCreatedByAdminId())
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with ID: " + request.getCreatedByAdminId()));

        Session session = new Session();
        session.setTitle(request.getTitle());
        session.setDescription(request.getDescription());
        session.setSpeaker(request.getSpeaker());
        session.setTargetBranch(request.getTargetBranch());
        session.setTargetYear(request.getTargetYear());
        session.setSessionDatetime(request.getSessionDatetime());
        session.setJoinUrl(request.getJoinUrl());
        session.setCreatedByAdmin(admin);

        return convertToResponse(sessionRepository.save(session));
    }

    public List<SessionResponse> getAllSessions() {
        return sessionRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private SessionResponse convertToResponse(Session session) {
        SessionResponse response = new SessionResponse();
        response.setId(session.getId());
        response.setTitle(session.getTitle());
        response.setDescription(session.getDescription());
        response.setSpeaker(session.getSpeaker());
        response.setTargetBranch(session.getTargetBranch());
        response.setTargetYear(session.getTargetYear());
        response.setSessionDatetime(session.getSessionDatetime());
        response.setJoinUrl(session.getJoinUrl());
        if (session.getCreatedByAdmin() != null) {
            response.setCreatedByAdminName(session.getCreatedByAdmin().getName());
        }
        if (session.getRegistrations() != null) {
            response.setRegistrationCount(session.getRegistrations().size());
        } else {
            response.setRegistrationCount(0);
        }
        return response;
    }
}
