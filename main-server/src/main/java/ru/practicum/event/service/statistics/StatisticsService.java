package ru.practicum.event.service.statistics;

import ru.practicum.event.model.Event;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface StatisticsService {

    void saveStats(HttpServletRequest request);

    Map<Long, Long> getViews(List<Event> events);


}
