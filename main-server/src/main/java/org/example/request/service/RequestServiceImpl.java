package org.example.request.service;

import lombok.RequiredArgsConstructor;
import org.example.request.dto.ParticipationRequestDto;
import org.example.request.repository.RequestRepository;
import org.springframework.stereotype.Service;

//TODO: заполнить сервис

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;

    @Override
    public ParticipationRequestDto get(long userId) {
        return null;
    }

    @Override
    public ParticipationRequestDto save(long userId, long eventId) {
        return null;
    }

    @Override
    public ParticipationRequestDto patch(long userId, long requestId) {
        return null;
    }
}
