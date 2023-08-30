package org.example.customText.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.customText.BarRepository;
import org.example.customText.model.Bar;
import org.example.exception.model.EntityNoFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bar")
@RequiredArgsConstructor
@Slf4j
public class BarController {
    private final BarRepository barRepository;


    @PostMapping("/{name}")
    public Bar addBar(@PathVariable String name) {
        return barRepository.save(new Bar(name));
    }


    @GetMapping("/{id}")
    public Bar getBar(@PathVariable long id) {
        return barRepository.findById(id).orElseThrow(()-> new EntityNoFoundException(" ups"));
    }

    @GetMapping("/getAll")
    public List<Bar> getAll() {
        return barRepository.findAll();
    }
}
