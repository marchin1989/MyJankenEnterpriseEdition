package com.example.janken.businesslogic.dao;

import com.example.janken.businesslogic.model.JankenDetail;

import java.util.Optional;

public interface JankenDetailDao {

    Optional<JankenDetail> findById(long id);

    long count();

    JankenDetail insert(JankenDetail jankenDetail);
}
