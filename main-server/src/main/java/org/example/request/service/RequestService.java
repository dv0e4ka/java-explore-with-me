package org.example.request.service;

import org.example.request.dto.EventRequestStatusUpdateRequest;
import org.example.request.dto.EventRequestStatusUpdateResult;
import org.example.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    ParticipationRequestDto saveUserRequest(long userId, long eventId);

    ParticipationRequestDto patch(long userId, long requestId);

    List<ParticipationRequestDto> getUserRequests(long userId);

    EventRequestStatusUpdateResult patchUserEventUpdateRequests(EventRequestStatusUpdateRequest updateEventDto,
                                                                long userId, long eventId);

    List<ParticipationRequestDto> getUserEventRequests(long userId, long eventId);
}
