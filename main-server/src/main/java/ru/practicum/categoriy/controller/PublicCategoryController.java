package ru.practicum.categoriy.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.categoriy.dto.CategoryDto;
import ru.practicum.categoriy.service.PublicCategoryService;
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
    private final PublicCategoryService categoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getCategories(@PositiveOrZero @RequestParam (defaultValue = "0") int from,
                                     @Positive @RequestParam (defaultValue = "10") int size) {
        log.info("получен запрос на получение категорий");
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getCategory(@PathVariable long catId) {
        log.info("получен запрос на получение категории id={}", catId);
        return categoryService.getCategory(catId);
    }
}
