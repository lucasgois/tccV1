package com.github.lucasgois.tcc.deploy.utils;

import com.github.lucasgois.tcc.deploy.models.Model;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import org.jetbrains.annotations.NotNull;

public interface ComboboxUtil<T extends Model> {


    static <T extends Model> void set(@NotNull ComboBox<T> comboBox) {
        comboBox.setButtonCell(new ListCell<>(){
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.simpleName());
                }
            }
        });

        comboBox.setCellFactory(new Callback<>() {
            @Override
            public ListCell<T> call(final ListView<T> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(final T item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.simpleName());
                        }
                    }
                };
            }
        });
    }
}
