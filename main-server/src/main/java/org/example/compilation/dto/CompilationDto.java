package org.example.compilation.dto;

import lombok.Builder;
import lombok.Data;
import org.example.event.dto.EventShortDto;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class CompilationDto {
    private List<EventShortDto> events;

    @NotNull(message = "поле id должно быть пустым")
    private long id;

    @NotNull(message = "поле pinned должно быть пустым")
    private Boolean pinned;

    @NotNull(message = "поле title должно быть пустым")
    private String title;
}
