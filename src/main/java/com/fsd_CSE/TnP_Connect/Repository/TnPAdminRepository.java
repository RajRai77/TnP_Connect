package com.fsd_CSE.TnP_Connect.Repository;

import com.fsd_CSE.TnP_Connect.Enitities.TnPAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TnPAdminRepository extends JpaRepository<TnPAdmin, Integer> {

    // Custom query method to find an admin by their email address.
    Optional<TnPAdmin> findByEmail(String email);

}