package org.example.compilation.service;

import org.example.compilation.dto.CompilationDto;
import org.example.compilation.dto.NewCompilationDto;
import org.example.compilation.dto.UpdateCompilationRequest;

public interface AdminCompilationService {

    CompilationDto saveCompilation(NewCompilationDto compilationDto);

    void deleteCompilation(long compId);

    CompilationDto updateCompilation(long compId, UpdateCompilationRequest updateCompilationRequest);
}
