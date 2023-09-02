package ru.practicum.comments.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.user.mapper.UserMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CommentMapper {

    public static Comment toComment(NewCommentDto commentDto) {
        return Comment.builder()
                .createdOn(LocalDateTime.now())
                .text(commentDto.getText())
                .build();
    }

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .commentator(UserMapper.toUserShortDto(comment.getAuthor()))
                .event(EventMapper.toEventShortDto(comment.getEvent()))
                .text(comment.getText())
                .createdOn(comment.getCreatedOn())
                .build();
    }

    public static List<CommentDto> toCommentDtoList(List<Comment> commentList) {
        return commentList.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }
}
