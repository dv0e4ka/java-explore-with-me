package org.example.event.service;

import lombok.RequiredArgsConstructor;
import org.example.categoriy.model.Category;
import org.example.categoriy.repository.CategoryRepository;
import org.example.event.dto.EventFullDto;
import org.example.event.dto.EventShortDto;
import org.example.event.dto.NewEventDto;
import org.example.event.model.Event;
import org.example.event.repository.EventRepository;
import org.example.event.util.EventMapper;
import org.example.location.Location;
import org.example.location.LocationRepository;
import org.example.user.model.User;
import org.example.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

// TODO: сделать сервис!

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;

    @Transactional
    @Override
    public EventShortDto save(long userId, NewEventDto newEventDto) {
        User requester = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("пользователь с id=" + userId + " не найден")
        );
        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(
                () -> new EntityNotFoundException("категория с id=" + userId + " не найдена")
        );
        Location location = locationRepository.save(newEventDto.getLocation());
        Event event = EventMapper.toEvent(newEventDto, requester, location, category);
        Event eventSaved = eventRepository.save(event);
        return EventMapper.toEventShortDto(eventSaved);
    }

    @Override
    public List<EventShortDto> getCurrUserEvents(long userId, int from, int size) {
        PageRequest page = PageRequest.of(from, size);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("пользователь с id=" + userId + " не найден")
        );
        List<Event> events = eventRepository.findByInitiator(userId, page);
        return EventMapper.toEventShortDtoList(events);
    }

    @Override
    public EventFullDto getEventFull(long userId, long eventId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("пользователь с id=" + userId + " не найден")
        );
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException("событие с id=" + userId + " не найдено")
        );
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventShortDto patch(long userId, long eventId) {
        return null;
    }

    @Override
    public EventShortDto getUserEventRequests(long userId, long eventId) {
        return null;
    }

    @Override
    public EventShortDto patchUserEventRequests(long userId, long eventId) {
        return null;
    }

    @Override
    public List<EventFullDto> findEventsFull(List<Long> users,
                                             List<String> state,
                                             List<Long> categories,
                                             String rangeStart,
                                             String rangeEnd,
                                             int from,
                                             int size) {
        return null;
    }

    @Override
    public EventFullDto findByIdFull(long id) {
        return null;
    }

    @Override
    public List<EventShortDto> findEventsShort(List<Long> users,
                                               List<String> state,
                                               List<Long> categories,
                                               String rangeStart,
                                               String rangeEnd,
                                               int from,
                                               int size) {
        return null;
    }

    @Override
    public EventShortDto patchById(long eventId, EventFullDto eventFullDto) {
        return null;
    }
}
