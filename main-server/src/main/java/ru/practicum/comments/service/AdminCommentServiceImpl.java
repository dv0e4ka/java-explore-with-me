package ru.practicum.comments.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.mapper.CommentMapper;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.repository.CommentRepository;
import ru.practicum.exception.model.EntityNoFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCommentServiceImpl implements AdminCommentService {
    private final CommentRepository commentRepository;

    @Override
    public void deleteComment(long commentId) {
        commentRepository.findById(commentId).orElseThrow(
                () -> new EntityNoFoundException(String.format("событие id=%d не найдено", commentId))
        );

        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDto> findAllComments(int from, int size) {
        PageRequest page = PageRequest.of(from, size);
        List<Comment> commentList = commentRepository.findAll(page).getContent();
        return CommentMapper.toCommentDtoList(commentList);
    }


}
