package com.github.lucasgois.tcc.servidor.utils;

import com.github.lucasgois.tcc.servidor.reroror.ServerException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ServerUtils {


    private ServerUtils() {
    }

    @NotNull
    public static String getMainPath() {
        return System.getProperty("user.home") + "\\tcc-server";
    }

    public static void createAppFile() {
        try {
            final Path path = Path.of(getMainPath());
            if (Files.notExists(path)) {
                Files.createDirectories(path);
            }

            final Path pathFiles = path.resolve("files");
            if (Files.notExists(pathFiles)) {
                Files.createDirectories(pathFiles);
            }
        } catch (IOException ex) {
            throw new ServerException(ex);
        }
    }

    @NotNull
    public static String getFilesPath() {
        return System.getProperty("user.home") + "\\tcc-server" + "\\files";
    }
}