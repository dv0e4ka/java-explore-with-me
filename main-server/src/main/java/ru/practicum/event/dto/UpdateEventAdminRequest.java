package ru.practicum.event.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.enums.AdminStateAction;
import ru.practicum.location.Location;

import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
public class UpdateEventAdminRequest {
    @Size(min = 20, max = 2000, message = "поле annotation должно содержать от 2 до 2000 символов")
    private String annotation;

    @Size(min = 20, max = 7000, message = "поле description должно содержать от 20 до 7000 символов")
    private String description;

    @Size(min = 3, max = 120, message = "поле title должно содержать от 3 до 120 символов")
    private String title;

    private Long category;
    private String eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private AdminStateAction stateAction;
}
