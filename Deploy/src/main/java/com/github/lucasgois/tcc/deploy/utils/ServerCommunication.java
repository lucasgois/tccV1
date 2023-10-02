package com.github.lucasgois.tcc.deploy.utils;

import com.github.lucasgois.tcc.deploy.exceptions.DeployException;
import com.github.lucasgois.tcc.deploy.models.Environment;
import com.github.lucasgois.tcc.deploy.models.Module;
import com.github.lucasgois.tcc.deploy.models.Version;
import com.github.lucasgois.tcc.deploy.server.DeployFile;
import com.github.lucasgois.tcc.deploy.utils.tableview.FileHash;
import javafx.collections.ObservableList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Slf4j
public class ServerCommunication {

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Module> getModules() throws DeployException {
        final ResponseEntity<List<Module>> response = restTemplate.exchange("http://localhost:8080/modules", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<>() {
        });

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new DeployException("Problema ao carregar módulos: " + response.getStatusCode());
        }

        final List<Module> modules = response.getBody();

        log.info("modules: {}", modules);
        return modules;
    }

    public List<Environment> getEnvironment() {
        final ResponseEntity<List<Environment>> response = restTemplate.exchange("http://localhost:8080/environments", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<>() {
        });

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new DeployException("Problema ao carregar ambientes: " + response.getStatusCode());
        }

        final List<Environment> environments = response.getBody();

        log.info("environments: {}", environments);
        return environments;
    }

    public List<Version> getVersions() {
        final ResponseEntity<List<Version>> response = restTemplate.exchange("http://localhost:8080/versions", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<>() {
        });

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new DeployException("Problema ao carregar ambientes: " + response.getStatusCode());
        }

        final List<Version> versions = response.getBody();

        log.info("versions: {}", versions);
        return versions;
    }


    public void deploy(Path path, ObservableList<FileHash> items) throws IOException {
        new DeployFile(this, path, items).deploy();
    }

    public void post(final Path fileToDeploy) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        final FileSystemResource fileResource = new FileSystemResource(fileToDeploy);

        final MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileResource);

        final HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        final ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8080/file-registries",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        log.info("response: {}", response);
    }
}