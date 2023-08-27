package org.example.request.util;

import lombok.experimental.UtilityClass;
import org.example.enums.RequestStatus;
import org.example.event.model.Event;
import org.example.request.dto.EventRequestStatusUpdateResult;
import org.example.request.dto.ParticipationRequestDto;
import org.example.request.model.ParticipationRequest;
import org.example.user.model.User;
import org.example.util.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class RequestMapper {

    public static ParticipationRequest toRequest(Event event, User user, RequestStatus status) {
        return ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(user)
                .status(status)
                .build();
    }

    public static ParticipationRequestDto toRequestDto(ParticipationRequest request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .created(request.getCreated().format(DateTimeFormat.formatter))
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }

    public static List<ParticipationRequestDto> toRequestDtoList(List<ParticipationRequest> requests) {
        return requests.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    public static EventRequestStatusUpdateResult toEventRequestStatusUpdateResult(
            List<ParticipationRequest> confirmedRequests, List<ParticipationRequest> rejectRequests) {

        List<ParticipationRequestDto> confirmedRequestsDtoList = confirmedRequests.stream()
                .map(RequestMapper::toRequestDto).collect(Collectors.toList());

        List<ParticipationRequestDto> rejectRequestsDtoList = rejectRequests.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());

        return new EventRequestStatusUpdateResult(confirmedRequestsDtoList, rejectRequestsDtoList);
    }
}
