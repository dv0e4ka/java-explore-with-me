package org.example.compilation.util;

import lombok.experimental.UtilityClass;
import org.example.compilation.dto.CompilationDto;
import org.example.compilation.dto.NewCompilationDto;
import org.example.compilation.model.Compilation;
import org.example.event.model.Event;
import org.example.event.util.EventMapper;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {

    public static List<CompilationDto> toCompilationDtoList(List<Compilation> compilationList) {
        return compilationList.stream()
                .map(compilation -> CompilationDto.builder()
                        .id(compilation.getId())
                        .pinned(compilation.getPinned())
//                        .events(EventMapper.toEventShortDtoList(compilation.getEvents()))
                        .build())
                .collect(Collectors.toList());
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
//                .events(EventMapper.toEventShortDtoList(compilation.getEvents()))
                .build();
    }

    public static Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> events) {
        return Compilation.builder()
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
//                .events(events)
                .build();
    }
}
