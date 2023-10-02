package com.github.lucasgois.tcc.servidor.controller.fileregistries;

import com.github.lucasgois.tcc.servidor.utils.ServerUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.github.lucasgois.tcc.servidor.controller.Locations.FILE_REGISTRIES;
import static java.util.Objects.requireNonNull;

@RestController
@RequestMapping(FILE_REGISTRIES)
public class FileRegistriesController {

    private final FileRegistriesRepository repository;

    public FileRegistriesController(FileRegistriesRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> all() throws SQLException {
        return ResponseEntity.ok(repository.all());
    }

    @GetMapping("{hash}")
    public ResponseEntity<Map<String, Object>> id(@PathVariable final String hash) throws SQLException {
        final Optional<Map<String, Object>> item = repository.id(hash);
        return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> create(@NotNull @RequestParam("file") MultipartFile file) throws SQLException, IOException {
        ServerUtils.createAppFile();

        final String fileHash = requireNonNull(file.getOriginalFilename());
        final Path filePath = Path.of(ServerUtils.getFilesPath(), fileHash);
        Files.write(filePath, file.getBytes());

        repository.create(fileHash);
        final URI uri = URI.create(FILE_REGISTRIES + "/" + fileHash);
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("{hash}")
    public ResponseEntity<Object> delete(@PathVariable final String hash) throws SQLException {
        repository.delete(hash);
        return ResponseEntity.ok().build();
    }
}