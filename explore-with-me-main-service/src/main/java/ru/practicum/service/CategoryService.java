package ru.practicum.service;


import ru.practicum.exception.DataNotFoundException;
import ru.practicum.model.Category;

public interface CategoryService {

    Category addCategory(Category category);

    void deleteCategory(Long categoryId);

    Category updateCategory(Category category) throws DataNotFoundException;
}
