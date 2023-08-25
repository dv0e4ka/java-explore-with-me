package org.example.event.model;

import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.example.categoriy.model.Category;
import org.example.compilation.model.Compilation;
import org.example.enums.State;
import org.example.location.Location;
import org.example.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "events")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "confirmedRequests")
    private long confirmedRequests;

    @Column(name = "views")
    private long views;

    @Column(name = "participantLimit")
    private int participantLimit;

    @Column(name = "createdOn")
    private LocalDateTime createdOn;

    @Column(name = "description")
    private String description;

    @Column(name = "publishedOn")
    private LocalDateTime publishedOn;

    @Enumerated(EnumType.STRING)
    private State state;

    @Column(name = "requestModeration")
    private Boolean requestModeration;

    @Column(name = "annotation")
    private String annotation;

    @Column(name = "eventDate")
    private LocalDateTime eventDate;

    @Column(name = "title")
    private String title;

    @Column(name = "paid")
    private Boolean paid;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compilation")
    @JsonIgnore
    private Compilation compilation;

}
