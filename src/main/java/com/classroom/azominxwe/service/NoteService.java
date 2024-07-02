package com.classroom.azominxwe.service;

import com.classroom.azominxwe.model.Note;
import com.classroom.azominxwe.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private TrimestreService trimestreService;

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public List<Note> getNotesParTrimestreActif() {
        return noteRepository.findByTrimestreActifTrueOrderByEleve();
    }

    public Optional<Note> getNoteById(Long id) {
        return noteRepository.findById(id);
    }

    public Note saveNote(Note note) {
        return noteRepository.save(note);
    }

    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }

    public void updateNote(Long id, Note noteDetails) {
        Optional<Note> optionalNote = noteRepository.findById(id);
        if (optionalNote.isPresent()) {
            Note note = optionalNote.get();
            note.setEleve(noteDetails.getEleve());
            note.setClasseMatiere(noteDetails.getClasseMatiere());
            note.setNote(noteDetails.getNote());
            note.setTrimestre(trimestreService.getActiveTrimestre());
            noteRepository.save(note);
        }
    }
}
