package com.rs.tool.youtube_sub;

import com.rs.tool.youtube_sub.action.GetListInfo;
import com.rs.tool.youtube_sub.action.GetSubContent;
import com.rs.tool.youtube_sub.action.GetVideoInfo;
import com.rs.tool.youtube_sub.action.HttpClient;
import com.rs.tool.youtube_sub.model.ListInfo;
import com.rs.tool.youtube_sub.model.SubContent;
import com.rs.tool.youtube_sub.model.VideoInfo;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CppCon {
    public static void main(String[] args) throws IOException {

        HttpClient.init("a2d09c7d76fced01f8be4b1f4cce8bec");

        List<String> done = new ArrayList<>();
        if (new File("downloaded.txt").exists()) {
            done = Files.readAllLines(new File("downloaded.txt").toPath());
        }

        List<String> todo;
        ListInfo listInfo = new GetListInfo("PLHTh1InhhwT6V9RVdFRoCG_Pm5udDxG1c").run();
        todo = new ArrayList<>(Arrays.asList(listInfo.response.playlist.videos));

        for (String videoId : todo) {
            if (done.contains(videoId)) {
                System.out.println("skip: " + videoId);
            } else {
                boolean success = getVideo(videoId);
                if (!success) continue;
                done.add(videoId);
                StringBuilder b = new StringBuilder();
                for (String s : done) {
                    b.append(s).append("\n");
                }
                Files.write(new File("downloaded.txt").toPath(), b.toString().getBytes(StandardCharsets.UTF_8));
            }
        }

    }

    private static boolean getVideo(String videoId) {
        System.out.println("video: " + videoId);
        try {
            VideoInfo videoInfo = new GetVideoInfo(videoId).run();
            String title = videoInfo.response.captions.title;
            title = title.replaceAll("CppCon 2018: ", "");
            title = title.replaceAll(":", "_");
            title = title.replaceAll("[?]", "_");

            for (VideoInfo.CaptionInfo caption : videoInfo.response.captions.available_captions) {
                if (caption.language.equals("en") || caption.language.contains("zh")) {
                    boolean success = getSub(title, caption);
                    if (!success) return false;
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("video fail");
            return false;
        }
    }

    private static boolean getSub(String title, VideoInfo.CaptionInfo caption) throws IOException {
        System.out.println("language: " + caption.language);

        try {
            SubContent subContent = new GetSubContent(caption.caption_content_url).run();

            String r = SubUtil.srt2ass(subContent.contents.content);
            r = r.replaceAll("&#39;", "'");
            r = r.replaceAll("expend", "exp·end");
            r = r.replaceAll("你支", "你·支");
            if (subContent.contents.language.contains("zh")) {
                r = r.replaceAll(" ", "");
            }

            Files.write(new File(title + "__" + subContent.contents.language + ".ass").toPath(), r.getBytes(), StandardOpenOption.CREATE);
            return true;
        } catch (Exception e) {
            System.out.println("sub fail");
            return false;
        }
    }
}
