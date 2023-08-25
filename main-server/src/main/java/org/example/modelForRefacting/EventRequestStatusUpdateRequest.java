package org.example.modelForRefacting;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class EventRequestStatusUpdateRequest {
    List<Long> requestIds;
    String status;
}
