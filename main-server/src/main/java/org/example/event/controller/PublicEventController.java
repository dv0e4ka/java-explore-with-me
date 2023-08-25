package org.example.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.dto.EventShortDto;
import org.example.event.service.EventServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
@Slf4j
public class PublicEventController {
    private final EventServiceImpl eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> findEventsShort(@RequestParam List<Long> users,
                                             @RequestParam List<String> state,
                                             @RequestParam List<Long> categories,
                                             @RequestParam String rangeStart,
                                             @RequestParam String rangeEnd,
                                             @RequestParam (defaultValue = "0") @PositiveOrZero int from,
                                             @RequestParam (defaultValue = "10") @Positive int size) {
        log.info("получен публичный запрос на получение информации обо всех событиях");
        return eventService.findEventsShort(users, state, categories, rangeStart, rangeEnd, from, size);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventShortDto findByIdShort(@PathVariable long id) {
        log.info("получен публичный запрос на получение информации событии id={}", id);
        return findByIdShort(id);
    }
}
