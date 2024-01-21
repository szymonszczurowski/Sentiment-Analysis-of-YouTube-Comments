package com.example.youtubecommentssentimentanalysis.httpRequestLayer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class HuggingFaceApiSender {

    private static HttpClient httpClient = HttpClient.newHttpClient();

    private static final String DEVELOPER_KEY = "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";

    private static final ObjectMapper objectMapper = new ObjectMapper();
    


    public static String evaluateCommentByModel(String commentFromYT) throws URISyntaxException, IOException, InterruptedException {


        String requestBody = getJSON(commentFromYT);
        HttpRequest httpRequest = getHttpRequest(requestBody);

        //wysylamy pierwsze rzadanie
        HttpResponse<String> httpResponse = sendHttpRequestToHuggingFaceApi(httpRequest);


        if(httpResponse.body().contains("Model lxyuan/distilbert-base-multilingual-cased-sentiments-student is currently loading"))
        {
            System.out.println("Model jeszcze nie gotowy, czekam 20 sekund na zaladowanie");
            Thread.sleep(20*1000);
            httpResponse = sendHttpRequestToHuggingFaceApi(httpRequest);
            System.out.println("Ponownie wyslalem rzadanie do API!");
        }

        //konwertowanie jsona na obiekt javowy
        List<List<Map<String,String>>> rawResult = objectMapper.readValue(httpResponse.body(), new TypeReference<>() {});


        //w tym momencie mamy czy komentarz jest pozytywny/negatywny/neutralny
        String sentiment = rawResult
                .get(0).stream()
                .findFirst()
                .get()
                .get("label");


        return sentiment;
    }


    private static HttpResponse<String> sendHttpRequestToHuggingFaceApi(HttpRequest httpRequest) throws IOException, InterruptedException {
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }


    private static HttpRequest getHttpRequest(String jsonBody) throws URISyntaxException {
        return HttpRequest.newBuilder()
            .uri(new URI("https://api-inference.huggingface.co/models/lxyuan/distilbert-base-multilingual-cased-sentiments-student"))
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer "+ DEVELOPER_KEY)
            .build();
    }

    private static String getJSON(String comment) throws JsonProcessingException {
        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("inputs", comment);
        return objectMapper.writeValueAsString(requestBodyMap);
    }


}
