package com.example.janken.framework;

public interface TransactionManager {

    /**
     * トランザクションを開始
     */
    Transaction startTransaction();
}
