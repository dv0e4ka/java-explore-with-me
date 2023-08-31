package org.example.request.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.RequestStatus;

import java.util.List;

@Getter
@Setter
@Builder
public class EventRequestStatusUpdateRequest {
    List<Long> requestIds;
    RequestStatus status;
}
