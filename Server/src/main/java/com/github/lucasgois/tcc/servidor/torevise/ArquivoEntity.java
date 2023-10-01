package com.github.lucasgois.tcc.servidor.torevise;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArquivoEntity {

    private String hash;
    private String nome;
}