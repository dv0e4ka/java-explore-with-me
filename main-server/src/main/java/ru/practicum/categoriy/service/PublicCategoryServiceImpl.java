package ru.practicum.categoriy.service;

import lombok.RequiredArgsConstructor;
import ru.practicum.categoriy.dto.CategoryDto;
import ru.practicum.categoriy.model.Category;
import ru.practicum.categoriy.repository.CategoryRepository;
import ru.practicum.categoriy.util.CategoryMapper;
import ru.practicum.exception.model.EntityNoFoundException;
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
                        () -> new EntityNoFoundException(
                                String.format("категория id=%d не найдена", catId)
                        )
                );
        return CategoryMapper.toCategoryDto(category);
    }
}
