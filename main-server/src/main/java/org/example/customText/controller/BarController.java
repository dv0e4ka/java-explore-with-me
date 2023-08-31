package org.example.customText.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.customText.BarRepository;
import org.example.customText.model.Bar;
import org.example.customText.model.Exp;
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

    @GetMapping("/text")
    public Boolean getAll(@RequestBody Exp exp) {
        String test =  "Vero voluptatem quo et earum numquam enim et. Labore ducimus at. Et voluptas voluptatem fuga cupiditate aut culpa ea voluptates. Sit ut error explicabo quas quibusdam. Fuga aut adipisci aliquid magni voluptatem et veritatis explicabo. Assumenda minus aut architecto a placeat error quisquam.\\n \\rAut sunt minima. Quia adipisci adipisci perferendis maiores hic est voluptas odio earum. Et vitae qui sit eveniet temporibus eveniet maxime accusantium. Non aut nisi iusto velit saepe. Ipsa aspernatur ea recusandae voluptas qui est accusamus laborum aut.\\n \\rAd delectus est quis nihil. Saepe est accusamus corrupti in ut corrupti. Fugit commodi dolor.";
        return exp.getText().equals(test);
    }
}
