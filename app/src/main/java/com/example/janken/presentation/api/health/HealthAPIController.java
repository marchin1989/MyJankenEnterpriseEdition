package com.example.janken.presentation.api.health;

import com.example.janken.domain.transaction.TransactionManager;
import com.example.janken.infrastructure.jdbctransaction.JdbcTransactionManager;
import com.example.janken.infrastructure.jdbctransaction.SimpleJDBCWrapper;
import com.example.janken.infrastructure.jdbctransaction.mapper.SingleRowMapper;
import com.example.janken.presentation.api.APIControllerUtils;
import com.example.janken.registry.ServiceLocator;
import lombok.val;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/health")
public class HealthAPIController extends HttpServlet {

    private final TransactionManager tm = new JdbcTransactionManager();
    private final SimpleJDBCWrapper simpleJDBCWrapper = ServiceLocator.resolve(SimpleJDBCWrapper.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        // DBとの疎通チェック
        tm.transactional(tx -> {
            simpleJDBCWrapper.findFirst(tx, new SingleRowMapper<Long>(), "SELECT 1");
        });

        val status = 200;
        APIControllerUtils.setResponse(response, new HealthResponseBody(status));
    }
}
