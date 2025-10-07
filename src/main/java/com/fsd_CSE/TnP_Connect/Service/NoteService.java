package com.fsd_CSE.TnP_Connect.Service;
import com.fsd_CSE.TnP_Connect.DTO.NoteRequest;
import com.fsd_CSE.TnP_Connect.DTO.NoteResponse;
import com.fsd_CSE.TnP_Connect.Enitities.Notes;
import com.fsd_CSE.TnP_Connect.Enitities.TnPAdmin;
import com.fsd_CSE.TnP_Connect.ExceptionHandling.ResourceNotFoundException;
import com.fsd_CSE.TnP_Connect.Repository.TnPAdminRepository;
import com.fsd_CSE.TnP_Connect.Repository.notesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {

    private static final Logger log = LoggerFactory.getLogger(NoteService.class);

        @Autowired private notesRepository noteRepository;
        @Autowired private TnPAdminRepository tnpAdminRepository;

        public NoteResponse createNote(NoteRequest request) {
            TnPAdmin admin = tnpAdminRepository.findById(request.getUploadedByAdminId())
                    .orElseThrow(() -> new ResourceNotFoundException("Admin not found with ID: " + request.getUploadedByAdminId()));

            Notes note = new Notes();
            note.setTitle(request.getTitle());
            note.setDescription(request.getDescription());
            note.setFileUrl(request.getFileUrl());
            note.setTargetBranch(request.getTargetBranch());
            note.setTargetYear(request.getTargetYear());
            note.setUploadedByAdmin(admin);

            Notes savedNote = noteRepository.save(note);
            log.info("Admin ID {} uploaded new note with ID {}", admin.getId(), savedNote.getId());
            return convertToResponse(savedNote);
        }

        public List<NoteResponse> getAllNotes(String branch, Integer year) {
            log.info("Fetching notes. Filter by branch: {}, year: {}", branch, year);
            List<Notes> allNotes = noteRepository.findAll();

            // Server-side filtering to find relevant notes for the student
            return allNotes.stream()
                    .filter(n -> branch == null || "ALL".equalsIgnoreCase(n.getTargetBranch()) || branch.equalsIgnoreCase(n.getTargetBranch()))
                    .filter(n -> year == null || n.getTargetYear() == 0 || year.equals(n.getTargetYear()))
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        }

        public void deleteNote(Integer id) {
            log.warn("Attempting to delete note with ID: {}", id);
            if (!noteRepository.existsById(id)) {
                throw new ResourceNotFoundException("Note not found with ID: " + id);
            }
            noteRepository.deleteById(id);
            log.info("Successfully deleted note with ID: {}", id);
        }

        private NoteResponse convertToResponse(Notes note) {
            NoteResponse response = new NoteResponse();
            response.setId(note.getId());
            response.setTitle(note.getTitle());
            response.setDescription(note.getDescription());
            response.setFileUrl(note.getFileUrl());
            response.setTargetBranch(note.getTargetBranch());
            response.setTargetYear(note.getTargetYear());
            response.setUploadedAt(note.getUploadedAt());
            if (note.getUploadedByAdmin() != null) {
                response.setUploadedByAdminName(note.getUploadedByAdmin().getName());
            }
            return response;
        }


}
