package com.example.janken.infrastructure.jdbctransaction.mapper;

import com.example.janken.domain.model.Janken;

import java.sql.Timestamp;
import java.util.List;

public class JankenInsertMapper implements InsertMapper<Janken> {
    @Override
    public List<Object> mapValues(Janken object) {
        return List.of(Timestamp.valueOf(object.getPlayedAt()));
    }

    @Override
    public Janken mapObjectWithKey(long key, Janken objectWithoutKey) {
        return new Janken(key, objectWithoutKey.getPlayedAt());
    }
}
