package ru.practicum.event.model;

import lombok.*;
import ru.practicum.categoriy.model.Category;
import ru.practicum.enums.State;
import ru.practicum.location.Location;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "events")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "confirmed_requests")
    private int confirmedRequests;

    @Column(name = "annotation")
    private String annotation;

    @Column(name = "participant_limit")
    private int participantLimit;

    @Column(name = "description")
    private String description;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Enumerated(EnumType.STRING)
    private State state;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Column(name = "event_date")
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

    @Transient
    private long views;

}
