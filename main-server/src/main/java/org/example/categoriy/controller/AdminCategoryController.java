package org.example.categoriy.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.categoriy.dto.CategoryDto;
import org.example.categoriy.dto.NewCategoryDto;
import org.example.categoriy.service.AdminCategoryService;
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
    private final AdminCategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("пришел запрос на добавление категории: {}", newCategoryDto.getName());
        return categoryService.addCategory(newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable long catId) {
        log.info("пришел запрос на удалении категории: id={}", catId);
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto updateCategory(@PathVariable Long catId, @Valid @RequestBody CategoryDto categoryDto) {
        log.info("пришел запрос на патч категории: id={}, на новое имя={}", catId, categoryDto.getName());
        return categoryService.updateCategory(catId, categoryDto);
    }
}


