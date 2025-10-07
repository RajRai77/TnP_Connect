package com.fsd_CSE.TnP_Connect.Repository;

import com.fsd_CSE.TnP_Connect.Enitities.InternshipApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternshipApplicationRepository extends JpaRepository<InternshipApplication, Integer> {
    // We can add custom queries here later if needed
}