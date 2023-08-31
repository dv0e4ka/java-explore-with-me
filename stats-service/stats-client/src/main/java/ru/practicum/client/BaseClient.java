package ru.practicum.client;


import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class BaseClient {
    protected final RestTemplate rest;

    public BaseClient(RestTemplate rest) {
        this.rest = rest;
    }

    protected <T> ResponseEntity<Object> post(String path, T body) {
        return post(path, null, body);
    }

    protected <T> ResponseEntity<Object> post(String path, Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.POST, path, null, body);
    }

    protected <T> ResponseEntity<Object> get(String path, Map<String, Object> parameters, T body) {
        return get(path, parameters);
    }

    protected ResponseEntity<Object> get(String path, Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.GET, path, parameters, null);
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, Map<String, Object> parameters, T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<Object> statsSvc;
        try {
            if (parameters != null) {
                statsSvc = rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                statsSvc = rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareStatsResponse(statsSvc);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private static ResponseEntity<Object> prepareStatsResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}
