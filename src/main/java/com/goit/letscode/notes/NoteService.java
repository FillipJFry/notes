package com.goit.letscode.notes;

import com.goit.letscode.notes.auth.User;
import com.goit.letscode.notes.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {

    private static final long EMPTY_ID = -1;
    @Autowired
    private NoteRepository notesRepository;
    @Autowired
    private UserRepository userRepository;

    public NoteDTO create() {

        return new NoteDTO(createEmptyNote());
    }

    public List<NoteDTO> listAllForUser(String userLogin) {

        User currentUser = userRepository.findByLogin(userLogin).orElseThrow();
        List<Note> notes = currentUser.getNotes();
        return notes.stream()
                .map(NoteDTO::new)
                .collect(Collectors.toList());
    }

    public void save(NoteDTO noteDTO, String userLogin) {

        User currentUser = userRepository.findByLogin(userLogin).orElseThrow();
        notesRepository.save(noteDTO.toNote(currentUser));
    }

    public void deleteById(long id) {

        notesRepository.deleteById(id);
    }

    public NoteDTO getById(Long id) {

        if (id != null && notesRepository.existsById(id)) {
            return new NoteDTO(notesRepository.findById(id).orElse(createEmptyNote()));
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
