package com.github.lucasgois.tcc.servidor.controller.environments;

import com.github.lucasgois.tcc.servidor.controller.Locations;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(Locations.ENVIRONMENTS)
public class EnvironmentsController {

    private final EnvironmentsRepository repository;

    public EnvironmentsController(EnvironmentsRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> all() throws SQLException {
        return ResponseEntity.ok(repository.all());
    }

    @GetMapping("{uuid}")
    public ResponseEntity<Map<String, Object>> id(@PathVariable final String uuid) throws SQLException {
        Optional<Map<String, Object>> item = repository.id(uuid);
        return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> create(@RequestBody @Valid final EnvironmentsDto body) throws SQLException {
        String id = repository.create(body);
        URI uri = URI.create(Locations.ENVIRONMENTS + "/" + id);
        return ResponseEntity.created(uri).build();
    }

    @PutMapping(value = "{uuid}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> alterar(@PathVariable final String uuid, @RequestBody @Valid final EnvironmentsDto body) throws SQLException {
        repository.update(uuid, body);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{uuid}")
    public ResponseEntity<Object> delete(@PathVariable final String uuid) throws SQLException {
        repository.delete(uuid);
        return ResponseEntity.ok().build();
    }
}