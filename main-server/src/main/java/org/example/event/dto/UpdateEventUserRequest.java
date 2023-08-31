package org.example.event.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.PrivateStateAction;
import org.example.location.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class UpdateEventUserRequest {
    @Size(min = 20, max = 2000, message = "поле annotation должно содержать от 2 до 2000 символов")
    String annotation;
    Long category;

    @Size(min = 20, max = 7000, message = "поле annotation должно содержать от 2 до 2000 символов")
    String description;
    String eventDate;
    Location location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    PrivateStateAction stateAction;

    @Size(min = 3, max = 120, message = "поле title должно содержать от 3 до 120 символов")
    String title;

}
