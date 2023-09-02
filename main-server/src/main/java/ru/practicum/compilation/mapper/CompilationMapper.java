package ru.practicum.compilation.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {

    public static Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> events) {
        boolean isPinned = false;
        if (newCompilationDto.getPinned() != null) {
            isPinned = newCompilationDto.getPinned();
        }
        return Compilation.builder()
                .pinned(isPinned)
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
