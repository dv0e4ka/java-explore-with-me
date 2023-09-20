package ru.practicum.comments.service;

import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.NewCommentDto;

import java.util.List;

public interface PrivateCommentService {

    CommentDto addComment(long userId, NewCommentDto newCommentDto, long eventId);

    CommentDto updateComment(long userId, long commentId, NewCommentDto newCommentDto);

    void deleteComment(long userId, long commentId);

    List<CommentDto> getAllCommentsByUser(long userId, int from, int size);
}
