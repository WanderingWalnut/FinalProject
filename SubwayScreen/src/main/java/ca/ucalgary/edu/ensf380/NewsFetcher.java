package ca.ucalgary.edu.ensf380;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JTextArea;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsFetcher {
    private static final String API_KEY = "6IoGr7aC0nWvQaD8yIMnwtWKgl0jXocbvvUR8AAZ";
    private static final String API_URL = "https://api.thenewsapi.com/v1/news/all?api_token=" + API_KEY;

    /**
     * @param keyword is passed as a String type
     * @throw IOException if error is encountered
     * @return newsTitles is returned as List<String> type
     */
    public static List<String> fetchNews(String keyword) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String urlString = API_URL + "&search=" + keyword;
        System.out.println("Fetching news from URL: " + urlString); // Debugging line

        Request request = new Request.Builder()
                .url(urlString)
                .build();

        Response response = client.newCall(request).execute();
        int responseCode = response.code();
        System.out.println("Response Code: " + responseCode); // Debugging line

        if (responseCode == 200) {
            String responseBody = response.body().string();
            System.out.println("Response: " + responseBody); // Debugging line

            // Parse JSON response
            List<String> newsTitles = new ArrayList<>();
            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
            JsonArray articles = jsonObject.getAsJsonArray("data");
            for (JsonElement article : articles) {
                String title = article.getAsJsonObject().get("title").getAsString();
                newsTitles.add(title);
            }
            return newsTitles;
        } else {
            throw new IOException("Failed to fetch news: " + responseCode);
        }
    }

    /**
     * @param textArea is returned as a JTexArea Type, newsTitles is returned as
     *                 List<String> type
     * 
     */
    public static void startNewsScrolling(JTextArea textArea, List<String> newsTitles) {
        Timer timer = new Timer();
        final int[] index = { 0 };
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!newsTitles.isEmpty()) {
                    textArea.setText(newsTitles.get(index[0]));
                    index[0] = (index[0] + 1) % newsTitles.size();
                }
            }
        }, 0, 5000); // Change headline every 5 seconds
    }
}
