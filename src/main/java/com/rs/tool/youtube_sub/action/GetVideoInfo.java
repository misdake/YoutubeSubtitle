package com.rs.tool.youtube_sub.action;

import com.rs.tool.youtube_sub.model.VideoInfo;

public class GetVideoInfo extends Action<VideoInfo> {

    public GetVideoInfo(String videoId) {
        super("https://api.zhuwei.me/v1/captions/" + videoId, VideoInfo.class);
    }

}
