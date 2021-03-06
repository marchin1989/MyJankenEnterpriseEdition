package com.example.janken.infrastructure.jdbctransaction.mapper;

import com.example.janken.domain.model.janken.Janken;
import lombok.val;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JankenRowMapper implements RowMapper<Janken> {
    @Override
    public Janken map(ResultSet rs) throws SQLException {
        val id = rs.getString("id");
        val playedAt = rs.getTimestamp("played_at").toLocalDateTime();
        return new Janken(id, playedAt, null, null);
    }
}
