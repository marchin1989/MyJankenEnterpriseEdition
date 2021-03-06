package com.example.janken;

import com.example.janken.application.service.JankenApplicationService;
import com.example.janken.application.service.PlayerApplicationService;
import com.example.janken.infrastructure.jdbctransaction.JdbcTransactionManager;
import com.example.janken.infrastructure.mysqldao.JankenDetailMySQLDao;
import com.example.janken.infrastructure.mysqldao.JankenMySQLDao;
import com.example.janken.infrastructure.mysqldao.PlayerMySQLDao;
import com.example.janken.infrastructure.mysqlrepository.JankenMySQLRepository;
import com.example.janken.infrastructure.mysqlrepository.PlayerMySQLRepository;
import com.example.janken.presentation.cli.controller.JankenCLIController;
import lombok.val;

public class JankenCLIApplication {

    public static void main(String[] args) {

        // 依存解決の設定

        val tm = new JdbcTransactionManager();

        val playerDao = new PlayerMySQLDao();
        val jankenDao = new JankenMySQLDao();
        val jankenDetailDao = new JankenDetailMySQLDao();

        val playerRepository = new PlayerMySQLRepository(playerDao);
        val jankenRepository = new JankenMySQLRepository(jankenDao, jankenDetailDao);

        val playerApplicationService = new PlayerApplicationService(tm, playerRepository);
        val jankenApplicationService = new JankenApplicationService(tm, playerRepository, jankenRepository);

        // 実行

        val jankkenController = new JankenCLIController(playerApplicationService, jankenApplicationService);
        jankkenController.play();
    }
}
