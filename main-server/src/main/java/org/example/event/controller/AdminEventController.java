package org.example.event.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.dto.EventFullDto;
import org.example.event.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@AllArgsConstructor
@Slf4j
public class AdminEventController {
    private final EventService eventService;


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> findEventsFull(@RequestParam List<Long> users,
                                         @RequestParam List<String> state,
                                         @RequestParam List<Long> categories,
                                         @RequestParam String rangeStart,
                                         @RequestParam String rangeEnd,
                                         @RequestParam (defaultValue = "0") @PositiveOrZero int from,
                                         @RequestParam (defaultValue = "10") @Positive int size) {
        log.info("получен запрос от админа на получение полной информации обо всех событиях");
        return eventService.findEventsFull(users, state, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto findByIdFull(@PathVariable long eventId) {
        log.info("получен запрос от админа на изменения статуса события id={}", eventId);
        return eventService.findByIdFull(eventId);
    }
}
