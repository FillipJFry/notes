package com.goit.letscode.notes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {

    private static final long EMPTY_ID = -1;
    @Autowired
    private NoteRepository repository;

    public NoteDTO create() {

        return new NoteDTO(createEmptyNote());
    }

    public List<NoteDTO> listAll() {

        List<Note> notes = repository.findAll();
        return notes.stream()
                .map(NoteDTO::new)
                .collect(Collectors.toList());
    }

    public void save(NoteDTO noteDTO) {

        repository.save(noteDTO.toNote());
    }

    public void deleteById(long id) {

        repository.deleteById(id);
    }

    public NoteDTO getById(Long id) {

        if (id != null && repository.existsById(id)) {
            return new NoteDTO(repository.findById(id).orElse(createEmptyNote()));
        }
        return create();
    }

    private Note createEmptyNote() {

        return Note.builder()
                .id(EMPTY_ID)
                .title("")
                .content("")
                .accessType(AccessType.PRIVATE)
                .build();
    }
}
