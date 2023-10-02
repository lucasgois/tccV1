package com.github.lucasgois.tcc.deploy.controllers;

import com.github.lucasgois.tcc.deploy.MainApplication;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public abstract class Controller implements Initializable {

    protected Stage stage;

    public void show() {
        stage.showAndWait();
    }

    @NotNull
    public static Controller createController(final Controllers controllerType) throws IOException {
        return createController(controllerType, null);
    }

    @NotNull
    public static Controller createController(@NotNull final Controllers controllerType, final Stage owner) throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(controllerType.getPath()));

        final Scene scene = new Scene(fxmlLoader.load());

        final Stage stage = new Stage();
        stage.setTitle(controllerType.getTitle());
        stage.setScene(scene);

        stage.initOwner(owner);

        if (owner == null) {
            stage.initModality(Modality.NONE);
        } else {
            stage.initModality(Modality.WINDOW_MODAL);
        }


        final Controller controller = fxmlLoader.getController();
        controller.stage = stage;

        return controller;
    }
}
