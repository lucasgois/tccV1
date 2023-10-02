package com.github.lucasgois.tcc.deploy.server;

import com.github.lucasgois.tcc.deploy.models.FileRegistry;
import com.github.lucasgois.tcc.deploy.utils.ServerCommunication;
import com.github.lucasgois.tcc.deploy.utils.tableview.FileHash;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class DeployFile {

    private final ServerCommunication server;
    private final Path mainPath;
    private final Path deployPath;
    private final List<FileHash> listFiles;

    @NotNull
    public static Path createPathDeploy(@NotNull final Path mainPath) {
        return mainPath.resolve(".deploy");
    }

    public DeployFile(final ServerCommunication server, @NotNull final Path mainPath, @NotNull final List<FileHash> listFiles) {
        this.server = server;
        this.mainPath = mainPath;
        this.listFiles = listFiles;

        this.deployPath = createPathDeploy(mainPath);
    }

    public static void init(final Path mainPath) throws IOException {
        final Path deployPath = DeployFile.createPathDeploy(mainPath);

        if (Files.exists(deployPath)) {
            try (final DirectoryStream<Path> paths = Files.newDirectoryStream(deployPath)) {
                for (final Path path : paths) {
                    Files.deleteIfExists(path);
                }
            }
            Files.deleteIfExists(deployPath);
        }
    }

    public void deploy() throws IOException {
        log.info("{}", listFiles);

        if (Files.notExists(deployPath)) {
            Files.createDirectory(deployPath);
        }

        final Set<String> deployedHashes = new HashSet<>();

        final List<FileRegistry> registries = new ArrayList<>();

        for (final FileHash listFile : listFiles) {

            final FileRegistry registry = new FileRegistry();
            registry.setHash(listFile.getHash().get());
            registry.setPath(listFile.getPath().get());

            if (deployedHashes.add(registry.getHash())) {
                copyFile(registry);
            }

            registries.add(registry);
        }

        postList(registries);

        postFiles(deployedHashes);

        Files.delete(deployPath);
    }

    private void copyFile(
            @NotNull final FileRegistry registry
    ) throws IOException {
        final Path pathFrom = mainPath.resolve(registry.getPath().substring(1));
        final Path pathTo = deployPath.resolve(registry.getHash());

        Files.copy(pathFrom, pathTo);
    }


    private void postList(final List<FileRegistry> registries) {
        log.info("post={}", registries);
    }

    private void postFiles(@NotNull final Set<String> hashList) throws IOException {
        for (final String string : hashList) {
            final Path fileToDeploy = deployPath.resolve(string);

            server.post(fileToDeploy);

            Files.delete(fileToDeploy);
        }
    }
}
