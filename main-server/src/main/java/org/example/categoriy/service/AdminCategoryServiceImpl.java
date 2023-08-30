package org.example.categoriy.service;

import lombok.RequiredArgsConstructor;
import org.example.categoriy.dto.CategoryDto;
import org.example.categoriy.dto.NewCategoryDto;
import org.example.categoriy.model.Category;
import org.example.categoriy.repository.CategoryRepository;
import org.example.categoriy.util.CategoryMapper;
import org.example.exception.model.EntityNoFoundException;
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
        //    TODO: проверить удаление при наличии связаных событий(нужен каскад делейт)
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
