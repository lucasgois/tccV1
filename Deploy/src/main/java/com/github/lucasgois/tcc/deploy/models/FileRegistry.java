package com.github.lucasgois.tcc.deploy.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FileRegistry {
    private String hash;
    private String path;
}