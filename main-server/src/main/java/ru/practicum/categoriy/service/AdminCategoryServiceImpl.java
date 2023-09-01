package ru.practicum.categoriy.service;

import lombok.RequiredArgsConstructor;
import ru.practicum.categoriy.dto.CategoryDto;
import ru.practicum.categoriy.dto.NewCategoryDto;
import ru.practicum.categoriy.model.Category;
import ru.practicum.categoriy.repository.CategoryRepository;
import ru.practicum.categoriy.util.CategoryMapper;
import ru.practicum.exception.model.EntityNoFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class AdminCategoryServiceImpl implements AdminCategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public CategoryDto addCategory(@Valid NewCategoryDto newCategoryDto) {
        Category categoryToSave = CategoryMapper.toCategory(newCategoryDto);
        Category categorySaved = categoryRepository.save(categoryToSave);
        return CategoryMapper.toCategoryDto(categorySaved);
    }

    @Transactional
    @Override
    public void deleteCategory(long catId) {
        findCatById(catId);
        categoryRepository.deleteById(catId);
    }

    @Transactional
    @Override
    public CategoryDto updateCategory(long catId, CategoryDto categoryDto) {
        findCatById(catId);

        Category categoryToPatch = CategoryMapper.toCategory(categoryDto);
        categoryToPatch.setId(catId);

        return CategoryMapper.toCategoryDto(categoryRepository.save(categoryToPatch));
    }

    private Category findCatById(long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(
                        () -> new EntityNoFoundException(
                                String.format("категория id=%d не найдена", catId)
                        )
                );
    }
}
