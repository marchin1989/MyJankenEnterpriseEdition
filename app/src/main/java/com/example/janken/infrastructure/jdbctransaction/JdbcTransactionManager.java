package com.example.janken.infrastructure.jdbctransaction;

import com.example.janken.domain.transaction.Transaction;
import com.example.janken.domain.transaction.TransactionManager;

public class JdbcTransactionManager implements TransactionManager {
    @Override
    public Transaction startTransaction() {
        return new JdbcTransaction();
    }
}
