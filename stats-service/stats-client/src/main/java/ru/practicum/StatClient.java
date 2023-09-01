package ru.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.EndpointHitDto;

import java.util.List;
import java.util.Map;

@Service
public class StatClient extends BaseClient {

    @Autowired
    public StatClient(@Value("${stats-server.url}") String statsServiceUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(statsServiceUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
    }

    public ResponseEntity<Object> add(EndpointHitDto endpointHitDto) {
        return post("/hit", endpointHitDto);
    }

    public ResponseEntity<Object> getStats(String start, String end, List<String> uris, Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique);
        return get("/stats/?start={start}&end={end}&uris={uris}&unique={unique}", parameters, null);
    }

    public ResponseEntity<Object> getStats(String start, String end, Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "unique", unique);
        return get("/stats/?start={start}&end={end}&unique={unique}", parameters, null);
    }

    public ResponseEntity<Object> getStats(String start, String end, List<String> uris) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris);
        return get("/stats/?start={start}&end={end}&uris={uris}", parameters, null);
    }

    public ResponseEntity<Object> getStats(String start, String end) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end);
        return get("/stats/?start={start}&end={end}", parameters, null);
    }
}
