package org.example.modelForRefacting;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.request.dto.ParticipationRequestDto;

@Getter
@Setter
@Builder
public class EventRequestStatusUpdateResult {
    ParticipationRequestDto confirmedRequests;
    ParticipationRequestDto rejectRequests;
}
