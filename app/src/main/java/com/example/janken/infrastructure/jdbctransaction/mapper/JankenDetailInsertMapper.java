package com.example.janken.infrastructure.jdbctransaction.mapper;

import com.example.janken.domain.model.janken.JankenDetail;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

public class JankenDetailInsertMapper implements InsertMapper<JankenDetail> {

    @Override
    public List<Object> mapValues(JankenDetail detail) {
        val list = new ArrayList<>();
        list.add(detail.getId());
        list.add(detail.getJankenId());
        list.add(detail.getPlayerId());
        list.add(detail.getHand().getValue());
        list.add(detail.getResult().getValue());
        return list;
    }
}
