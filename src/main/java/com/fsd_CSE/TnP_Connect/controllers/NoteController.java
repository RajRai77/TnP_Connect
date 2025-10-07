package com.fsd_CSE.TnP_Connect.controllers;
import com.fsd_CSE.TnP_Connect.DTO.NoteRequest;
import com.fsd_CSE.TnP_Connect.DTO.NoteResponse;
import com.fsd_CSE.TnP_Connect.Service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// FILE 4: NoteController.java (API Endpoints)
// =====================================================================================
@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @PostMapping("/")
    public ResponseEntity<NoteResponse> createNote(@RequestBody NoteRequest request) {
        NoteResponse response = noteService.createNote(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<NoteResponse>> getAllNotes(
            @RequestParam(required = false) String branch,
            @RequestParam(required = false) Integer year) {
        List<NoteResponse> notes = noteService.getAllNotes(branch, year);
        return ResponseEntity.ok(notes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Integer id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }
}
