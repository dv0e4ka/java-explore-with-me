package org.example.categoriy.service;

import org.example.categoriy.dto.CategoryDto;
import org.example.categoriy.dto.NewCategoryDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

public interface CategoryService {

    public CategoryDto save(NewCategoryDto newCategoryDto);

    public void delete(long catId);

    public CategoryDto patch(long catId, CategoryDto categoryDto);

    public List<CategoryDto> findAll(int from, int size);

    public CategoryDto findById(long catId);
}
