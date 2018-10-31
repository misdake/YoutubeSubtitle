package com.rs.tool.youtube_sub.action;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class HttpClient {
    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();
    private static String       key    = null;

    public static void init(String key) {
        HttpClient.key = key;
    }

    public static String run(String url) {
        if (key == null) {
            System.out.println("key未指定");
            return null;
        }

        if (url.contains("?")) {
            url = url + "&api-key=" + key;
        } else {
            url = url + "?api-key=" + key;
        }

        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();
            if (body == null) return null;
            return body.string();
        } catch (IOException e) {
            return null;
        }
    }

}
