package com.github.lucasgois.tcc.servidor.controller.environments;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class EnvironmentsDto {

    @NotEmpty
    private String environment_name;
}