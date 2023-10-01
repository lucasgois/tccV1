package com.github.lucasgois.tcc.deploy;

import javafx.scene.control.Alert;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public interface Alerts {


    default void showAlert(String title, String headerText, String contentText) {
        final Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    default void error(final @NotNull Throwable throwable) {
        final Logger log = LoggerFactory.getLogger(Alerts.class);
        log.error("error", throwable);

        final Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(throwable.getMessage());
        alert.setContentText(throwable.toString());
        alert.showAndWait();
    }
}
