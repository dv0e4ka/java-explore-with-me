package org.example.event.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.AdminStateAction;
import org.example.location.Location;

@Getter
@Setter
@Builder
public class UpdateEventAdminRequest {
    String annotation;
    long category;
    String description;
    String eventDate;
    Location location;
    boolean paid;
    int participantLimit;
    boolean requestModeration;
    AdminStateAction stateAction;
    String title;
}
