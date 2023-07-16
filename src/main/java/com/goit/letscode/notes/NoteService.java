package com.goit.letscode.notes;

import com.goit.letscode.notes.noteDTO.NoteDTO;
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
        return NoteDTO.builder()
                .id(EMPTY_ID)
                .title("")
                .content("")
                .accessType(AccessType.PRIVATE)
                .build();
    }

    public List<NoteDTO> listAll() {
        List<Note> notes = repository.findAll();
        return notes.stream()
                .map(NoteDTO::fromNote)
                .collect(Collectors.toList());
    }

    public void save(NoteDTO noteDTO) {
        Note note = NoteDTO.fromDto(noteDTO);
        repository.save(note);
    }

    public void deleteById(long id) {

        repository.deleteById(id);
    }

    public NoteDTO getById(Long id) {
        if (id != null && repository.existsById(id)) {
            return NoteDTO.fromNote(repository.findById(id).orElse(NoteDTO.fromDto(create())));
        }
        return create();
    }
}
