package com.example.janken;

import com.example.janken.application.service.JankenService;
import com.example.janken.application.service.PlayerService;
import com.example.janken.domain.dao.JankenDao;
import com.example.janken.domain.dao.JankenDetailDao;
import com.example.janken.domain.dao.PlayerDao;
import com.example.janken.domain.transaction.TransactionManager;
import com.example.janken.infrastructure.jdbctransaction.JdbcTransactionManager;
import com.example.janken.infrastructure.mysqldao.JankenDetailMySQLDao;
import com.example.janken.infrastructure.mysqldao.JankenMySQLDao;
import com.example.janken.infrastructure.mysqldao.PlayerMySQLDao;
import com.example.janken.presentation.controller.JankenController;
import com.example.janken.registry.ServiceLocator;

public class App {

    public static void main(String[] args) {

        // 依存解決の設定

        ServiceLocator.register(JankenController.class, JankenController.class);

        ServiceLocator.register(JankenService.class, JankenService.class);
        ServiceLocator.register(PlayerService.class, PlayerService.class);

        ServiceLocator.register(TransactionManager.class, JdbcTransactionManager.class);

        ServiceLocator.register(PlayerDao.class, PlayerMySQLDao.class);
        ServiceLocator.register(JankenDao.class, JankenMySQLDao.class);
        ServiceLocator.register(JankenDetailDao.class, JankenDetailMySQLDao.class);

        // 実行

        ServiceLocator.resolve(JankenController.class).play();
    }
}
