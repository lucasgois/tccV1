package com.github.lucasgois.tcc.deploy.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class Environment extends Model {

    private String environment_id;
    private String environment_name;

    @Override
    protected String id() {
        return environment_id;
    }

    @Override
    protected String name() {
        return environment_name;
    }
}
