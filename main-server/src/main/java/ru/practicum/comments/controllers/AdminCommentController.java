package ru.practicum.comments.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.service.AdminCommentService;

import java.util.List;

@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
@Slf4j
public class AdminCommentController {
    private final AdminCommentService adminCommentService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> findAllComments(@RequestParam(defaultValue = "0") int from,
                                            @RequestParam(defaultValue = "10") int size) {
        log.info("пришел запрос от админа на просмотр комментариев");
        return adminCommentService.findAllComments(from, size);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{commentId}")
    public void deleteComment(@PathVariable long commentId) {
        log.info("пришел запрос от админа на удаление комментария id={}", commentId);
        adminCommentService.deleteComment(commentId);
    }
}
