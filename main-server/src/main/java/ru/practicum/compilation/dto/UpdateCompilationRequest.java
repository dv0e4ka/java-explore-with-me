package ru.practicum.compilation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Builder
public class UpdateCompilationRequest {
    List<Long> events;
    Boolean pinned;

    @Size(min = 1, max = 50, message = "поле title должно содержать от 1 до 50 символов")
    String title;
}
