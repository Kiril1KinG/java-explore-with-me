package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.HitCreateDto;
import ru.practicum.dto.HitStatsDto;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.service.StatsService;

import javax.validation.Valid;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {

    private static final DateTimeFormatter DATE_TIME_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault());

    private final StatsService service;

    private final StatsMapper mapper;


    @PostMapping("/hit")
    public HitStatsDto addHit(@RequestBody @Valid HitCreateDto request) {
        log.info("/hit");
        return mapper.toHitStats(service.addHit(mapper.toEntity(request)));
    }

    @GetMapping("/stats")
    public List<HitStatsDto> getStats(@RequestParam String start,
                                      @RequestParam String end,
                                      @RequestParam(required = false) List<String> uris,
                                      @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        log.info("/stats");
        return service.getStats(Instant.from(DATE_TIME_PATTERN.parse(start)), Instant.from(DATE_TIME_PATTERN.parse(end)),
                uris, unique).stream()
                .map(mapper::toHitStats)
                .collect(Collectors.toList());
    }
}