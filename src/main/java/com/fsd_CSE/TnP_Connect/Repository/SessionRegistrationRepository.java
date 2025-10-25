package com.fsd_CSE.TnP_Connect.Repository;

import com.fsd_CSE.TnP_Connect.Enitities.SessionRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRegistrationRepository extends JpaRepository<SessionRegistration, Integer> {
    // This is a crucial method to prevent a student from registering for the same session twice

    Optional<SessionRegistration> findByStudentIdAndSessionId(Integer studentId, Integer sessionId);
}