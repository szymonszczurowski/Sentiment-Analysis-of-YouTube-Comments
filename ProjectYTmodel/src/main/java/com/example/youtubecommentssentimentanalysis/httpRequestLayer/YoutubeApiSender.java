package com.example.youtubecommentssentimentanalysis.httpRequestLayer;

import com.example.youtubecommentssentimentanalysis.modelsDTO.ResponseModel;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeApiSender {

    private static final HttpClient httpClient = HttpClient.newHttpClient();

    private static final String DEVELOPER_KEY = "AIzaSyABND2t9_Tr3361z1QgHtW01yt3ljZz8gQ";

    private static final ObjectMapper objectMapper = new ObjectMapper();


    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     *
     *  GET https://youtube.googleapis.com/youtube/v3/commentThreads?part=snippet%2Creplies&videoId=mi1ybs8W4y0&key=[YOUR_API_KEY] HTTP/1.1
     *
     *  Accept: application/json
     */

    public static Map<String,List<String>> loadCommentsFromVideoByVideoId(String youtubeURL) throws URISyntaxException, IOException, InterruptedException {

        String videoID = extractVideoIdFromURL(youtubeURL);

        if(!videoID.equals(null))
        {
            HttpRequest httpRequest =  createHttpRequest(videoID);

            HttpResponse<String> httpResponse = sendRequest(httpRequest);

            //tworzymy obiekt ktory reprezentuje komentarze ktore przychodza z responsa yt api
            ResponseModel items = objectMapper.readValue(httpResponse.body(), ResponseModel.class);

            List<String> listOfComments = new ArrayList<>();

            //filtrujemy komentarze i wrzucamy do listy
            items.getItems().stream().forEach(objektItem -> {

                        String comment = objektItem
                                .getSnippets()
                                .getLevelcomments()
                                .getSnippetLevelComment()
                                .getTextOriginal();

                        if(comment.length() < 500)
                            listOfComments.add(comment);

                    }
            );

            Map<String,List<String>> sentimentedCommentByModel = new HashMap<>();
            List<String> listpositive = new ArrayList<>();
            List<String> listnegative = new ArrayList<>();
            List<String> listneutral = new ArrayList<>();

            listOfComments.stream().forEach(comment -> {
                try {
                    String sentimentOfComment = HuggingFaceApiSender
                            .evaluateCommentByModel(comment);

                    if(sentimentOfComment.equals("positive"))
                    {
                        listpositive.add(comment);
                    }
                    else if (sentimentOfComment.equals("neutral"))
                    {
                        listneutral.add(comment);
                    }
                    else if(sentimentOfComment.equals("negative"))
                    {
                        listnegative.add(comment);
                    }

                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });



            sentimentedCommentByModel.put("pozytywne",listpositive);
            sentimentedCommentByModel.put("negatywne",listnegative);
            sentimentedCommentByModel.put("neutralne",listneutral);

            return sentimentedCommentByModel;
        }

        return null;
    }

    private static String extractVideoIdFromURL(String youtubeUrl)
    {

        String videoIdPattern = "(?<=watch\\?v=|\\/videos\\/|\\/v\\/|youtu.be\\/|\\/embed\\/|\\/v\\/|www.youtube.com\\/user\\/[^\\/]+\\/u\\/[\\d]+\\/%7Cvideo_id%=)([a-zA-Z0-9_-]{11})";

        Pattern compiledPattern = Pattern.compile(videoIdPattern);
        Matcher matcher = compiledPattern.matcher(youtubeUrl);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }



    private static HttpRequest createHttpRequest(String videoID) throws URISyntaxException {
        return HttpRequest.newBuilder()
                .uri(new URI("https://youtube.googleapis.com/youtube/v3/commentThreads?part=snippet%2Creplies&maxResults=100&textFormat=plainText&videoId="+videoID+"&fields=items&key="+DEVELOPER_KEY))
                .GET()
                .build();
    }


    private static HttpResponse<String> sendRequest(HttpRequest httpRequest) throws IOException, InterruptedException {
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

}
