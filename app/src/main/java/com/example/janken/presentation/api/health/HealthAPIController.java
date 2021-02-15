package com.example.janken.presentation.api.health;

import com.example.janken.domain.transaction.TransactionManager;
import com.example.janken.infrastructure.jdbctransaction.SimpleJDBCWrapper;
import com.example.janken.infrastructure.jdbctransaction.mapper.SingleRowMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
@AllArgsConstructor
public class HealthAPIController {

    private final TransactionManager tm;
    private final SimpleJDBCWrapper simpleJDBCWrapper;

    @GetMapping
    public HealthResponseBody get() {

        // DBとの疎通チェック
        tm.transactional(tx -> {
            simpleJDBCWrapper.findFirst(tx, new SingleRowMapper<Long>(), "SELECT 1");
        });

        return new HealthResponseBody(200);
    }
}
