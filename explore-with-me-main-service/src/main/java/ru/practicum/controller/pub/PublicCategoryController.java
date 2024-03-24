package ru.practicum.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.CategoryDto;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.service.sample.CategoryService;

import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PublicCategoryController {

    private final CategoryMapper mapper;
    private final CategoryService service;

    @GetMapping
    public Collection<CategoryDto> getCategories(@RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                                 @RequestParam(required = false, defaultValue = "10") @Min(1) Integer size) {
        log.info("GET /categories");
        return service.getCategories(from, size).stream()
                .map(mapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable @Min(1) Long catId) {
        log.info("GET /categories/{}", catId);
        return mapper.toCategoryDto(service.getCategoryById(catId));
    }
}
