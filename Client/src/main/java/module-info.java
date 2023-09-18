module com.github.lucasgois.tcc.cliente {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.github.lucasgois.tcc.cliente to javafx.fxml;
    exports com.github.lucasgois.tcc.cliente;
}