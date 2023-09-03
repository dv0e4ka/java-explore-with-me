package ru.practicum.comments.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {
    private long id;

    private UserShortDto author;

    private EventShortDto event;

    private String text;

    private LocalDateTime createdOn;
}
