package ru.practicum.request.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.enums.RequestStatus;

import java.util.List;

@Getter
@Setter
@Builder
public class EventRequestStatusUpdateRequest {
    List<Long> requestIds;
    RequestStatus status;
}
