package com.example.janken.infrastructure.dao;

import com.example.janken.domain.model.janken.JankenDetail;
import com.example.janken.domain.transaction.Transaction;

import java.util.List;
import java.util.Optional;

public interface JankenDetailDao {

    List<JankenDetail> findAllOrderById(Transaction tx);

    Optional<JankenDetail> findById(Transaction tx, String id);

    List<JankenDetail> findByJankenIdOrderById(Transaction tx, String jankenId);

    long count(Transaction t);

    void insertAll(Transaction tx, List<JankenDetail> jankenDetails);
}
