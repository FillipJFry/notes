package com.goit.letscode.notes.noteDTO;

import com.goit.letscode.notes.AccessType;
import com.goit.letscode.notes.Note;
import lombok.Builder;
import lombok.Data;

@Builder
@Data

public class NoteDTO {
    private long id;
    private String title;
    private String content;
    private AccessType accessType;

    public static NoteDTO fromNote(Note note) {

        return NoteDTO.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .accessType(note.getAccessType())
                .build();
    }

    public static Note fromDto(NoteDTO noteDTO) {
        Note note = new Note();
        note.setId(noteDTO.getId());
        note.setTitle(noteDTO.getTitle());
        note.setContent(noteDTO.getContent());
        note.setAccessType(noteDTO.getAccessType());

        return note;
    }
}
