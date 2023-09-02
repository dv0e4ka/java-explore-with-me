package ru.practicum.categoriy.service;

import ru.practicum.categoriy.dto.CategoryDto;
import ru.practicum.categoriy.dto.NewCategoryDto;

public interface AdminCategoryService {

    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(long catId);

    CategoryDto updateCategory(long catId, CategoryDto categoryDto);
}
