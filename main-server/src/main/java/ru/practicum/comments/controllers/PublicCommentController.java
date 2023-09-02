package ru.practicum.comments.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.service.PublicCommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PublicCommentController {
    PublicCommentService publicCommentService;


    @GetMapping("/events/{eventId}/comments")
    public List<CommentDto> getCommentsByEvent(@PathVariable Long eventId,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "10") int size) {
        log.info("пришел запрос на получение комментариев к событию id={}", eventId);
        return publicCommentService.getCommentsByEvent(eventId, from, size);
    }

    @GetMapping("/comments/{commentId}")
    public CommentDto getById(@PathVariable long commentId) {
        log.info("пришел запрос на просмотр комментария id={}", commentId);
        return publicCommentService.getById(commentId);
    }
}
