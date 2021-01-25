package com.example.janken.domain.dao;

import com.example.janken.domain.model.JankenDetail;
import com.example.janken.framework.Transaction;

import java.util.List;
import java.util.Optional;

public interface JankenDetailDao {

    Optional<JankenDetail> findById(Transaction tx, long id);

    long count(Transaction t);

    List<JankenDetail> insertAll(Transaction tx, List<JankenDetail> jankenDetails);
}
