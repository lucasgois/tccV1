package com.github.lucasgois.tcc.deploy.utils;

import com.github.lucasgois.tcc.deploy.exceptions.DeployException;
import com.github.lucasgois.tcc.deploy.models.Environment;
import com.github.lucasgois.tcc.deploy.models.Module;
import com.github.lucasgois.tcc.deploy.models.Version;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
public class InformationLoader {

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Module> getModules() throws DeployException {
        final ResponseEntity<List<Module>> response = restTemplate.exchange(
                "http://localhost:8080/modules",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<>() {
                }
        );

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new DeployException("Problema ao carregar módulos: " + response.getStatusCode());
        }

        final List<Module> modules = response.getBody();

        log.info("modules: {}", modules);
        return modules;
    }

    public List<Environment> getEnvironment() {
        final ResponseEntity<List<Environment>> response = restTemplate.exchange(
                "http://localhost:8080/environments",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<>() {
                }
        );

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new DeployException("Problema ao carregar ambientes: " + response.getStatusCode());
        }

        final List<Environment> environments = response.getBody();

        log.info("environments: {}", environments);
        return environments;
    }

    public List<Version> getVersions() {
        final ResponseEntity<List<Version>> response = restTemplate.exchange(
                "http://localhost:8080/versions",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<>() {
                }
        );

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new DeployException("Problema ao carregar ambientes: " + response.getStatusCode());
        }

        final List<Version> versions = response.getBody();

        log.info("versions: {}", versions);
        return versions;
    }
}