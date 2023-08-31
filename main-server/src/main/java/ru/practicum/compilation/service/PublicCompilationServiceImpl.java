package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.exception.model.EntityNoFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicCompilationServiceImpl implements PublicCompilationService {
    private final CompilationRepository compilationRepository;

    @Override
    public List<CompilationDto> findAll(Boolean pinned, int from, int size) {
        PageRequest page = PageRequest.of(from, size);
        Page<Compilation> compilationPage;
        if (pinned != null) {
            compilationPage = compilationRepository.findAllByPinned(pinned, page);
        } else {
            compilationPage = compilationRepository.findAll(page);
        }

        List<Compilation> compilationList = compilationPage.getContent();
        return CompilationMapper.toCompilationDtoList(compilationList);
    }

    @Override
    public CompilationDto findById(long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new EntityNoFoundException("подборка событий по id=" + compId + " не найдено"));
        return CompilationMapper.toCompilationDto(compilation);
    }
}
