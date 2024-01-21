package com.example.youtubecommentssentimentanalysis;

import com.example.youtubecommentssentimentanalysis.httpRequestLayer.HuggingFaceApiSender;
import com.example.youtubecommentssentimentanalysis.httpRequestLayer.YoutubeApiSender;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class YouTubeCommentsSentimentAnalysisApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(YouTubeCommentsSentimentAnalysisApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {


//        Map<String, List<String>> listMap=  YoutubeApiSender.loadCommentsFromVideoByVideoId("https://www.youtube.com/watch?v=ZsgM-FccrC8");
//        System.out.println("to sa komentarze pozytywne: "+listMap.get("pozytywne"));
//        System.out.println("to sa komentarze negatywne: "+listMap.get("negatywne"));
//        System.out.println("to sa komentarze neutralne: "+listMap.get("neutralne"));
//        System.out.println(HuggingFaceApiSender.getModel("i hate you").get("negative"));


    }
}
