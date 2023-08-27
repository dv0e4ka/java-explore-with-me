package org.example.exception.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ApiError {
    private List<String> errors;
    private String message;
    private String reason;
    // TODO тут нужен код статуса (?) или что вообще требуется?
    private String status;
    private LocalDateTime timestamp;
}
