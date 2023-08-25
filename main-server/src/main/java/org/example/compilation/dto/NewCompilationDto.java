package org.example.compilation.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
public class NewCompilationDto {
    private Set<Long> events;
    private Boolean pinned;

    @Size(min = 1, max = 50, message = "поле title должно содержать от 1 до 50 символов")
    private String title;
}
