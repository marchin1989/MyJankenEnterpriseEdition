package com.example.janken.dataaccess.dao;

import com.example.janken.dataaccess.model.Janken;
import lombok.val;

import java.io.*;
import java.time.format.DateTimeFormatter;

public class JankenCsvDao {

    private static final String JANKENS_CSV = CsvDaoUtils.DATA_DIR + "jankens.csv";

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public Janken insert(Janken janken) {
        val jankensCsv = new File(JANKENS_CSV);

        try (val fw = new FileWriter(jankensCsv, true);
             val bw = new BufferedWriter(fw);
             val pw = new PrintWriter(bw)) {

            // ファイルが存在しない場合に備えて作成
            jankensCsv.createNewFile();

            val jankenId = CsvDaoUtils.countFileLines(JANKENS_CSV) + 1;
            val jankenWithId = new Janken(jankenId, janken.getPlayedAt());

            writeJanken(pw, jankenWithId);

            return jankenWithId;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void writeJanken(PrintWriter pw, Janken janken) {
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss");
        val playedAtStr = formatter.format(janken.getPlayedAt());
        pw.println(janken.getId() + CsvDaoUtils.CSV_DELIMITER + playedAtStr);
    }
}
