package org.example.compilation.util;

import lombok.experimental.UtilityClass;
import org.example.compilation.dto.CompilationDto;
import org.example.compilation.dto.NewCompilationDto;
import org.example.compilation.dto.UpdateCompilationRequest;
import org.example.compilation.model.Compilation;
import org.example.event.dto.EventShortDto;
import org.example.event.mapper.EventMapper;
import org.example.event.model.Event;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {

    public static Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> events) {
        return Compilation.builder()
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .events(events)
                .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .events(EventMapper.toEventShortDtoList(compilation.getEvents()))
                .title(compilation.getTitle())
                .build();
    }

    public static List<CompilationDto> toCompilationDtoList(List<Compilation> compilationList) {
        return compilationList.stream()
                .map(compilation -> CompilationDto.builder()
                        .id(compilation.getId())
                        .pinned(compilation.getPinned())
                        .events(EventMapper.toEventShortDtoList(compilation.getEvents()))
                        .title(compilation.getTitle())
                        .build())
                .collect(Collectors.toList());
    }
}
