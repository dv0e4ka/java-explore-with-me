package org.example.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.compilation.dto.CompilationDto;
import org.example.compilation.dto.NewCompilationDto;
import org.example.compilation.dto.UpdateCompilationRequest;
import org.example.compilation.service.AdminCompilationService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdminCompilationController {

    private final AdminCompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto saveCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("получен запрос добавление новой подборки событий: {}", newCompilationDto.getTitle());
        return compilationService.saveCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable long compId) {
        log.info("получен запрос на удаление подборки событий id={}", compId);
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto updateCompilation(@PathVariable long compId,
                                            @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        log.info("получен запрос на патч подборки событий id={}", compId);
        return compilationService.updateCompilation(compId, updateCompilationRequest);
    }
}
