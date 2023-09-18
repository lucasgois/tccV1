package com.github.lucasgois.tcc.servidor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("version")
public class VersionController {

    private final List<Object> versions = new ArrayList<>();

    @GetMapping()
    public ResponseEntity<List<Object>> getAll() {
        return ResponseEntity.ok(versions);
    }

    @GetMapping("{index}")
    public ResponseEntity<Object> get(@PathVariable int index) {
        return ResponseEntity.ok(versions.get(index));
    }

    @PostMapping(value = "", consumes = "text/plain;charset=UTF-8")
    public ResponseEntity<Void> post(@RequestBody String body) {
        versions.add(body);

        URI location = fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(versions.size() - 1)
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
