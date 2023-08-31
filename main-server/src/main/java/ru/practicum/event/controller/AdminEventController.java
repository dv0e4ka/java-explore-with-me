package ru.practicum.event.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.service.AdminEventService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@AllArgsConstructor
@Validated
@Slf4j
public class AdminEventController {
    private final AdminEventService eventService;


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> findEventsFull(@RequestParam(required = false) List<Long> users,
                                             @RequestParam(required = false) List<String> state,
                                             @RequestParam(required = false) List<Long> categories,
                                             @RequestParam(required = false) String rangeStart,
                                             @RequestParam(required = false) String rangeEnd,
                                             @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                             @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("получен запрос от админа на получение полной информации обо всех событиях");
        return eventService.findEventsFull(users, state, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEventByAdmin(@PathVariable long eventId,
                                           @RequestBody @Valid UpdateEventAdminRequest updateEvent) {
        log.info("получен запрос от админа на изменения статуса события id={}", eventId);
        return eventService.updateEventByAdmin(eventId, updateEvent);
    }
}
