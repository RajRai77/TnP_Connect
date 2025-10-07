package com.fsd_CSE.TnP_Connect.Repository;

import com.fsd_CSE.TnP_Connect.Enitities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    // We can add filtering queries here later if performance becomes an issue
}