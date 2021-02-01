package com.example.janken.infrastructure.jdbctransaction.mapper;

import com.example.janken.domain.model.janken.Hand;
import com.example.janken.domain.model.janken.JankenDetail;
import com.example.janken.domain.model.janken.Result;
import lombok.val;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JankenDetailRowMapper implements RowMapper<JankenDetail> {
    @Override
    public JankenDetail map(ResultSet rs) throws SQLException {
        val id = rs.getLong("id");
        val jankenId = rs.getLong("janken_id");
        val playerId = rs.getLong("player_id");
        val hand = Hand.of(rs.getInt("hand"));
        val result = Result.of(rs.getInt("result"));
        return new JankenDetail(id, jankenId, playerId, hand, result);
    }
}
