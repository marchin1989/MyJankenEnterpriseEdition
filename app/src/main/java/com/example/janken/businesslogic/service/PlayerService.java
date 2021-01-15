package com.example.janken.businesslogic.service;

import com.example.janken.businesslogic.dao.PlayerDao;
import com.example.janken.businesslogic.model.Player;
import com.example.janken.framework.ServiceLocator;

public class PlayerService {

    private PlayerDao playerCsvDao = ServiceLocator.resolve(PlayerDao.class);

    public Player findPlayerById(long playerId) {
        return playerCsvDao.findPlayerById(playerId);
    }
}
