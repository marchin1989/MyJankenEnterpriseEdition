package com.example.janken.infrastructure.jdbctransaction.mapper;

import com.example.janken.domain.model.janken.Janken;

import java.sql.Timestamp;
import java.util.List;

public class JankenInsertMapper implements InsertMapper<Janken> {
    @Override
    public List<Object> mapValues(Janken object) {
        return List.of(object.getId(), Timestamp.valueOf(object.getPlayedAt()));
    }
}
