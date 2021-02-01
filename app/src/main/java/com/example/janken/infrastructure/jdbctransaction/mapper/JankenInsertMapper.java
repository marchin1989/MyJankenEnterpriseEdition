package com.example.janken.infrastructure.jdbctransaction.mapper;

import com.example.janken.domain.model.janken.Janken;
import com.example.janken.domain.model.janken.JankenDetail;
import lombok.val;

import java.sql.Timestamp;
import java.util.List;

public class JankenInsertMapper implements InsertMapper<Janken> {
    @Override
    public List<Object> mapValues(Janken object) {
        return List.of(Timestamp.valueOf(object.getPlayedAt()));
    }

    @Override
    public Janken mapObjectWithKey(long key, Janken objectWithoutKey) {
        val detail1 = objectWithoutKey.getJankenDetail1();
        val detail2 = objectWithoutKey.getJankenDetail2();
        val detail1WithJankenId = new JankenDetail(null,
                key,
                detail1.getPlayerId(),
                detail1.getHand(),
                detail1.getResult());
        val detail2WithJankenId = new JankenDetail(null,
                key,
                detail2.getPlayerId(),
                detail2.getHand(),
                detail2.getResult());
        return new Janken(key, objectWithoutKey.getPlayedAt(), detail1WithJankenId, detail2WithJankenId);
    }
}
