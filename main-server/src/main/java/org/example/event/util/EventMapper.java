package org.example.event.util;

import lombok.experimental.UtilityClass;
import org.example.categoriy.model.Category;
import org.example.categoriy.util.CategoryMapper;
import org.example.event.dto.EventFullDto;
import org.example.event.dto.EventShortDto;
import org.example.event.dto.NewEventDto;
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
                .build();
    }


    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .views(event.getViews())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .eventDate(event.getEventDate().format(DateTimeFormat.formatter))
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();
    }


    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .build();
    }

    public static List<EventShortDto> toEventShortDtoList(List<Event> eventList) {
        return eventList.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }
}
