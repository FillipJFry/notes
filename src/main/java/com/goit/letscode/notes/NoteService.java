package com.goit.letscode.notes;

import com.goit.letscode.notes.auth.User;
import com.goit.letscode.notes.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.InputMismatchException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {

    private static final long EMPTY_ID = -1;
    private static final int MIN_TITLE_LEN = 5;
    private static final int MAX_TITLE_LEN = 100;
    private static final int MIN_CONTENT_LEN = 5;
    private static final int MAX_CONTENT_LEN = 10000;
    @Autowired
    private NoteRepository notesRepository;
    @Autowired
    private UserRepository userRepository;

    public NoteDTO create() {

        return new NoteDTO(createEmptyNote());
    }

    public List<NoteDTO> listAllForCurrentUser() {

        List<Note> notes = getCurrentUser().getNotes();
        return notes.stream()
                .map(NoteDTO::new)
                .collect(Collectors.toList());
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

    private User getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        String userLogin = authentication.getName();
        return userRepository.findByLogin(userLogin).orElseThrow(
                () -> new RuntimeException("користувач не зареєстрований в БД: " + userLogin));
    }
}
