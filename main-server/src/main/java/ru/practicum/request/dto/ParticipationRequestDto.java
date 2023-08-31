package org.example.request.dto;

import lombok.Builder;
import lombok.Data;
import org.example.enums.RequestStatus;

@Data
@Builder
public class ParticipationRequestDto {
    private long id;
    private String created;
    private long event;
    private long requester;
    private RequestStatus status;
}
