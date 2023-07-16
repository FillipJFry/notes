package com.goit.letscode.notes.auth;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AuthDTO {

    private String login;
    private String password;
}
