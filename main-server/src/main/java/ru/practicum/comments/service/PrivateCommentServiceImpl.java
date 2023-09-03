package ru.practicum.comments.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.mapper.CommentMapper;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.repository.CommentRepository;
import ru.practicum.enums.State;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.model.EntityNoFoundException;
import ru.practicum.exception.model.RequestException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrivateCommentServiceImpl implements PrivateCommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public CommentDto addComment(long userId, NewCommentDto newCommentDto, long eventId) {
        User user = findUserById(userId);
        Event event = findEventById(eventId);
        checkIfNotPublished(event);

        Comment comment = CommentMapper.toComment(newCommentDto);
        comment.setAuthor(user);
        comment.setEvent(event);

        Comment commentSaved = commentRepository.save(comment);
        return CommentMapper.toCommentDto(commentSaved);
    }

    @Override
    public CommentDto updateComment(long userId, long commentId, NewCommentDto newCommentDto) {
        findUserById(userId);
        Comment comment = findCommentById(commentId);

        String text = newCommentDto.getText();
        comment.setText(text);

        Comment commentUpdated = commentRepository.save(comment);
        return CommentMapper.toCommentDto(commentUpdated);
    }

    @Override
    public void deleteComment(long userId, long commentId) {
        User user = findUserById(userId);
        Comment comment = findCommentById(commentId);

        if (user.getId() != comment.getAuthor().getId()) {
            throw new RequestException(String.format(
                    "пользователь id=%d не может удалять чужой комменатрий id=%d", userId, commentId
            ));
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDto> getAllCommentsByUser(long userId, int from, int size) {
        User user = findUserById(userId);
        PageRequest page = PageRequest.of(from, size, Sort.by("id"));
        List<Comment> commentList = commentRepository.findAllByAuthor(user, page).getContent();
        return CommentMapper.toCommentDtoList(commentList);
    }

    User findUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNoFoundException(String.format("пользователь id=%d не найден", userId))
        );
    }

    Event findEventById(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNoFoundException(String.format("событие id=%d не найдено", eventId))
        );
    }

    Comment findCommentById(long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new EntityNoFoundException(String.format("комментарий id=%d не найден", commentId))
        );
    }

    void checkIfNotPublished(Event event) {
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new RequestException(String.format("событие id=%d не опубликовано", event.getId()));
        };
    }
}
