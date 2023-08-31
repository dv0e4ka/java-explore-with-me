package ru.practicum.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.categoriy.model.Category;
import ru.practicum.categoriy.util.CategoryMapper;
import ru.practicum.enums.State;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.location.Location;
import ru.practicum.user.model.User;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.util.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// TODO заполнить маппер
@UtilityClass
public class EventMapper {

    public static Event toEvent(NewEventDto newEventDto, User requester, Location location, Category category) {
        if (newEventDto.getPaid() == null) {
            newEventDto.setPaid(false);
        }

        boolean requestModeration = true;
        if (newEventDto.getRequestModeration() != null) {
            requestModeration = newEventDto.getRequestModeration();
        }
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .description(newEventDto.getDescription())
                .title(newEventDto.getTitle())
                .eventDate(LocalDateTime.parse(newEventDto.getEventDate(), DateTimeFormat.formatter))
                .location(location)
                .views(0)
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(requestModeration)
                .initiator(requester)
                .createdOn(LocalDateTime.now())
                .state(State.PENDING)
                .build();
    }

    public static EventShortDto toEventShortDto(Event event) {
        if (event.getPaid() == null) {
            event.setPaid(false);
        }

        return EventShortDto.builder()
                .id(event.getId())
                .views(event.getViews())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .eventDate(event.getEventDate().format(DateTimeFormat.formatter))
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .confirmedRequests(event.getConfirmedRequests())
                .build();
    }


    public static EventFullDto toEventFullDto(Event event) {
        String publishedOn = "";
        if (event.getPublishedOn() != null) {
            publishedOn = event.getPublishedOn().toString();
        }
        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .paid(event.getPaid())
                .views(event.getViews())
                .eventDate(event.getEventDate().format(DateTimeFormat.formatter))
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .description(event.getDescription())
                .participantLimit(event.getParticipantLimit())
                .state(event.getState().toString())
                .createdOn(event.getCreatedOn().format(DateTimeFormat.formatter))
                .location(event.getLocation())
                .requestModeration(event.getRequestModeration())
                .publishedOn(publishedOn)
                .confirmedRequests(event.getConfirmedRequests())
                .build();
    }

    public static List<EventFullDto> toEventFullDtoList(List<Event> eventList) {
        return eventList.stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
    }

    public static List<EventShortDto> toEventShortDtoList(List<Event> eventList) {
        return eventList.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }
}
