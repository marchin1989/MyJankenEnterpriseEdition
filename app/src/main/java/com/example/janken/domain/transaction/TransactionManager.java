package com.example.janken.domain.transaction;

public interface TransactionManager {

    /**
     * トランザクションを開始
     */
    Transaction startTransaction();
}
