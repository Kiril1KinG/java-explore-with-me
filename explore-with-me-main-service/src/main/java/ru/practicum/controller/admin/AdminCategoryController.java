package ru.practicum.controller.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.service.sample.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/admin/categories")
@AllArgsConstructor
@Validated
@Slf4j
public class AdminCategoryController {

    private final CategoryService service;
    private final CategoryMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("POST /admin/categories");
        return mapper.toCategoryDto(service.addCategory(mapper.toCategory(newCategoryDto)));
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(@RequestBody @Valid CategoryDto categoryDto,
                                      @PathVariable @Min(1) Long catId) {
        log.info("PATCH /admin/categories/{}", catId);
        categoryDto.setId(catId);
        return mapper.toCategoryDto(service.addCategory(mapper.toCategory(categoryDto)));
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable @Min(1) Long catId) {
        log.info("DELETE /admin/categories/{}", catId);
        service.deleteCategory(catId);
    }

}
