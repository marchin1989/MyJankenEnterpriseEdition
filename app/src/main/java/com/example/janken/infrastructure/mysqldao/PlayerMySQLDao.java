package com.example.janken.infrastructure.mysqldao;

import com.example.janken.domain.model.player.Player;
import com.example.janken.domain.transaction.Transaction;
import com.example.janken.infrastructure.dao.PlayerDao;
import com.example.janken.infrastructure.jdbctransaction.SimpleJDBCWrapper;
import com.example.janken.infrastructure.jdbctransaction.mapper.PlayerRowMapper;
import lombok.val;

public class PlayerMySQLDao implements PlayerDao {
    private static final String SELECT_FROM_CLAUSE = "SELECT id, name FROM players ";

    private final SimpleJDBCWrapper simpleJDBCWrapper = new SimpleJDBCWrapper();
    private final PlayerRowMapper mapper = new PlayerRowMapper();

    @Override
    public Player findPlayerById(Transaction tx, long playerId) {
        val sql = SELECT_FROM_CLAUSE + "WHERE id = ?";
        return simpleJDBCWrapper.findFirst(tx, mapper, sql, playerId).get();
    }
}
