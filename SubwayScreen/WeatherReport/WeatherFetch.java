import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeatherFetch {
    
    // Main method to fetch and parse weather data
    public static void main(String[] args) {
        try {
            String htmlContent = fetchHTML("http://wttr.in/Calgary,CA");
            System.out.println(htmlContent); // For debugging
            parseHTML(htmlContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to fetch HTML content from a URL
    private static String fetchHTML(String urlString) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
        }
        return result.toString();
    }

    // Method to parse the HTML content
    private static void parseHTML(String html) {
        String tempPattern = "^\\+?\\d{2}(\\(\\d{2}\\))? °C$";
        String conditionPattern = "Sunny|Patchy rain ne..."; // Adjust based on actual content

        // Compile the patterns
        Pattern tempRegex = Pattern.compile(tempPattern);
        Pattern conditionRegex = Pattern.compile(conditionPattern);

        // Match the patterns
        Matcher tempMatcher = tempRegex.matcher(html);
        Matcher conditionMatcher = conditionRegex.matcher(html);

        // Extract and print weather data
        if (tempMatcher.find()) {
            System.out.println("Temperature: " + tempMatcher.group());
        } else {
            System.out.println("Temperature data not found.");
        }

        if (conditionMatcher.find()) {
            System.out.println("Condition: " + conditionMatcher.group());
        } else {
            System.out.println("Condition data not found.");
        }
    }
}
