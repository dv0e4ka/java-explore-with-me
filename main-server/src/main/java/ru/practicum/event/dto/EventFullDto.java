package ru.practicum.event.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.categoriy.dto.CategoryDto;
import ru.practicum.location.Location;
import ru.practicum.user.dto.UserShortDto;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class EventFullDto {
    private long id;
    private long confirmedRequests;
    private long views;
    private int participantLimit;
    private String createdOn;
    private String description;
    private String publishedOn;
    private String state;
    private boolean requestModeration;

    @NotNull(message = "поле annotation должен быть пустым")
    private String annotation;

    @NotNull(message = "поле category должен быть пустым")
    private CategoryDto category;

    @NotNull(message = "поле eventDate должен быть пустым")
    private String eventDate;

    @NotNull(message = "поле initiator должен быть пустым")
    private UserShortDto initiator;

    @NotNull(message = "поле location должен быть пустым")
    private Location location;

    @NotNull(message = "поле title должен быть пустым")
    private String title;

    @NotNull(message = "поле paid должен быть пустым")
    private boolean paid;
}
