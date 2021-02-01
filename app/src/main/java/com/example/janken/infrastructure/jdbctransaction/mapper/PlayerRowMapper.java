package com.example.janken.infrastructure.jdbctransaction.mapper;

import com.example.janken.domain.model.player.Player;
import lombok.val;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerRowMapper implements RowMapper<Player> {
    @Override
    public Player map(ResultSet rs) throws SQLException {
        val id = rs.getLong("id");
        val name = rs.getString("name");
        return new Player(id, name);
    }
}
