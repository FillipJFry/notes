package com.goit.letscode.notes.data;

import javax.persistence.*;

import com.goit.letscode.notes.auth.data.User;
import lombok.*;

@Entity
@Table(name = "note")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"owner"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;
    @Column(name = "title", length = 100, nullable = false)
    private String title;
    @Column(name = "content", nullable = false)
    private String content;
    @Column(name = "access_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccessType accessType;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_user", nullable = false)
    private User owner;
}
