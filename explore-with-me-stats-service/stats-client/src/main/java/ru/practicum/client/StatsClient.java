package ru.practicum.client;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.HitCreateDto;
import ru.practicum.dto.HitStatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
public class StatsClient {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final RestTemplate rest = new RestTemplate();

    public StatsClient(RestTemplateBuilder builder) {
        builder
                .uriTemplateHandler(new DefaultUriBuilderFactory("http://localhost:9090"))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public void addHit(String app, String uri, String ip) {
        HitCreateDto body = new HitCreateDto();
        body.setApp(app);
        body.setUri(uri);
        body.setIp(ip);
        body.setTimestamp(LocalDateTime.now().format(DATE_TIME_FORMATTER));
        try {
            rest.postForEntity("/hit", body, null);
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Can't add hit", e);
        }
    }

    public HitStatsDto getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        Map<String, Object> params = Map.of(
                "start", start.format(DATE_TIME_FORMATTER),
                "end", end.format(DATE_TIME_FORMATTER),
                "uris", uris,
                "unique", unique
        );
        try {
            return rest.getForEntity("/stats", HitStatsDto.class, params).getBody();
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Can't get stats hit", e);
        }
    }
}
