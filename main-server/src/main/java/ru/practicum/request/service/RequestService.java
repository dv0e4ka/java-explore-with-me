package ru.practicum.request.service;

import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    ParticipationRequestDto addParticipationRequest(long userId, long eventId);

    ParticipationRequestDto cancelRequest(long userId, long requestId);

    List<ParticipationRequestDto> getUserRequests(long userId);
}
