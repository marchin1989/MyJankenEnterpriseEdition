package com.example.janken.infrastructure.jdbctransaction.mapper;

import java.util.List;

public interface InsertMapper<T> {
    /**
     * Objectから列の値に変換する
     */
    List<Object> mapValues(T object);
}
