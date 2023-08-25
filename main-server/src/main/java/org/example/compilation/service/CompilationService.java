package org.example.compilation.service;

import org.example.compilation.dto.CompilationDto;
import org.example.compilation.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {

    List<CompilationDto> findAll(Boolean pinned, int from, int size);

    CompilationDto findById(long compId);

    CompilationDto save(NewCompilationDto newCompilationDto);

    void delete(long compId);

    CompilationDto patch(long compId, CompilationDto compilationDto);
}
