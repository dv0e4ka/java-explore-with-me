package ru.practicum.compilation.model;

import lombok.*;
import ru.practicum.event.model.Event;

import javax.persistence.*;
import java.util.List;

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "compilation_events_relation",
            joinColumns = { @JoinColumn(name = "compilation_id") },
            inverseJoinColumns = { @JoinColumn(name = "event_id") })
    private List<Event> events;

    @Column(name = "pinned", columnDefinition = "boolean default false")
    private Boolean pinned;

    @Column(name = "title")
    private String title;
}
