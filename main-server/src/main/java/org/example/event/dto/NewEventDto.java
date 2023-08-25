package org.example.event.dto;

import lombok.Builder;
import lombok.Data;
import org.example.location.Location;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class NewEventDto {

    @Size(min = 20, max = 2000, message = "поле annotation должно содержать от 2 до 2000 символов")
    private String annotation;

    @NotNull(message = "поле category должен быть пустым")
    private long category;

    @Size(min = 20, max = 7000, message = "поле description должно содержать от 20 до 7000 символов")
    private String description;

    @Size(min = 3, max = 120, message = "поле title должно содержать от 3 до 120 символов")
    private String title;

    private String eventDate;
    private Location location;
    private Boolean paid;
    private int participantLimit;
    private Boolean requestModeration;
}
