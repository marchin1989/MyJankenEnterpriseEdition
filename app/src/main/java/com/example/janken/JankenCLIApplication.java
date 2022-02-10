package com.example.janken;

import com.example.janken.application.service.JankenApplicationService;
import com.example.janken.application.service.PlayerApplicationService;
import com.example.janken.infrastructure.jdbctransaction.JdbcTransaction;
import com.example.janken.infrastructure.jdbctransaction.JdbcTransactionManager;
import com.example.janken.infrastructure.mysqldao.PlayerMySQLDao;
import com.example.janken.infrastructure.mysqlrepository.JankenMySQLRepository;
import com.example.janken.infrastructure.mysqlrepository.PlayerMySQLRepository;
import com.example.janken.presentation.cli.controller.JankenCLIController;
import lombok.val;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public class JankenCLIApplication {

    public static void main(String[] args) {

        // 依存解決の設定

        val tm = new JdbcTransactionManager();

        val playerDao = new PlayerMySQLDao();

        tm.transactional(tx -> {
            val conn = ((JdbcTransaction) tx).conn;
            val dslContext = DSL.using(conn, SQLDialect.MYSQL);

            val playerRepository = new PlayerMySQLRepository(playerDao);
            val jankenRepository = new JankenMySQLRepository(dslContext);

            val playerApplicationService = new PlayerApplicationService(tm, playerRepository);
            val jankenApplicationService = new JankenApplicationService(tm, playerRepository, jankenRepository);

            // 実行

            val jankkenController = new JankenCLIController(playerApplicationService, jankenApplicationService);
            jankkenController.play();
        });
    }
}
