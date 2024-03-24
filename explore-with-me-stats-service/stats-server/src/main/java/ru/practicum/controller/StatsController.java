package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.HitCreateDto;
import ru.practicum.dto.HitStatsDto;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.service.StatsService;
import ru.practicum.validation.DateTimeValidator;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.validation.DateTimeValidator.DATE_TIME_PATTERN;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {

    private final StatsService service;
    private final StatsMapper mapper;
    private final DateTimeValidator dateTimeValidator;

    @PostMapping("/hit")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void addHit(@RequestBody @Valid HitCreateDto request) {
        log.info("/hit - Dto:{}", request);
        service.addHit(mapper.toEntity(request));
    }

    @GetMapping("/stats")
    public List<HitStatsDto> getStats(@RequestParam @DateTimeFormat(pattern = DATE_TIME_PATTERN, fallbackPatterns = "yyyy-MM-dd%20HH:mm:ss") LocalDateTime start,
                                      @RequestParam @DateTimeFormat(pattern = DATE_TIME_PATTERN, fallbackPatterns = "yyyy-MM-dd%20HH:mm:ss") LocalDateTime end,
                                      @RequestParam(required = false) List<String> uris,
                                      @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        dateTimeValidator.validate(start, end);
        log.info("GET /stats - start:{}, end:{}, uris:{}, unique:{}", start, end, uris, unique);
        return service.getStats(start, end, uris, unique).stream()
                .map(mapper::toHitStats)
                .collect(Collectors.toList());
    }
}