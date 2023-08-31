package ru.practicum.categoriy.service;

import ru.practicum.categoriy.dto.CategoryDto;

import java.util.List;

public interface PublicCategoryService {

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategory(long catId);
}
