package org.example.compilation.service;

import lombok.RequiredArgsConstructor;
import org.example.compilation.dto.CompilationDto;
import org.example.compilation.dto.NewCompilationDto;
import org.example.compilation.model.Compilation;
import org.example.compilation.repository.CompilationRepository;
import org.example.compilation.util.CompilationMapper;
import org.example.event.dto.EventShortDto;
import org.example.event.model.Event;
import org.example.event.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminCompilationServiceImpl implements AdminCompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CompilationDto save(NewCompilationDto newCompilationDto) {
        List<Event> events = eventRepository.findByIdIn(newCompilationDto.getEvents());
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto, events);
        Compilation compilationSaved = compilationRepository.save(compilation);
        return CompilationMapper.toCompilationDto(compilationSaved);
    }

    @Transactional
    @Override
    public void delete(long compId) {
        compilationRepository.findById(compId).orElseThrow(
                () -> new EntityNotFoundException(
                        "подборка событий по id=" + compId + "  предназначенная на удаление не найдено"
                )
        );

        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto patch(long compId, CompilationDto compilationDto) {
        Set<Long> eventsIds = compilationDto.getEvents().stream()
                .map(EventShortDto::getId)
                .collect(Collectors.toSet());


        List<Event> events = eventRepository.findByIdIn(eventsIds);




        Compilation compilationFound = compilationRepository.findById(compId).orElseThrow(
                () -> new EntityNotFoundException(
                        "подборка событий по id=" + compId + "  предназначенная на удаление не найдено"
                )
        );

//        List<Event> eventsFound = compilationFound.getEvents();
//        List<Event> eventToPatch;

//        Set<Long>
//        Compilation patchedCompilation = CompilationMapper.toCompilation(compilationDto, eventsIds);
        //TODO: не доделал
        return null;
    }
}
