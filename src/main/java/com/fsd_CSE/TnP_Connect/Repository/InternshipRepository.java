package com.fsd_CSE.TnP_Connect.Repository;


import com.fsd_CSE.TnP_Connect.Enitities.Internship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InternshipRepository extends JpaRepository<Internship, Integer> {

    // We will add custom queries later, for example:
    // List<Internship> findByCompany(String company);
    // List<Internship> findByStatus(String status);

}