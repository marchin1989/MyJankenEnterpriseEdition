package com.example.janken.infrastructure.jdbctransaction;

import com.example.janken.domain.transaction.Transaction;
import com.example.janken.domain.transaction.TransactionManager;
import lombok.val;

import java.util.function.Consumer;
import java.util.function.Function;

public class JdbcTransactionManager implements TransactionManager {

    @Override
    public <U> U transactional(Function<Transaction, U> f) {
        try (val tx = new JdbcTransaction()) {
            val result = f.apply(tx);
            tx.commit();
            return result;
        }
    }

    @Override
    public void transactional(Consumer<Transaction> t) {
        try (val tx = new JdbcTransaction()) {
            t.accept(tx);
            tx.commit();
        }
    }
}
