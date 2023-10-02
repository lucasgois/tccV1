package com.github.lucasgois.tcc.deploy.controllers;

import com.github.lucasgois.tcc.common.Util;
import com.github.lucasgois.tcc.deploy.exceptions.DeployException;
import com.github.lucasgois.tcc.deploy.models.Environment;
import com.github.lucasgois.tcc.deploy.models.Module;
import com.github.lucasgois.tcc.deploy.models.Version;
import com.github.lucasgois.tcc.deploy.server.DeployFile;
import com.github.lucasgois.tcc.deploy.utils.ComboboxUtil;
import com.github.lucasgois.tcc.deploy.utils.ServerCommunication;
import com.github.lucasgois.tcc.deploy.utils.tableview.FileHash;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.ResourceBundle;

import static com.github.lucasgois.tcc.deploy.utils.Alerts.error;


@Slf4j
public class DeployController extends Controller {

    private final Version newVersion = new Version("--------", "Criar nova versão", null, null);

    @FXML
    private ComboBox<Version> cbVersions;
    @FXML
    private ComboBox<Module> cbModules;
    @FXML
    private ComboBox<Environment> cbEnvironments;
    @FXML
    private TableView<FileHash> tbArquivos;
    @FXML
    private TableColumn<FileHash, String> treeTableColumn1;
    @FXML
    private TableColumn<FileHash, String> treeTableColumn2;

    private Path path;
    private boolean comboBoxFlag = false;
    private final ServerCommunication server = new ServerCommunication();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        treeTableColumn1.setCellValueFactory(param -> param.getValue().getHash());
        treeTableColumn2.setCellValueFactory(param -> param.getValue().getPath());

        try {
            path = Path.of(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent().resolve("main");
        } catch (URISyntaxException ex) {
            throw new DeployException(ex);
        }

        ComboboxUtil.set(cbVersions);
        ComboboxUtil.set(cbModules);
        ComboboxUtil.set(cbEnvironments);

        loadCombobox();
    }

    @FXML
    private void onCbVersions() {
        if (comboBoxFlag) return;
        else comboBoxFlag = true;

        Version version = cbVersions.getValue();

        if (version != newVersion) {
            final Module module = cbModules.getItems().stream().filter(item -> version.getModule_id().equals(item.getModule_id())).findFirst().orElse(null);
            cbModules.setValue(module);

            final Environment environment = cbEnvironments.getItems().stream().filter(item -> version.getEnvironment_id().equals(item.getEnvironment_id())).findFirst().orElse(null);
            cbEnvironments.setValue(environment);
        }

        comboBoxFlag = false;
    }

    @FXML
    private void onCbEnvironments() {
        if (comboBoxFlag) return;
        else comboBoxFlag = true;

        cbVersions.setValue(newVersion);

        comboBoxFlag = false;
    }

    @FXML
    private void onCbModules() {
        if (comboBoxFlag) return;
        else comboBoxFlag = true;

        cbVersions.setValue(newVersion);

        comboBoxFlag = false;
    }

    private void loadCombobox() {
        cbVersions.getItems().clear();
        cbModules.getItems().clear();
        cbEnvironments.getItems().clear();

        cbVersions.getItems().add(newVersion);
        cbVersions.setValue(newVersion);

        cbVersions.getItems().addAll(server.getVersions());
        cbModules.getItems().addAll(server.getModules());
        cbEnvironments.getItems().addAll(server.getEnvironment());
    }

    @FXML
    protected void onExplorer() {
        try {
            explorer();
        } catch (Exception ex) {
            error(ex);
        }
    }


    @FXML
    private void onLoad() {
        try {
            load();
        } catch (Exception ex) {
            error(ex);
        }
    }

    @FXML
    private void onDeploy() {
        log.info("onDeploy");

        try {
            server.deploy(path, tbArquivos.getItems());
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
        DeployFile.init(path);

        tbArquivos.getItems().clear();

        final List<Pair<String, String>> lista = Util.listFilesWithHashes(path);

        for (final Pair<String, String> item : lista) {
            tbArquivos.getItems().add(new FileHash(item));
        }
    }
}