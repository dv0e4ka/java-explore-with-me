package org.example.categoriy.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.categoriy.dto.CategoryDto;
import org.example.categoriy.dto.NewCategoryDto;
import org.example.categoriy.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto save(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("пришел запрос на добавление категории: {}", newCategoryDto.getName());
        return categoryService.save(newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long catId) {
        log.info("пришел запрос на удалении категории: id={}", catId);
        categoryService.delete(catId);
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto patch(@PathVariable Long catId, @Valid @RequestBody CategoryDto categoryDto) {
        log.info("пришел запрос на патч категории: id={}, на новое имя={}", catId, categoryDto.getName());
        return categoryService.patch(catId, categoryDto);
    }
}


