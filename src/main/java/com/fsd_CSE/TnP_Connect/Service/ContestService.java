package com.fsd_CSE.TnP_Connect.Service;
import com.fsd_CSE.TnP_Connect.DTO.ContestRequest;
import com.fsd_CSE.TnP_Connect.DTO.ContestResponse;
import com.fsd_CSE.TnP_Connect.Enitities.Contest;
import com.fsd_CSE.TnP_Connect.Enitities.TnPAdmin;
import com.fsd_CSE.TnP_Connect.ExceptionHandling.ResourceNotFoundException;
import com.fsd_CSE.TnP_Connect.Repository.ContestRepository;
import com.fsd_CSE.TnP_Connect.Repository.TnPAdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;


// ContestService.java (Business Logic)

@Service
public class ContestService {
    private static final Logger log = LoggerFactory.getLogger(ContestService.class);

    @Autowired private ContestRepository contestRepository;
    @Autowired private TnPAdminRepository tnpAdminRepository;

    public ContestResponse createContest(ContestRequest request) {
        TnPAdmin admin = tnpAdminRepository.findById(request.getCreatedByAdminId())
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with ID: " + request.getCreatedByAdminId()));

        Contest contest = new Contest();
        contest.setTitle(request.getTitle());
        contest.setPlatform(request.getPlatform());
        contest.setContestUrl(request.getContestUrl());
        contest.setDescription(request.getDescription());
        contest.setStartDatetime(request.getStartDatetime());
        contest.setEndDatetime(request.getEndDatetime());
        contest.setCreatedByAdmin(admin);

        Contest savedContest = contestRepository.save(contest);
        log.info("Admin ID {} created new contest with ID {}", admin.getId(), savedContest.getId());
        return convertToResponse(savedContest);
    }

    public List<ContestResponse> getAllContests() {
        log.info("Fetching all contests");
        return contestRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private ContestResponse convertToResponse(Contest contest) {
        ContestResponse response = new ContestResponse();
        response.setId(contest.getId());
        response.setTitle(contest.getTitle());
        response.setPlatform(contest.getPlatform());
        response.setContestUrl(contest.getContestUrl());
        response.setDescription(contest.getDescription());
        response.setStartDatetime(contest.getStartDatetime());
        response.setEndDatetime(contest.getEndDatetime());
        response.setStatus(calculateStatus(contest)); // Calculate status dynamically
        if (contest.getCreatedByAdmin() != null) {
            response.setCreatedByAdminName(contest.getCreatedByAdmin().getName());
        }
        return response;
    }

    // This is the "beautiful intelligence" - a helper method to determine the contest status
    private String calculateStatus(Contest contest) {
        OffsetDateTime now = OffsetDateTime.now();
        if (now.isBefore(contest.getStartDatetime())) {
            return "UPCOMING";
        } else if (now.isAfter(contest.getEndDatetime())) {
            return "COMPLETED";
        } else {
            return "LIVE";
        }
    }
}