package com.github.lucasgois.tcc.deploy.models;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Version extends Model {

    private String version_id;
    private String version_name;
    private String environment_id;
    private String module_id;

    @Override
    protected String id() {
        return version_id;
    }

    @Override
    protected String name() {
        return version_name;
    }
}
