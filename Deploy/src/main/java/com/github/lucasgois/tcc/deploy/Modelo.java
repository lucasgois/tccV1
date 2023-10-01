package com.github.lucasgois.tcc.deploy;

import javafx.beans.property.SimpleStringProperty;
import javafx.util.Pair;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Data
@Slf4j
public class Modelo {

    private final SimpleStringProperty key;
    private final SimpleStringProperty value;

    public Modelo(@NotNull Pair<String, String> pair) {
        this.key = new SimpleStringProperty(pair.getKey());
        this.value = new SimpleStringProperty(pair.getValue());
    }
}
