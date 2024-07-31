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

public class WeatherFetch {

    // Main method to fetch and parse weather data
    public static void main(String[] args) {
        try {
            String htmlContent = fetchHTML("https://wttr.in/Calgary?format=" + URLEncoder.encode("%t+%c", StandardCharsets.UTF_8));
            System.out.println("Fetched HTML Content: " + htmlContent); // For debugging
            parseHTML(htmlContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to fetch HTML content from a URL using HttpClient
    private static String fetchHTML(String urlString) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlString))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    // Method to parse the HTML content
    private static void parseHTML(String html) {
        // Adjust the pattern to handle the emoji and special characters
        String weatherPattern = "([-+]?\\d+°C)\\s*(.+)";
        
        // Compile the pattern
        Pattern weatherRegex = Pattern.compile(weatherPattern);
        
        // Match the pattern
        Matcher weatherMatcher = weatherRegex.matcher(html);

        // Extract and print weather data
        if (weatherMatcher.find()) {
            String temperature = weatherMatcher.group(1);
            String condition = weatherMatcher.group(2).trim().replaceAll("[+]", ""); // Clean up condition

            System.out.println("Temperature: " + temperature);
            System.out.println("Condition: " + condition);

            displayWeatherVisual(condition);
        } else {
            System.out.println("Weather data not found.");
        }
    }

    // Method to display a larger visual representation of the weather condition
    private static void displayWeatherVisual(String condition) {
        switch (condition) {
            case "☀️":
                System.out.println("    \\   /    ");
                System.out.println("     .-.     ");
                System.out.println("  ― (   ) ―  ");
                System.out.println("     `-’     ");
                System.out.println("    /   \\    ");
                System.out.println("Sunny");
                break;
            case "🌧️":
                System.out.println("     .-.     ");
                System.out.println("    (   ).   ");
                System.out.println("   (___(__)  ");
                System.out.println("    ‘ ‘ ‘ ‘  ");
                System.out.println("   ‘ ‘ ‘ ‘   ");
                System.out.println("Rainy");
                break;
            case "⛅":
                System.out.println("   \\  /      ");
                System.out.println(" _ /\"\".-.    ");
                System.out.println("   \\_(   ).  ");
                System.out.println("   /(___(__) ");
                System.out.println("Partly Cloudy");
                break;
            case "☁️":
                System.out.println("             ");
                System.out.println("     .--.    ");
                System.out.println("  .-(    ).  ");
                System.out.println(" (___.__)__) ");
                System.out.println("Cloudy");
                break;
            case "❄️":
                System.out.println("     *  *    ");
                System.out.println("   *     *   ");
                System.out.println("  *   *   *  ");
                System.out.println("   *     *   ");
                System.out.println("     *  *    ");
                System.out.println("Snowy");
                break;
            default:
                System.out.println("Weather condition not recognized.");
                break;
        }
    }
}
