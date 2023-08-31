package org.example.categoriy.repository;

import org.example.categoriy.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByIdIn(List<Long> ids);
}
