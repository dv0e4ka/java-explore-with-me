package org.example.categoriy.service;

import org.example.categoriy.dto.CategoryDto;
import org.example.categoriy.model.Category;

import java.util.List;

public interface PublicCategoryService {

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategory(long catId);
}
