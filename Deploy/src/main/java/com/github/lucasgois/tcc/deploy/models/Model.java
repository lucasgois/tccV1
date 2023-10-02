package com.github.lucasgois.tcc.deploy.models;

import org.jetbrains.annotations.NotNull;

public abstract class Model {

    @NotNull
    public final String simpleName() {
        return id().substring(0, 8) + ' ' + name();
    }

    protected abstract String id();

    protected abstract String name();
}
