package com.github.lucasgois.tcc.deploy;

import javafx.application.Application;
import javafx.stage.Stage;

import static com.github.lucasgois.tcc.deploy.controllers.Controller.createController;
import static com.github.lucasgois.tcc.deploy.controllers.Controllers.DEPLOY;
import static com.github.lucasgois.tcc.deploy.utils.Alerts.error;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) {
        try {
            createController(DEPLOY, stage).show();
        } catch (Exception ex) {
            error(ex);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}