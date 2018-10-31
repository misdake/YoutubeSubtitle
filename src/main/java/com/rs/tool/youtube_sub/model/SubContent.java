package com.rs.tool.youtube_sub.model;

public class SubContent {

    public Meta meta;
    public Contents contents;

    public static class Contents {
        public String language;
        public boolean multilanguage;
        public boolean notimeline;
        public String content;
    }

}
