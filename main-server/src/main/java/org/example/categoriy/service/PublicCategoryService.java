package org.example.categoriy.service;

import org.example.categoriy.dto.CategoryDto;

import java.util.List;

public interface PublicCategoryService {

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategory(long catId);
}
