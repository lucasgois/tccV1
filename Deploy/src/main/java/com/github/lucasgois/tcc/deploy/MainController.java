package com.github.lucasgois.tcc.deploy;

import com.github.lucasgois.tcc.common.ModeloDto;
import com.github.lucasgois.tcc.common.Util;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Slf4j
public class MainController implements Initializable, Alerts {

    @FXML
    private TextField tfVersion;
    @FXML
    private ComboBox<String> cbModule;
    @FXML
    private ComboBox<String> cbEnvironment;
    @FXML
    private TableView<Modelo> tbArquivos;
    @FXML
    private TableColumn<Modelo, String> treeTableColumn1;
    @FXML
    private TableColumn<Modelo, String> treeTableColumn2;

    private final RestTemplate restTemplate = new RestTemplate();
    private Path path;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tfVersion.setDisable(true);
        tfVersion.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyMM")) + ".#");

        cbModule.getItems().add("Devok Gestão");
        cbModule.getItems().add("Devok NFC-e");
        cbModule.getItems().add("Devok NF-e");
        cbModule.getItems().add("Devok MEI");

        cbEnvironment.getItems().add("Homologação");
        cbEnvironment.getItems().add("Produção");

        treeTableColumn1.setCellValueFactory(param -> param.getValue().getKey());
        treeTableColumn2.setCellValueFactory(param -> param.getValue().getValue());

        try {
            path = Path.of(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent().resolve("main");
        } catch (URISyntaxException ex) {
            log.error("initialize", ex);
        }
    }

    @FXML
    protected void onExplorer() {
        log.info("onExplorer");

        try {
            explorer();

        } catch (Exception ex) {
            log.error("onExplorer", ex);
        }
    }


    @FXML
    private void onLoad() {
        log.info("onLoad");

        try {
            load();
        } catch (Exception ex) {
            log.error("onLoad", ex);
        }
    }

    @FXML
    private void onDeploy() {
        log.info("onDeploy");

        try {
            post();
            get();
        } catch (Exception ex) {
            error(ex);
        }
    }

    private void explorer() throws IOException {

        if (Files.notExists(path)) {
            Files.createDirectory(path);
        }

        final ProcessBuilder processBuilder = new ProcessBuilder("explorer.exe", path.toString());
        processBuilder.start();
    }

    private void load() throws IOException, NoSuchAlgorithmException {
        tbArquivos.getItems().clear();

        final List<Pair<String, String>> lista = Util.listFilesWithHashes(path);

        for (final Pair<String, String> item : lista) {
            tbArquivos.getItems().add(new Modelo(item));
        }
    }


    private void post() {
        List<ModeloDto> list = new ArrayList<>();

        for (Modelo item : tbArquivos.getItems()) {
            list.add(new ModeloDto(item.getKey().get(), item.getValue().get()));
        }

        String apiUrl = "http://localhost:8080/version";

        HttpEntity<List<ModeloDto>> requestEntity = new HttpEntity<>(list);

        ResponseEntity<String> postResponse = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        log.info("POST Response: {}", postResponse.getBody());
    }

    private void get() {
        String apiUrl = "http://localhost:8080/version";

        ResponseEntity<String> getResponse = restTemplate.getForEntity(
                apiUrl,
                String.class
        );

        log.info("GET Response: {}", getResponse.getBody());
    }
}