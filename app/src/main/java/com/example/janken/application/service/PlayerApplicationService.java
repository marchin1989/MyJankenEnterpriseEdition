package com.example.janken.application.service;

import com.example.janken.domain.dao.PlayerDao;
import com.example.janken.domain.model.Player;
import com.example.janken.domain.transaction.TransactionManager;
import com.example.janken.registry.ServiceLocator;

public class PlayerApplicationService {

    private final TransactionManager tm = ServiceLocator.resolve(TransactionManager.class);
    private final PlayerDao playerDao = ServiceLocator.resolve(PlayerDao.class);

    public Player findPlayerById(long playerId) {
        return tm.transactional(tx -> {
            return playerDao.findPlayerById(tx, playerId);
        });
    }
}