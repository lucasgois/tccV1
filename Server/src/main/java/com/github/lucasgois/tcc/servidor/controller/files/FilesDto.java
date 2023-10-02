package com.github.lucasgois.tcc.servidor.controller.files;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class FilesDto {

    @NotEmpty
    private String file_hash;
    @NotEmpty
    private String file_name;
}