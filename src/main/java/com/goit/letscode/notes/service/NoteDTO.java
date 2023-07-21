package com.goit.letscode.notes.service;

import com.goit.letscode.notes.auth.data.User;
import com.goit.letscode.notes.data.AccessType;
import com.goit.letscode.notes.data.Note;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteDTO {
    private long id;
    private String title;
    private String content;
    private AccessType accessType;

    public NoteDTO(Note note) {

        id = note.getId();
        title = note.getTitle();
        content = note.getContent();
        accessType = note.getAccessType();
    }

    public Note toNote(User owner) {

        assert owner != null;
        Note note = new Note();
        note.setId(id);
        note.setTitle(title);
        note.setContent(content);
        note.setAccessType(accessType);
        note.setOwner(owner);
        return note;
    }
}
