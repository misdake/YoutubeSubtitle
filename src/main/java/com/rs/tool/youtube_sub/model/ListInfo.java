package com.rs.tool.youtube_sub.model;

public class ListInfo {

    public Meta     meta;
    public Response response;

    public static class Response {
        public Playlist playlist;
    }

    public static class Playlist {
        public int      videos_count;
        public String[] videos;
    }

}
