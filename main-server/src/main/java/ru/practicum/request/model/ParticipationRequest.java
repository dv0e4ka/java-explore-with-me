package org.example.request.model;

import lombok.*;
import org.example.event.model.Event;
import org.example.enums.RequestStatus;
import org.example.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "requests")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "created")
    private LocalDateTime created;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "requester")
    private User requester;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;
}
