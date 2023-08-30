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
    private Long id;

    @NotNull(message = "поле pinned не должно быть пустым")
    private Boolean pinned;

    @NotNull(message = "поле title не должно быть пустым")
    private String title;
}
