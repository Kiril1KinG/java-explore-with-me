package ru.practicum.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.CompilationDto;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.service.sample.CompilationService;

import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/compilations")
@Validated
@Slf4j
@RequiredArgsConstructor
public class PublicCompilationController {

    private final CompilationService compilationService;
    private final CompilationMapper compilationMapper;

    @GetMapping
    public Collection<CompilationDto> getCompilationsByFilters(@RequestParam(required = false) Boolean pinned,
                                                               @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                                               @RequestParam(required = false, defaultValue = "10") @Min(1) Integer size) {
        log.info("GET /compilations");
        return compilationService.getCompilationsByFilters(pinned, from, size).stream()
                .map(compilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable @Min(1) Long compId) {
        log.info("GET /compilations/{}", compId);
        return compilationMapper.toCompilationDto(compilationService.getCompilationById(compId));
    }
}
