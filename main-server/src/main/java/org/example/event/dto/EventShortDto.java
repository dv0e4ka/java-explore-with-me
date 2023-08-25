package org.example.event.dto;

import lombok.Builder;
import lombok.Data;
import org.example.categoriy.dto.CategoryDto;
import org.example.user.dto.UserShortDto;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class EventShortDto {
    private long id;
    private long views;

    @NotNull(message = "поле annotation должен быть пустым")
    private String annotation;

    @NotNull(message = "поле category должен быть пустым")
    private CategoryDto category;

    @NotNull(message = "поле eventDate должен быть пустым")
    private String eventDate;

    @NotNull(message = "поле initiator должен быть пустым")
    private UserShortDto initiator;

    @NotNull(message = "поле paid должен быть пустым")
    private Boolean paid;

    @NotNull(message = "поле title должен быть пустым")
    private String title;
}
