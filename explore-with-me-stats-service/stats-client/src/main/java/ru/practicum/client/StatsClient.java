package ru.practicum.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.dto.HitCreateDto;
import ru.practicum.dto.HitStatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class StatsClient {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final RestTemplate rest;

    @Autowired
    public StatsClient(@Value("${STATS_SERVER_URI:http://localhost:9090}") String uri, RestTemplateBuilder builder) {
        rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(uri))
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
            rest.postForEntity("/hit", body, Object.class);
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Can't add hit", e);
        }
    }

    public List<HitStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("/stats")
                .queryParam("start", start.format(DATE_TIME_FORMATTER))
                .queryParam("end", end.format(DATE_TIME_FORMATTER))
                .encode();

        if (uris != null) {
            builder.queryParam("uris", uris);
        }
        if (unique != null) {
            builder.queryParam("unique", unique);
        }
        builder.encode();

        try {
            return rest.exchange(builder.build(false).toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<List<HitStatsDto>>() {
            }).getBody();
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Can't get stats hit", e);
        }
    }
}
