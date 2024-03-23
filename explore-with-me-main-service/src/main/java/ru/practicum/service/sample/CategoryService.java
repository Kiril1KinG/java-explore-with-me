package ru.practicum.service.sample;


import ru.practicum.exception.DataNotFoundException;
import ru.practicum.model.entity.Category;

import java.util.Collection;

public interface CategoryService {

    Category addCategory(Category category);

    void deleteCategory(Long categoryId);

    Category updateCategory(Category category) throws DataNotFoundException;

    Collection<Category> getCategories(Integer from, Integer size);

    Category getCategoryById(Long catId);
}
