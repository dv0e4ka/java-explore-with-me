package org.example.event.dto;

import lombok.Builder;
import lombok.Data;
import org.example.categoriy.dto.CategoryDto;
import org.example.user.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class EventShortDto {
    private long id;
    private long views;

    @NotBlank(message = "поле annotation не должно быть пустым")
    private String annotation;

    @NotNull(message = "поле category не должно быть пустым")
    private CategoryDto category;

    @NotBlank(message = "поле eventDate не должно быть пустым")
    private String eventDate;

    @NotNull(message = "поле initiator не должно быть пустым")
    private UserShortDto initiator;

    @NotNull(message = "поле paid не должно быть пустым")
    private Boolean paid;

    @NotNull(message = "поле title не должно быть пустым")
    private String title;

    private int confirmedRequests;
}
