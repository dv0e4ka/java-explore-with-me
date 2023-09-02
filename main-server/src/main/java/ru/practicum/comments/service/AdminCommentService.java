package ru.practicum.comments.service;

import ru.practicum.comments.dto.CommentDto;

import java.util.List;

public interface AdminCommentService {

    void deleteComment(long commentId);

    List<CommentDto> findAllComments(int from, int size);
}
