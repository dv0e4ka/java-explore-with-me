package org.example.event.service;

import lombok.RequiredArgsConstructor;
import org.example.categoriy.model.Category;
import org.example.categoriy.repository.CategoryRepository;
import org.example.enums.AdminStateAction;
import org.example.enums.State;
import org.example.event.dto.EventFullDto;
import org.example.event.dto.UpdateEventAdminRequest;
import org.example.event.mapper.EventMapper;
import org.example.event.model.Event;
import org.example.event.repository.EventRepository;
import org.example.exception.model.DateTimeEventException;
import org.example.exception.model.EntityNotFoundException;
import org.example.exception.model.PatchEventStateException;
import org.example.location.Location;
import org.example.location.LocationRepository;
import org.example.user.model.User;
import org.example.user.repository.UserRepository;
import org.example.util.DateTimeFormat;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;


    @Transactional(readOnly = true)
    @Override
    public List<EventFullDto> findEventsFull(List<Long> userIds,
                                             List<String> states,
                                             List<Long> categoryIds,
                                             String rangeStart,
                                             String rangeEnd,
                                             int from,
                                             int size) {

        if (userIds != null) {
            List<User> userList = userRepository.findAllByIdIn(userIds);
            if (userList.size() != userIds.size()) {
                throw new EntityNotFoundException("введены не добавленные пользователи");
            }
        }

        if (categoryIds != null) {
            List<Category> categoryList = categoryRepository.findAllByIdIn(categoryIds);

            if (categoryList.size() != categoryIds.size()) {
                throw new EntityNotFoundException("введены не добавленные категории");
            }
        }


        LocalDateTime start;
        LocalDateTime end;

        if (rangeStart != null) {
            start = LocalDateTime.parse(rangeStart, DateTimeFormat.formatter);
        } else {
            start = LocalDateTime.now();
        }
        if (rangeEnd != null) {
            end = LocalDateTime.parse(rangeEnd, DateTimeFormat.formatter);
        } else {
//            TODO изменить наверное до 1 года
            end = LocalDateTime.now().plusYears(2);
        }


        PageRequest page = PageRequest.of(from, size);

        List<Event> eventList = eventRepository.findAllByParam(userIds, states, categoryIds, start, end, page);
        System.out.println(eventList.size());
        return EventMapper.toEventFullDtoList(eventList);
    }



    @Transactional
    @Override
    public EventFullDto updateEventByAdmin(long eventId, UpdateEventAdminRequest updateEvent) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException(String.format("событие id=%d не найдено", eventId))
        );


        AdminStateAction stateAction = updateEvent.getStateAction();
        if (stateAction != null && stateAction.equals(AdminStateAction.REJECT_EVENT)
                && event.getState().equals(State.PUBLISHED)) {
            throw new PatchEventStateException("событие можно отклонить, только если оно еще не опубликовано");
        }

        if (!event.getState().equals(State.PENDING)) {
            throw new PatchEventStateException(
                    String.format(
                            "событие id=%d можно публиковать, то если оно в состоянии ожидидания публикации", eventId)
            );
        }

        if (stateAction != null) {
            switch (stateAction) {
                case REJECT_EVENT:
                    event.setState(State.CANCELED);
                    break;
                case PUBLISH_EVENT:
                    event.setState(State.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
            }
        }

        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }

        Long categoryId = updateEvent.getCategory();
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId).orElseThrow(
                    () -> new EntityNotFoundException(String.format("категория id=%d не найдена", categoryId))
            );

            event.setCategory(category);
        }

        if (updateEvent.getEventDate() != null) {
            LocalDateTime eventDate = LocalDateTime.parse(updateEvent.getEventDate(), DateTimeFormat.formatter);
            if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
                throw new DateTimeEventException("Событие не удовлетворяет правилам создания, " +
                        "время создания ивента должно быть позже текущего на 2 часа");
            }
            event.setEventDate(eventDate);
        }

        if (updateEvent.getLocation() != null) {
            float lat = updateEvent.getLocation().getLat();
            float lon =  updateEvent.getLocation().getLon();
            Location location = locationRepository.findByLatAndLon(lat, lon).stream().findAny().orElse(
                    locationRepository.save(Location.builder().lat(lat).lon(lon).build())
            );

            event.setLocation(location);
        }

        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }

        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }

        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }

        String title = updateEvent.getTitle();
        if (title != null) {
            event.setTitle(title);
        }

        Event eventUpdated = eventRepository.save(event);

        return EventMapper.toEventFullDto(eventUpdated);
    }
}
