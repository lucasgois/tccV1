package com.github.lucasgois.tcc.deploy.controllers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum Controllers {
    DEPLOY("Deploy", "views/deploy-view.fxml"),
    MAIN2("Main2", "views/main-view2.fxml"),
    ;

    private final String title;
    private final String path;
}
