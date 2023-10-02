package com.github.lucasgois.tcc.deploy.utils.tableview;

import javafx.beans.property.SimpleStringProperty;
import javafx.util.Pair;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Data
@Slf4j
public class FileHash {

    private final SimpleStringProperty hash;
    private final SimpleStringProperty path;

    public FileHash(@NotNull Pair<String, String> pair) {
        this.hash = new SimpleStringProperty(pair.getKey());
        this.path = new SimpleStringProperty(pair.getValue());
    }
}
