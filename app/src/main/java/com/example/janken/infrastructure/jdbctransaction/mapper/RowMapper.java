package com.example.janken.infrastructure.jdbctransaction.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper<T> {

    /**
     * RowからEntityに変換する.
     */
    T map(ResultSet rs) throws SQLException;
}
