package org.example.request.service;

import org.example.request.dto.ParticipationRequestDto;

public interface RequestService {

    ParticipationRequestDto get(long userId);

    ParticipationRequestDto save(long userId, long eventId);

    ParticipationRequestDto patch(long userId, long requestId);
}
