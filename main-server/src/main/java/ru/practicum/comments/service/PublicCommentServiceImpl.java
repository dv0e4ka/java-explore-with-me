package ru.practicum.comments.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.mapper.CommentMapper;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.repository.CommentRepository;
import ru.practicum.enums.State;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.model.EntityNoFoundException;
import ru.practicum.exception.model.RequestException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicCommentServiceImpl implements PublicCommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;

    @Override
    public List<CommentDto> getCommentsByEvent(Long eventId, int from, int size) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNoFoundException(String.format("событие id=%d не найдено", eventId))
        );

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new RequestException(String.format("событие id=%d не опубликовано", event.getId()));
        }

        List<Comment> commentList = commentRepository.findAllByEvent(event);
        return CommentMapper.toCommentDtoList(commentList);
    }

    @Override
    public CommentDto getById(long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new EntityNoFoundException(String.format("комменатрий id=%d не найден", commentId))
        );
        return CommentMapper.toCommentDto(comment);
    }
}