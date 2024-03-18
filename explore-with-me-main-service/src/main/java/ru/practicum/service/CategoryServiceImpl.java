package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository repo;

    @Override
    public Category addCategory(Category category) {
        return repo.save(category);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        repo.deleteById(categoryId);
    }

    @Override
    public Category updateCategory(Category category) {
        Category categoryEntity = repo.findById(category.getId()).orElseThrow(
                () -> new DataNotFoundException(
                        String.format("Category with id %d not found", category.getId())));
        if (category.getName() != null) {
            categoryEntity.setName(category.getName());
        }
        return repo.save(categoryEntity);
    }
    @Override
    public Collection<Category> getCategories(Integer from, Integer size) {
        return repo.findAll(PageRequest.of(from / size, size, Sort.by("id"))).getContent();
    }

    @Override
    public Category getCategoryById(Long catId) {
        return repo.findById(catId).orElseThrow(
                () -> new DataNotFoundException(
                        String.format("Category with id %d not found", catId)));
    }
}
