package com.rs.tool.youtube_sub.action;

import com.rs.tool.youtube_sub.model.ListInfo;

public class GetListInfo extends Action<ListInfo> {

    public GetListInfo(String listId) {
        super("https://api.zhuwei.me/v1/videos/playlists/" + listId, ListInfo.class);
    }

}
