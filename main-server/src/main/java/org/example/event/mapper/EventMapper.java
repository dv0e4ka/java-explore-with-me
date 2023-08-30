package org.example.event.mapper;

import lombok.experimental.UtilityClass;
import org.example.categoriy.model.Category;
import org.example.categoriy.util.CategoryMapper;
import org.example.enums.State;
import org.example.event.dto.*;
import org.example.event.model.Event;
import org.example.location.Location;
import org.example.user.model.User;
import org.example.user.util.UserMapper;
import org.example.util.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// TODO заполнить маппер
@UtilityClass
public class EventMapper {

    public static Event toEvent(EventFullDto eventFullDto) {
        return Event.builder()
                .build();
    }


    public static Event toEvent(EventShortDto eventShortDto) {
        return Event.builder()
                .build();
    }



    public static Event toEvent(NewEventDto newEventDto, User requester, Location location, Category category) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .description(newEventDto.getDescription())
                .title(newEventDto.getTitle())
                .eventDate(LocalDateTime.parse(newEventDto.getEventDate(), DateTimeFormat.formatter))
                .location(location)
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .initiator(requester)
                .createdOn(LocalDateTime.now())
                .state(State.PENDING)
                .build();
    }

    public static Event toEvent(UpdateEventUserRequest eventDto) {
        Event event = Event.builder()
                .annotation(eventDto.getAnnotation())
                .description(eventDto.getDescription())
                .paid(eventDto.getPaid())
                .participantLimit(eventDto.getParticipantLimit())
                .requestModeration(eventDto.getRequestModeration())
                .title(eventDto.getTitle())
                .location(eventDto.getLocation())
                .build();

        if (event.getEventDate() != null) {
                LocalDateTime eventDate =  LocalDateTime.parse(eventDto.getEventDate(), DateTimeFormat.formatter);
                event.setEventDate(eventDate);
        }

        if (event.getCategory() != null) {
            event.setCategory(Category.builder().id(eventDto.getCategory()).build());
        }
        return event;
    }


    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
//                TODO views
//                .views(event.getViews())
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
        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .paid(event.getPaid())
                .eventDate(event.getEventDate().format(DateTimeFormat.formatter))
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .description(event.getDescription())
                .participantLimit(event.getParticipantLimit())
                .state(event.getState().toString())
                .createdOn(event.getCreatedOn().format(DateTimeFormat.formatter))
                .location(event.getLocation())
                .requestModeration(event.getRequestModeration())
                .build();
    }

    public static List<EventFullDto> toEventFullDtoList(List<Event> eventList) {
        return eventList.stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
    }

//    TODO: включать сюда еще статистику просмотров
//     например: публичный запрос событий по параметрам требует
    public static List<EventShortDto> toEventShortDtoList(List<Event> eventList) {
        return eventList.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }
}
