package ca.ucalgary.edu.ensf380;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;

import javax.swing.ImageIcon;

public class WeatherFetch {

    // Main method to fetch and parse weather data
    public static void main(String[] args) {
        try {
            String htmlContent = fetchHTML(
                    "https://wttr.in/Calgary?format=" + URLEncoder.encode("%t+%c", StandardCharsets.UTF_8));
            System.out.println("Fetched HTML Content: " + htmlContent); // For debugging
            parseHTML(htmlContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to fetch HTML content from a URL using HttpClient
    public static String fetchHTML(String urlString) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlString))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    // Method to parse the HTML content
    public static String parseHTML(String html) {
        // Adjust the pattern to handle the emoji and special characters
        String weatherPattern = "([-+]?\\d+°C)\\s*(.+)";

        // Compile the pattern
        Pattern weatherRegex = Pattern.compile(weatherPattern);

        // Match the pattern
        Matcher weatherMatcher = weatherRegex.matcher(html);

        // Extract and return weather data
        if (weatherMatcher.find()) {
            String temperature = weatherMatcher.group(1);
            String condition = weatherMatcher.group(2).trim().replaceAll("[+]", ""); // Clean up condition
            return temperature + " " + condition;
        } else {
            return "Weather data not found.";
        }
    }

    // Method to get the file path for the weather condition image
    public static ImageIcon getWeatherVisual(String condition) {
        String imagePath = "data/Weather/";
    
        switch (condition) {
            case "☀️":
                imagePath += "Sunny.png";
                break;
            case "🌧️":
                imagePath += "Rainy.png";
                break;
            case "⛅":
                imagePath += "PartlyCloudy.png";
                break;
            case "☁️":
                imagePath += "Cloudy.png";
                break;
            case "❄️":
                imagePath += "Snowy.png";
                break;
            default:
                imagePath = null;
                break;
        }
    
        if (imagePath != null) {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                return new ImageIcon(imagePath);
            } else {
                System.err.println("Image file not found: " + imagePath);
            }
        } else {
            System.err.println("Invalid condition: " + condition);
        }
        return null;
    }
}