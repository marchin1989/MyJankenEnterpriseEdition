package com.example.janken.infrastructure.jdbctransaction.mapper;

import com.example.janken.domain.model.janken.JankenDetail;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

public class JankenDetailInsertMapper implements InsertMapper<JankenDetail> {

    @Override
    public List<Object> mapValues(JankenDetail entity) {
        val list = new ArrayList<>();
        list.add(entity.getJankenId());
        list.add(entity.getPlayerId());
        list.add(entity.getHand().getValue());
        list.add(entity.getResult().getValue());
        return list;
    }

    @Override
    public JankenDetail mapObjectWithKey(long key, JankenDetail objectWithoutKey) {
        return new JankenDetail(key,
                objectWithoutKey.getJankenId(),
                objectWithoutKey.getPlayerId(),
                objectWithoutKey.getHand(),
                objectWithoutKey.getResult());
    }
}
