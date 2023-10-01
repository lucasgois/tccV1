package com.github.lucasgois.tcc.servidor.controller.modules;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ModuleDto {

    @NotEmpty
    private String module_name;
}