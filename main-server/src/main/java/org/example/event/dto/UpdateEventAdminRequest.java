package org.example.event.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.AdminStateAction;
import org.example.location.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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
    String title;

    Long category;
    String eventDate;
    Location location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    AdminStateAction stateAction;
}
