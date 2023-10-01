package com.github.lucasgois.tcc.servidor.controller.versions;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.github.lucasgois.tcc.servidor.controller.Locations.VERSIONS;

@RestController
@RequestMapping(VERSIONS)
public class VersionsController {

    private final VersionsRepository repository;

    public VersionsController(VersionsRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> all() throws SQLException {
        return ResponseEntity.ok(repository.all());
    }

    @GetMapping("{uuid}")
    public ResponseEntity<Map<String, Object>> id(@PathVariable final String uuid) throws SQLException {
        final Optional<Map<String, Object>> item = repository.id(uuid);
        return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> create(@RequestBody @Valid final VersionDto body) throws SQLException {
        final String id = repository.create(body);
        final URI uri = URI.create(VERSIONS + "/" + id);
        return ResponseEntity.created(uri).build();
    }

    @PutMapping(value = "{uuid}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> alterar(@PathVariable final String uuid, @RequestBody @Valid final VersionDto body) throws SQLException {
        repository.update(uuid, body);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{uuid}")
    public ResponseEntity<Object> delete(@PathVariable final String uuid) throws SQLException {
        repository.delete(uuid);
        return ResponseEntity.ok().build();
    }
}