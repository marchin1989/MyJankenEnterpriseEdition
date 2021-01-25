package com.example.janken.domain.dao;

import com.example.janken.domain.model.Janken;
import com.example.janken.domain.transaction.Transaction;

import java.util.Optional;

public interface JankenDao {

    Optional<Janken> findById(Transaction tx, long id);

    long count(Transaction tx);

    Janken insert(Transaction tx, Janken janken);
}
