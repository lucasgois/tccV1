package com.github.lucasgois.tcc.servidor.controller.versions;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class VersionDto {

    @NotEmpty
    private String version_name;
    @NotEmpty
    private String environment_id;
    @NotEmpty
    private String module_id;
}