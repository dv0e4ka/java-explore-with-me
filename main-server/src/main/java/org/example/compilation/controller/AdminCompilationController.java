package org.example.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.compilation.dto.CompilationDto;
import org.example.compilation.dto.NewCompilationDto;
import org.example.compilation.service.CompilationService;
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

    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto save(@Valid NewCompilationDto newCompilationDto) {
        log.info("получен запрос добавление новой подборки событий: {}", newCompilationDto.getTitle());
        return compilationService.save(newCompilationDto);
    }

    @PostMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long compId) {
        log.info("получен запрос на удаление подборки событий id={}", compId);
        compilationService.delete(compId);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto patch(@PathVariable long compId, CompilationDto compilationDto) {
        log.info("получен запрос на патч подборки событий id={}",compilationDto.getId());
        return compilationService.patch(compId, compilationDto);
    }
}
