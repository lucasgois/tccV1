package com.github.lucasgois.tcc.deploy.utils;

import javafx.scene.control.Alert;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;


@Slf4j
public class Alerts {

    private Alerts() {
    }

    public static void showAlert(String title, String headerText, String contentText) {
        final Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public static void error(@NotNull final Throwable throwable) {
        log.error("error", throwable);

        final Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");

        if (throwable.getCause() == null) {
            alert.setHeaderText(throwable.getMessage());
            alert.setContentText(throwable.toString());
        } else {
            alert.setHeaderText(throwable.getCause().getMessage());
            alert.setContentText(throwable.getCause().toString());
        }

        alert.showAndWait();
    }
}
