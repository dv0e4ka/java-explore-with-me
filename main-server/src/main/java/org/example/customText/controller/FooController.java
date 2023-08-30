package org.example.customText.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.customText.BarRepository;
import org.example.customText.FooRepository;
import org.example.customText.model.Bar;
import org.example.customText.model.Foo;
import org.example.enums.RequestStatus;
import org.example.event.model.Event;
import org.example.event.repository.EventRepository;
import org.example.exception.model.EntityNoFoundException;
import org.example.request.model.ParticipationRequest;
import org.example.request.repository.RequestRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/foo")
@RequiredArgsConstructor
@Slf4j
public class FooController {
    private final FooRepository fooRepository;
    private final BarRepository barRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;


    @PostMapping("/{barId}")
    public Foo add(@PathVariable long barId) {
        log.info("barId = {}", barId);
        Bar bar = barRepository.findById(barId).orElseThrow(()-> new EntityNoFoundException(" ups"));
        String name = "foo" + barId;
        return fooRepository.save(new Foo(name, bar));
    }

    @GetMapping("/{id}")
    public Foo get(@PathVariable long id) {
        return fooRepository.findById(id).orElseThrow(()-> new EntityNoFoundException(" ups"));
    }

    @GetMapping("/getAll")
    public List<Foo> getAll() {
        return fooRepository.findAll();
    }

//    @GetMapping("/param/{text}")
//    public List<Foo> getByParam(@PathVariable(required = false) String text) {
//        List<String> stringList = List.of(text);
//
//        return fooRepository.findAllByParam(text);
//    }
//
//    @GetMapping("/params/{ids}")
//    public List<Foo> getByParams(@PathVariable(required = false) Long ids) {
//        log.info("ids = {}", ids);
//        List<Long> idsList = List.of(ids);
//
//        return fooRepository.findAllByParams(idsList);
//    }

    @PostMapping("/params")
    public List<Foo> getByParamsNull(@RequestParam(required = false) List<Long> par) {
//        log.info("params ={}", par);
        Long ids = null;
        Long idFail = 500L;

        return fooRepository.findAllByParams(par);
    }

    @GetMapping("/long")
    public Long getLong(@RequestParam(required = false) Long number) {
        return number;
    }

    @GetMapping("/longlist")
    public List<Foo> getLongList(@RequestParam(required = false) List<Long> number) {
        return fooRepository.findAllByParams(number);
    }


    @GetMapping("/event")
    public List<Event> getEvents(@RequestParam(required = false) List<Long> users,
                                 @RequestParam(required = false) List<String> state,
                                 @RequestParam(required = false) List<Long> categories,
                                 @RequestParam(required = false) String rangeStart,
                                 @RequestParam(required = false) String rangeEnd,
                                 @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                 @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("пришел запрос");
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusYears(3);

        PageRequest page = PageRequest.of(0, 100);
        return eventRepository.findAllByParam(users, state, categories, start, end, page);
    }

//    @GetMapping("/publish")
//    public List<ParticipationRequest> getPublish() {
//        return requestRepository.findAllByStatus(RequestStatus.CONFIRMED);
//    }




}
