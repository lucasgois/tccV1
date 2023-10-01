package com.github.lucasgois.tcc.servidor.torevise;

import com.github.lucasgois.tcc.common.ModeloDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("version")
public class VersionController {

    private final ArquivoRepository arquivoRepository;
    private final List<ModeloDto> lista = new ArrayList<>();

    public VersionController(ArquivoRepository arquivoRepository) {
        this.arquivoRepository = arquivoRepository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ModeloDto>> getAll() {
        log.info("getAll");
        return ResponseEntity.ok(lista);
    }

    @GetMapping("{index}")
    public ResponseEntity<Object> get(@PathVariable int index) {
        log.info("get");
        return ResponseEntity.ok(lista.get(index));
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ModeloDto>> post(@RequestBody List<ModeloDto> lista1) throws SQLException {
        log.info("post");

        lista.addAll(lista1);

        for (final ModeloDto dto : lista1) {
            arquivoRepository.insert(new ArquivoEntity(dto.key(), dto.value()));
        }

        return ResponseEntity.ok(lista);

//        URI location = fromCurrentRequest()
//                .path("/{id}")
//                .buildAndExpand(versions.size() - 1)
//                .toUri();
//
//        return ResponseEntity.created(location).build();
    }

//    @PostMapping(value = "test", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Void> post2(@RequestBody String body) {
//        log.info("post2");
//
//        versions.add(body);
//
//        URI location = fromCurrentRequest()
//                .path("/{id}")
//                .buildAndExpand(versions.size() - 1)
//                .toUri();
//
//        return ResponseEntity.created(location).build();
//    }
}
