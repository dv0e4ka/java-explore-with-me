package ru.practicum.comments.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.service.PrivateCommentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user/{userId}/comments")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PrivateCommentController {
    private final PrivateCommentService privateCommentService;

    @PostMapping("/event/{eventId}")
    public CommentDto addComment(@PathVariable long userId,
                                 @PathVariable long eventId,
                                 @Valid @RequestBody NewCommentDto newCommentDto) {
        log.info("пользователь id={} добавляет новый комментарий", userId);
        return privateCommentService.addComment(userId, newCommentDto, eventId);
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@PathVariable long userId,
                                    @PathVariable long commentId,
                                    @Valid @RequestBody NewCommentDto newCommentDto) {
        log.info("пользователь id={} обновляет свой комментарий id={}", userId, commentId);
        return privateCommentService.updateComment(userId, commentId, newCommentDto);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable long userId,
                              @PathVariable long commentId) {
        log.info("пользователь id={} удаляет свой комментарий", userId);
        privateCommentService.deleteComment(userId, commentId);

    }

    @GetMapping
    public List<CommentDto> getAllCommentsByUser(@PathVariable long userId,
                                                 @RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "10") int size) {
        log.info("пользователь id={} просматривает свои комментарии", userId);
        return privateCommentService.getAllCommentsByUser(userId, from, size);
    }
}
