package com.goit.letscode.notes.service;

import com.goit.letscode.notes.auth.data.User;
import com.goit.letscode.notes.auth.data.UserRepository;
import com.goit.letscode.notes.data.AccessType;
import com.goit.letscode.notes.data.Note;
import com.goit.letscode.notes.data.NoteRepository;
import static com.goit.letscode.notes.data.Constraints.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.InputMismatchException;
import java.util.Set;
import java.util.stream.Stream;

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

    public Stream<NoteDTO> listAllForCurrentUser() {

        Set<Note> notes = getCurrentUser().getNotes();
        return notes.stream()
                .sorted((l, r) -> (int)(r.getId() - l.getId()))
                .map(NoteDTO::new);
    }

    public void save(NoteDTO noteDTO) throws InputMismatchException {

        String title = noteDTO.getTitle();
        if (title.length() < MIN_TITLE_LEN || title.length() > MAX_TITLE_LEN)
            throw new InputMismatchException("Довжина заголовка не відповідає вимогам: від "
                                            + MIN_TITLE_LEN + " до " + MAX_TITLE_LEN + " симв.");
        String content = noteDTO.getContent();
        if (content.length() < MIN_CONTENT_LEN || content.length() > MAX_CONTENT_LEN)
            throw new InputMismatchException("Довжина тексту нотатки не відповідає вимогам: від "
                                        + MIN_CONTENT_LEN + " до " + MAX_CONTENT_LEN + " симв.");
        notesRepository.save(noteDTO.toNote(getCurrentUser()));
    }

    public void deleteById(Long id) {

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

    private User getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        String userLogin = authentication.getName();
        return userRepository.findByLogin(userLogin).orElseThrow(
                () -> new RuntimeException("користувач не зареєстрований в БД: " + userLogin));
    }
}
