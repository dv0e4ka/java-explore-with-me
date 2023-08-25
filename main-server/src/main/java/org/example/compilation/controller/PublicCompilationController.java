package org.example.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.compilation.dto.CompilationDto;
import org.example.compilation.service.CompilationService;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Slf4j
public class PublicCompilationController {
    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> findAll(@RequestParam (required = false) Boolean pinned,
                                        @RequestParam (defaultValue = "0") @PositiveOrZero int from,
                                        @RequestParam (defaultValue = "10") @Positive int size) {
        log.info("получен запрос на получение подборок событий");
        return compilationService.findAll(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto findById (@PathVariable long compId) {
        log.info("получен запрос на получение подборки события по id={}", compId);
        return compilationService.findById(compId);
    }
}
