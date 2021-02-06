package com.example.janken.presentation.api.health;

import com.example.janken.domain.transaction.TransactionManager;
import com.example.janken.infrastructure.jdbctransaction.JdbcTransactionManager;
import com.example.janken.infrastructure.jdbctransaction.SimpleJDBCWrapper;
import com.example.janken.infrastructure.jdbctransaction.mapper.SingleRowMapper;
import com.example.janken.registry.ServiceLocator;
import com.google.gson.Gson;
import lombok.val;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/health")
public class HealthAPIController extends HttpServlet {

    private final TransactionManager tm = new JdbcTransactionManager();
    private final SimpleJDBCWrapper simpleJDBCWrapper = ServiceLocator.resolve(SimpleJDBCWrapper.class);
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        // DBとの疎通チェック
        tm.transactional(tx -> {
            simpleJDBCWrapper.findFirst(tx, new SingleRowMapper<Long>(), "SELECT 1");
        });

        // レスポンスヘッダを設定
        response.setContentType("application/json");

        // レスポンスボディを設定
        val status = 200;
        val responseBodyStr = gson.toJson(new HealthResponseBody(status));
        val writer = response.getWriter();
        writer.print(responseBodyStr);
        writer.flush();
    }
}
