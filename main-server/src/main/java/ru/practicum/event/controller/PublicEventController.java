package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.enums.EventSort;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.service.PublicEventService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
@Validated
@Slf4j
public class PublicEventController {
    private final PublicEventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> findEventsByPublicParameters(@RequestParam(required = false) String text,
                                                            @RequestParam(required = false) List<Long> categories,
                                                            @RequestParam(required = false) Boolean paid,
                                                            @RequestParam(required = false) String rangeStart,
                                                            @RequestParam(required = false) String rangeEnd,
                                                            @RequestParam(required = false) Boolean onlyAvailable,
                                                            @RequestParam(required = false) @Valid EventSort sort,
                                                            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                            @RequestParam(defaultValue = "10") @Positive int size,
                                                            HttpServletRequest request) {
        log.info("получен публичный запрос на получение информации обо всех событиях");

        return eventService.findEventsByPublicParameters(
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request
        );
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto findByPublicIdShort(@PathVariable long id, HttpServletRequest request) {
        log.info("получен публичный запрос на получение информации событии id={}", id);
        return eventService.findByPublicIdShort(id, request);
    }
}
