package ca.ucalgary.edu.ensf380;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;
import java.util.Timer;
import java.util.TimerTask;

public class NewsFetcher {
    private static final String API_KEY = "6IoGr7aC0nWvQaD8yIMnwtWKgl0jXocbvvUR8AAZ";
    private static final String API_URL = "https://api.thenewsapi.com/v1/news/headlines?api_token=" + API_KEY;

    public static List<String> fetchNews(String keyword) throws Exception {
        String urlString = API_URL + "&search=" + keyword;
        System.out.println("Fetching news from URL: " + urlString); // Debugging line
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        System.out.println("Response Code: " + responseCode); // Debugging line

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        System.out.println("Response: " + response.toString()); // Debugging line

        // Parse JSON response (simple example, use a library like Jackson or Gson for more complex parsing)
        List<String> newsTitles = new ArrayList<>();
        String[] articles = response.toString().split("\"title\":\"");
        for (int i = 1; i < articles.length; i++) {
            String title = articles[i].split("\",")[0];
            newsTitles.add(title);
        }
        return newsTitles;
    }

    public static void startNewsScrolling(JTextArea textArea) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String currentText = textArea.getText();
                if (!currentText.isEmpty()) {
                    textArea.setText(currentText.substring(1) + currentText.charAt(0));
                }
            }
        }, 0, 150);
    }
}


