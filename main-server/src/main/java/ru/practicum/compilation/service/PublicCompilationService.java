package org.example.compilation.service;

import org.example.compilation.dto.CompilationDto;

import java.util.List;

public interface PublicCompilationService {

    List<CompilationDto> findAll(Boolean pinned, int from, int size);

    CompilationDto findById(long compId);
}
