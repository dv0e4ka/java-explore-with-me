package org.example.categoriy.service;

import org.example.categoriy.dto.CategoryDto;
import org.example.categoriy.dto.NewCategoryDto;

public interface AdminCategoryService {

    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(long catId);

    CategoryDto updateCategory(long catId, CategoryDto categoryDto);
}
