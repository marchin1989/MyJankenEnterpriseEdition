package com.example.janken.presentation.api;

import com.google.gson.Gson;
import lombok.val;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class APIControllerUtils {

    private static final Gson GSON = new Gson();

    public static <T> T getRequestBody(HttpServletRequest request, Class<T> clazz) {
        try {
            // リクエストbodyの取得
            val requestReader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = requestReader.readLine()) != null) {
                sb.append(line);
            }
            String requestBodyStr = sb.toString();
            return GSON.fromJson(requestBodyStr, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setResponse(HttpServletResponse response, Object object) {
        try {
            // レスポンスヘッダを設定
            response.setContentType("application/json");

            // レスポンスボディを設定
            val responseBodyStr = GSON.toJson(object);
            val writer = response.getWriter();
            writer.print(responseBodyStr);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
