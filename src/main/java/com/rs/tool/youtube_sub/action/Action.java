package com.rs.tool.youtube_sub.action;

import com.google.gson.Gson;

public abstract class Action<T> {

    private static Gson gson = new Gson();

    private final String   url;
    private final Class<T> clazz;

    public Action(String url, Class<T> clazz) {
        this.url = url;
        this.clazz = clazz;
    }

    public T run() {
        String response = HttpClient.run(url);
        if (response == null) return null;
        T t = gson.fromJson(response, clazz);
        return t;
    }

}
