package com.rs.tool.youtube_sub;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class SubUtil {

    public static String srt2ass(String srt) {
        Process process = null;

        try {
            //write content to file
            Files.write(new File("sub.srt").toPath(), srt.getBytes(), StandardOpenOption.CREATE);

            //convert
            process = new ProcessBuilder("ffmpeg", "-i", "sub.srt", "sub.ass").start();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

            //read content from file
            byte[] bytes = Files.readAllBytes(new File("sub.ass").toPath());
            String r = new String(bytes, StandardCharsets.UTF_8);
            return r;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (process != null) {
                process.destroyForcibly();
            }
            new File("sub.srt").delete();
            new File("sub.ass").delete();
        }
    }

}
