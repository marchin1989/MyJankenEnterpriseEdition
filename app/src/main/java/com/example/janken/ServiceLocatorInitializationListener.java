package com.example.janken;

import com.example.janken.application.service.JankenApplicationService;
import com.example.janken.application.service.PlayerApplicationService;
import com.example.janken.domain.model.janken.JankenRepository;
import com.example.janken.domain.model.player.PlayerRepository;
import com.example.janken.domain.transaction.TransactionManager;
import com.example.janken.infrastructure.dao.JankenDao;
import com.example.janken.infrastructure.dao.JankenDetailDao;
import com.example.janken.infrastructure.dao.PlayerDao;
import com.example.janken.infrastructure.jdbctransaction.JdbcTransactionManager;
import com.example.janken.infrastructure.jdbctransaction.SimpleJDBCWrapper;
import com.example.janken.infrastructure.mysqldao.JankenDetailMySQLDao;
import com.example.janken.infrastructure.mysqldao.JankenMySQLDao;
import com.example.janken.infrastructure.mysqldao.PlayerMySQLDao;
import com.example.janken.infrastructure.mysqlrepository.JankenMySQLRepository;
import com.example.janken.infrastructure.mysqlrepository.PlayerMySQLRepository;
import com.example.janken.registry.ServiceLocator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ServiceLocatorInitializationListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 依存解決の設定
        ServiceLocator.register(TransactionManager.class, JdbcTransactionManager.class);

        ServiceLocator.register(SimpleJDBCWrapper.class, SimpleJDBCWrapper.class);

        ServiceLocator.register(JankenApplicationService.class, JankenApplicationService.class);
        ServiceLocator.register(PlayerApplicationService.class, PlayerApplicationService.class);

        ServiceLocator.register(PlayerDao.class, PlayerMySQLDao.class);
        ServiceLocator.register(JankenDao.class, JankenMySQLDao.class);
        ServiceLocator.register(JankenDetailDao.class, JankenDetailMySQLDao.class);

        ServiceLocator.register(PlayerRepository.class, PlayerMySQLRepository.class);
        ServiceLocator.register(JankenRepository.class, JankenMySQLRepository.class);
    }
}
