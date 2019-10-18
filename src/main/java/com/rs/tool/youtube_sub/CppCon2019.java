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

public class CppCon2019 {
    public static void main(String[] args) throws IOException {

        HttpClient.init("a2d09c7d76fced01f8be4b1f4cce8bec");
        List<String> done = new ArrayList<>();
        if (new File("downloaded.txt").exists()) {
            done = Files.readAllLines(new File("downloaded.txt").toPath());
        }

        List<String> todo;
        ListInfo listInfo = new GetListInfo("PLHTh1InhhwT6KhvViwRiTR7I5s09dLCSw").run();
        todo = new ArrayList<>(Arrays.asList(listInfo.response.playlist.videos));

        for (int i = 0; i < todo.size(); i++) {
            String videoId = todo.get(i);
            if (done.contains(videoId)) {
                System.out.println("skip: " + (i + 1) + " " + videoId);
            } else {
                boolean success = getVideo(i, videoId);
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

    private static boolean getVideo(int index, String videoId) {
        System.out.println("video: " + (index + 1) + " " + videoId);
        try {
            VideoInfo videoInfo = new GetVideoInfo(videoId).run();
            String title = videoInfo.response.captions.title;
            title = title.replaceAll("CppCon 2019: ", "");
            title = title.replaceAll(":", "_");
            title = title.replaceAll("[?]", "_");

            for (VideoInfo.CaptionInfo caption : videoInfo.response.captions.available_captions) {
                if (caption.language.equals("en")/* || caption.language.contains("zh")*/) {
                    boolean success = getSub(index, title, caption);
                    if (!success) return false;
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("video fail");
            return false;
        }
    }

    private static boolean getSub(int index, String title, VideoInfo.CaptionInfo caption) throws IOException {
        System.out.println("language: " + caption.language);

        try {
            SubContent subContent = new GetSubContent(caption.caption_content_url).run();

            String r = SubUtil.srt2ass(subContent.contents.content);
            r = SubUtil.avoidWords(r, subContent.contents.language);

            title = title.replaceAll("\"", "");
            title = title.replaceAll("\\\\", "");

            Files.write(new File((index + 1) + ". " + title + "__" + subContent.contents.language + ".ass").toPath(), r.getBytes(), StandardOpenOption.CREATE);
            return true;
        } catch (Exception e) {
            System.out.println("sub fail");
            e.printStackTrace();
            return false;
        }
    }
}
