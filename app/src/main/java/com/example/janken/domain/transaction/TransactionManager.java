package com.example.janken.domain.transaction;

import java.util.function.Consumer;
import java.util.function.Function;

public interface TransactionManager {

    /**
     * 以下の流れで戻り値のあるトランザクションを実行する.
     *
     * <ol>
     *     <li>トランザクションを開始</li>
     *     <li>引数の実行</li>
     *     <li>コミットし、トランザクションの終了</li>
     *     <li>結果を返却</li>
     * </ol>
     * <p>
     * 引数の処理の途中で例外が発生した場合は、コミットせずにトランザクションを終了.
     */
    <U> U transactional(Function<Transaction, U> f);

    /**
     * 以下の流れで戻り値のないトランザクションを実行する.
     *
     * <ol>
     *     <li>トランザクションを開始</li>
     *     <li>引数の実行</li>
     *     <li>コミットし、トランザクションの終了</li>
     * </ol>
     * <p>
     * 引数の処理の途中で例外が発生した場合は、コミットせずにトランザクションを終了.
     */
    void transactional(Consumer<Transaction> t);
}
