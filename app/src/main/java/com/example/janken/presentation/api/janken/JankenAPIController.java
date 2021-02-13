package com.example.janken.presentation.api.janken;

import com.example.janken.application.service.JankenApplicationService;
import com.example.janken.registry.ServiceLocator;
import com.google.gson.Gson;
import lombok.val;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/jankens")
public class JankenAPIController extends HttpServlet {

    private final JankenApplicationService service = ServiceLocator.resolve(JankenApplicationService.class);
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        // リクエストbodyの取得
        val requestReader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = requestReader.readLine()) != null) {
            sb.append(line);
        }
        String requestBodyStr = sb.toString();
        val requestBody = gson.fromJson(requestBodyStr, JankenRequestBody.class);

        // jankenする
        val maybeWinner = service.play(
                requestBody.getPlayer1Id(),
                requestBody.player1Hand(),
                requestBody.getPlayer2Id(),
                requestBody.player2Hand());

        // レスポンスヘッダを設定
        response.setContentType("application/json");

        // レスポンスボディを設定

        val responseBodyStr = gson.toJson(JankenResponseBody.of(maybeWinner));
        val writer = response.getWriter();
        writer.print(responseBodyStr);
        writer.flush();
    }
}
