package com.example.janken.dao;

import lombok.val;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CsvDaoUtils {
    // MyJankenEnterpriseEdition/app/../data/ を指す
    private static final String DEFAULT_DATA_DIR = System.getProperty("user.dir") + "/../data/";
    private static final String DATA_DIR_ENV_VARIABLE = System.getenv("DATA_DIR");
    static final String DATA_DIR = DATA_DIR_ENV_VARIABLE != null ? DATA_DIR_ENV_VARIABLE + "/" : DEFAULT_DATA_DIR;
    static final String CSV_DELIMITER = ",";

    static long countFileLines(String path) throws IOException {
        try (val stream = Files.lines(Paths.get(path), StandardCharsets.UTF_8)) {
            return stream.count();
        }
    }
}