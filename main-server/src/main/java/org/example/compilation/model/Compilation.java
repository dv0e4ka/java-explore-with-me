package org.example.compilation.model;

import lombok.*;
import org.example.event.model.Event;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "compilations")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "events", joinColumns = "id")
//    private Set<Event> events;

    @Column(name = "pinned")
    private Boolean pinned;

    @Column(name = "title")
    private String title;
}
