package com.goit.letscode.notes;

import com.goit.letscode.notes.service.NoteDTO;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class DTOWithTitle extends NoteDTO {

    private String pageTitle;
}
