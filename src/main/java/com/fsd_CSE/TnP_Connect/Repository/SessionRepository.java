package com.fsd_CSE.TnP_Connect.Repository;


import com.fsd_CSE.TnP_Connect.Enitities.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {
}