package com.example.youtubecommentssentimentanalysis.viewLayer;

import com.example.youtubecommentssentimentanalysis.httpRequestLayer.YoutubeApiSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainPageController {


    @GetMapping("/")
    public String getMainPage()
    {
        return "index";
    }

    @GetMapping("/wykres")
    public String getChart(Model model,@RequestParam(value = "filmURL") String url) throws URISyntaxException, IOException, InterruptedException {
        System.out.println(url);
        Map<String, List<String>> listMap=  YoutubeApiSender.loadCommentsFromVideoByVideoId(url);
        System.out.println("to sa komentarze pozytywne: "+listMap.get("pozytywne"));
        System.out.println("to sa komentarze negatywne: "+listMap.get("negatywne"));
        System.out.println("to sa komentarze neutralne: "+listMap.get("neutralne"));


        model.addAttribute("tablica",listMap);
        return "chart";
    }










}
