package com.github.lucasgois.tcc.servidor.controller.fileregistries;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class FileRegistriesDto {

    @NotEmpty
    private String file_hash;
    @NotEmpty
    private String file_name;
}