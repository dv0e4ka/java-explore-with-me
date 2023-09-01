package ru.practicum.event.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.enums.PrivateStateAction;
import ru.practicum.location.Location;

import javax.validation.constraints.Size;

@Data
@Builder
public class UpdateEventUserRequest {
    @Size(min = 20, max = 2000, message = "поле annotation должно содержать от 2 до 2000 символов")
    private String annotation;
    private Long category;

    @Size(min = 20, max = 7000, message = "поле annotation должно содержать от 2 до 2000 символов")
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private PrivateStateAction stateAction;

    @Size(min = 3, max = 120, message = "поле title должно содержать от 3 до 120 символов")
    private String title;

}
