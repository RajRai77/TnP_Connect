package com.fsd_CSE.TnP_Connect.Service;

import com.fsd_CSE.TnP_Connect.DTO.ResourceRequest;
import com.fsd_CSE.TnP_Connect.DTO.ResourceResponse;
import com.fsd_CSE.TnP_Connect.Enitities.Resource;
import com.fsd_CSE.TnP_Connect.Enitities.TnPAdmin;
import com.fsd_CSE.TnP_Connect.ExceptionHandling.ResourceNotFoundException;
import com.fsd_CSE.TnP_Connect.Repository.ResourceRepository;
import com.fsd_CSE.TnP_Connect.Repository.TnPAdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


// ResourceService.java (Business Logic)

@Service
public class ResourceService {
    private static final Logger log = LoggerFactory.getLogger(ResourceService.class);

    @Autowired private ResourceRepository resourceRepository;
    @Autowired private TnPAdminRepository tnpAdminRepository;

    public ResourceResponse createResource(ResourceRequest request) {
        log.info("Admin ID: {} is creating a new resource", request.getCreatedByAdminId());
        TnPAdmin admin = tnpAdminRepository.findById(request.getCreatedByAdminId())
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with ID: " + request.getCreatedByAdminId()));

        Resource resource = new Resource();
        resource.setTitle(request.getTitle());
        resource.setType(request.getType());
        resource.setFileUrl(request.getFileUrl());
        resource.setDescription(request.getDescription());
        resource.setCreatedByAdmin(admin);

        Resource savedResource = resourceRepository.save(resource);
        log.info("Successfully created resource with ID: {}", savedResource.getId());
        return convertToResponse(savedResource);
    }

    public List<ResourceResponse> getAllResources() {
        log.info("Fetching all resources");
        return resourceRepository.findAll().stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public void deleteResource(Integer id) {
        log.warn("Attempting to delete resource with ID: {}", id);
        if (!resourceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resource not found with ID: " + id);
        }
        resourceRepository.deleteById(id);
        log.info("Successfully deleted resource with ID: {}", id);
    }

    private ResourceResponse convertToResponse(Resource resource) {
        ResourceResponse response = new ResourceResponse();
        response.setId(resource.getId());
        response.setTitle(resource.getTitle());
        response.setType(resource.getType());
        response.setFileUrl(resource.getFileUrl());
        response.setDescription(resource.getDescription());
        response.setCreatedAt(resource.getCreatedAt());
        if (resource.getCreatedByAdmin() != null) {
            response.setCreatedByAdminName(resource.getCreatedByAdmin().getName());
        }
        return response;
    }
}
