package org.example.event.service;

import lombok.RequiredArgsConstructor;
import org.example.event.dto.EventFullDto;
import org.example.event.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {
    private final EventRepository eventRepository;

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
}
