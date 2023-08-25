package org.example.modelForRefacting;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.location.Location;

@Getter
@Setter
@Builder
public class UpdateEventUserRequest {
    String annotation;
    long category;
    String description;
    String eventDate;
    Location location;
    boolean paid;
    int participantLimit;
    boolean requestModeration;
    String stateAction;
    String title;
}
