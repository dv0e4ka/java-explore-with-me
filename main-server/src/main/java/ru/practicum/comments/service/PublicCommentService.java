package ru.practicum.comments.service;

import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.comments.dto.CommentDto;

import java.util.List;

public interface PublicCommentService {

    List<CommentDto> getCommentsByEvent(Long eventId, int from, int size);

    CommentDto getById(@PathVariable long commentId);
}
