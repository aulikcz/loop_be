package com.aulikcz.loop.hello;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
public class HelloController {
    private final HelloRepository repo;
    public HelloController(HelloRepository repo) { this.repo = repo; }

    @GetMapping("/hello")
    public Map<String, String> hello() {
        String msg = repo.findAll().stream().findFirst()
                .map(HelloEntity::getMessage).orElse("Prázdno");
        return Map.of("message", msg);
    }
}
