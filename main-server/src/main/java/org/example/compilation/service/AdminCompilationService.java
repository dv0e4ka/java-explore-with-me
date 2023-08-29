package org.example.compilation.service;

import org.example.compilation.dto.CompilationDto;
import org.example.compilation.dto.NewCompilationDto;

public interface AdminCompilationService {

    CompilationDto save(NewCompilationDto compilationDto);

    void delete(long compId);

    CompilationDto patch(long compId, CompilationDto compilationDto);
}
