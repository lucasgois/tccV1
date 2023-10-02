package com.github.lucasgois.tcc.deploy.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Module extends Model {

    private String module_id;
    private String module_name;

    @Override
    protected String id() {
        return module_id;
    }

    @Override
    protected String name() {
        return module_name;
    }
}
