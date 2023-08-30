package org.example.compilation.service;

import lombok.RequiredArgsConstructor;
import org.example.compilation.dto.CompilationDto;
import org.example.compilation.model.Compilation;
import org.example.compilation.repository.CompilationRepository;
import org.example.compilation.util.CompilationMapper;
import org.example.exception.model.EntityNoFoundException;
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

//     TODO: разобраться с полем ивенты к компиляции
    @Override
    public CompilationDto findById(long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new EntityNoFoundException("подборка событий по id=" + compId + " не найдено"));
        return CompilationMapper.toCompilationDto(compilation);
    }
}
