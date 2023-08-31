package ru.practicum.categoriy.repository;

import ru.practicum.categoriy.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByIdIn(List<Long> ids);
}
