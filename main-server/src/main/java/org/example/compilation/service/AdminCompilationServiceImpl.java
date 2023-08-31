package org.example.compilation.service;

import lombok.RequiredArgsConstructor;
import org.example.compilation.dto.CompilationDto;
import org.example.compilation.dto.NewCompilationDto;
import org.example.compilation.dto.UpdateCompilationRequest;
import org.example.compilation.model.Compilation;
import org.example.compilation.repository.CompilationRepository;
import org.example.compilation.mapper.CompilationMapper;
import org.example.event.model.Event;
import org.example.event.repository.EventRepository;
import org.example.exception.model.EntityNoFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCompilationServiceImpl implements AdminCompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CompilationDto saveCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = new ArrayList<>();
        if (newCompilationDto.getEvents() != null) {
            events = eventRepository.findByIdIn(newCompilationDto.getEvents());
        }
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto, events);
        Compilation compilationSaved = compilationRepository.save(compilation);
        return CompilationMapper.toCompilationDto(compilationSaved);
    }

    @Transactional
    @Override
    public void deleteCompilation(long compId) {
        compilationRepository.findById(compId).orElseThrow(
                () -> new EntityNoFoundException(
                        "подборка событий по id=" + compId + "  предназначенная на удаление не найдено"
                )
        );

        compilationRepository.deleteById(compId);
    }

    @Transactional
    @Override
    public CompilationDto updateCompilation(long compId, UpdateCompilationRequest updateCompilationRequest) {
        List<Event> events = new ArrayList<>();
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new EntityNoFoundException(String.format("подборка событий id=%d не найдена", compId))
        );

        List<Long> compIds = updateCompilationRequest.getEvents();

        if (compIds != null) {
            events = eventRepository.findByIdIn(compIds);
            if (events.size() != compIds.size()) {
                throw new EntityNoFoundException(String.format("указаны не существующие события", compId));
            }
            compilation.setEvents(events);
        }
        updateCompilationField(compilation, updateCompilationRequest);

        Compilation compilationSaved = compilationRepository.save(compilation);
        return CompilationMapper.toCompilationDto(compilationSaved);
    }


    private void updateCompilationField(Compilation compilation, UpdateCompilationRequest compilationRequest) {

        if (compilationRequest.getPinned() != null) {
            compilation.setPinned(compilationRequest.getPinned());
        }

        if (compilationRequest.getTitle() != null) {
            compilation.setTitle(compilationRequest.getTitle());
        }
    }
}
