package com.rs.tool.youtube_sub.action;

import com.rs.tool.youtube_sub.model.SubContent;

public class GetSubContent extends Action<SubContent> {

    public GetSubContent(String url) {
        super(url, SubContent.class);
    }

}
