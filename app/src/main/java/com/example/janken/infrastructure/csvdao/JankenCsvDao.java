package com.example.janken.infrastructure.csvdao;

import com.example.janken.domain.model.janken.Janken;
import com.example.janken.domain.transaction.Transaction;
import com.example.janken.infrastructure.dao.JankenDao;
import lombok.val;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JankenCsvDao implements JankenDao {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private static final String JANKENS_CSV = CsvDaoUtils.DATA_DIR + "jankens.csv";

    @Override
    public List<Janken> findAllOrderByPlayedAt(Transaction tx) {
        try (val stream = Files.lines(Paths.get(JANKENS_CSV), StandardCharsets.UTF_8)) {
            return stream.map(this::line2Janken)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public Optional<Janken> findById(Transaction tx, String id) {
        try (val stream = Files.lines(Paths.get(JANKENS_CSV), StandardCharsets.UTF_8)) {
            return stream.map(this::line2Janken)
                    .filter(j -> j.getId().equals(id))
                    .findFirst();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public long count(Transaction tx) {
        return CsvDaoUtils.countFileLines(JANKENS_CSV);
    }

    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void insert(Transaction tx, Janken janken) {
        val jankensCsv = new File(JANKENS_CSV);

        try (val fw = new FileWriter(jankensCsv, true);
             val bw = new BufferedWriter(fw);
             val pw = new PrintWriter(bw)) {

            // ファイルが存在しない場合に備えて作成
            jankensCsv.createNewFile();

            val line = janken2Line(janken);
            pw.println(line);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Janken line2Janken(String line) {
        val values = line.split(CsvDaoUtils.CSV_DELIMITER);
        val jankenId = values[0];
        val playedAt = LocalDateTime.parse(values[1], dateTimeFormatter);

        return new Janken(jankenId, playedAt, null, null);
    }

    private String janken2Line(Janken janken) {
        val playedAtStr = dateTimeFormatter.format(janken.getPlayedAt());
        return janken.getId() + CsvDaoUtils.CSV_DELIMITER + playedAtStr;
    }
}
