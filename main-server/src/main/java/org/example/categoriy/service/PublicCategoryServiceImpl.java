package org.example.categoriy.service;

import lombok.RequiredArgsConstructor;
import org.example.categoriy.dto.CategoryDto;
import org.example.categoriy.model.Category;
import org.example.categoriy.repository.CategoryRepository;
import org.example.categoriy.util.CategoryMapper;
import org.example.exception.model.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicCategoryServiceImpl implements PublicCategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        PageRequest page = PageRequest.of(from, size);
        List<Category> categoryList = categoryRepository.findAll(page).getContent();
        return CategoryMapper.toCategoryDtoList(categoryList);
    }

    @Override
    public CategoryDto getCategory(long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                String.format("категория id=%d не найдена", catId)
                        )
                );
        return CategoryMapper.toCategoryDto(category);
    }
}
