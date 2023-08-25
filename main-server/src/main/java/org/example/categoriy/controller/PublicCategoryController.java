package org.example.categoriy.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.categoriy.dto.CategoryDto;
import org.example.categoriy.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PublicCategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> findAll(@PositiveOrZero @RequestParam (defaultValue = "0") int from,
                                     @Positive @RequestParam (defaultValue = "10") int size) {
        log.info("получен запрос на получение категорий");
        return categoryService.findAll(from, size);
    }

    @GetMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto findById(@PathVariable long catId) {
        log.info("получен запрос на получение категории id={}", catId);
        return categoryService.findById(catId);
    }
}
