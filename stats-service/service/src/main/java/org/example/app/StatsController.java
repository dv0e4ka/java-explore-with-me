package org.example.app;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.app.service.StatsService;
import org.example.dto.EndpointHitDto;
import org.example.dto.ViewStatsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {
    StatsService statsService;

    @Autowired
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @PostMapping("/hit")
    public ResponseEntity<EndpointHitDto> add(@RequestBody EndpointHitDto endPointHitDto) {
        return new ResponseEntity<>(statsService.add(endPointHitDto), HttpStatus.CREATED);

    }

    @GetMapping("/stats")
    public ResponseEntity<List<ViewStatsDto>> getStats(@RequestParam
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                       LocalDateTime start,

                                                       @RequestParam
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                       LocalDateTime end,

                                                       @RequestParam(required = false) List<String> uris,
                                                       @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("поспупил запрос");
        List<ViewStatsDto> responseBody = statsService.getStats(start, end, uris, unique);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
