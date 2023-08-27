package org.example.categoriy.service;

import lombok.RequiredArgsConstructor;
import org.example.categoriy.dto.CategoryDto;
import org.example.categoriy.dto.NewCategoryDto;
import org.example.categoriy.model.Category;
import org.example.categoriy.repository.CategoryRepository;
import org.example.categoriy.util.CategoryMapper;
import org.example.exception.model.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public CategoryDto save(@Valid NewCategoryDto newCategoryDto) {
        Category categoryToSave = CategoryMapper.toCategory(newCategoryDto);
        Category categorySaved = categoryRepository.save(categoryToSave);
        return CategoryMapper.toCategoryDto(categorySaved);
    }

//    TODO: проверить удаление при наличии связаных событий
    @Transactional
    @Override
    public void delete(long catId) {
        findCatById(catId);
        categoryRepository.deleteById(catId);
    }

    @Transactional
    @Override
    public CategoryDto patch(long catId, CategoryDto categoryDto) {
        findCatById(catId);

        Category categoryToPatch = CategoryMapper.toCategory(categoryDto);
        categoryToPatch.setId(catId);

        return CategoryMapper.toCategoryDto(categoryRepository.save(categoryToPatch));
    }

    @Override
    public List<CategoryDto> findAll(int from, int size) {
        PageRequest page = PageRequest.of(from, size);
        List<Category> categoryList = categoryRepository.findAll(page).getContent();
        return CategoryMapper.toCategoryDtoList(categoryList);
    }

    @Override
    public CategoryDto findById(long catId) {
        return CategoryMapper.toCategoryDto(findCatById(catId));
    }

    private Category findCatById(long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                String.format("категория id=%d не найдена", catId)
                        )
                );
    }
}
