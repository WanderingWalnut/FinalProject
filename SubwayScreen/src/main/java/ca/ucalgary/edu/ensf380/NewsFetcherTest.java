package ca.ucalgary.edu.ensf380;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.JTextArea;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

class NewsFetcherTest {

    @BeforeEach
    void setUp() throws Exception {
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Test
    void testFetchNews() {
        try {
            // Testing with a sample keyword
            List<String> newsTitles = NewsFetcher.fetchNews("technology");
            assertNotNull(newsTitles, "News titles should not be null");
            assertFalse(newsTitles.isEmpty(), "News titles list should not be empty");
            System.out.println("Fetched news titles: " + newsTitles);
        } catch (IOException e) {
            fail("Fetching news failed with IOException: " + e.getMessage());
        }
    }

    @Test
    void testStartNewsScrolling() {
        // Creating a JTextArea and sample news titles for testing
        JTextArea textArea = new JTextArea();
        List<String> newsTitles = Arrays.asList("News 1", "News 2", "News 3");

        // Start the news scrolling in a separate thread to simulate real-time scrolling
        NewsFetcher.startNewsScrolling(textArea, newsTitles);

        // Wait for a few cycles of news scrolling
        try {
            Thread.sleep(11000); // Sleep for 11 seconds (2 complete cycles and part of 3rd)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if the text area has been updated with news titles
        String currentText = textArea.getText();
        assertTrue(newsTitles.contains(currentText), "Text area should contain one of the news titles");

        System.out.println("Current text in text area: " + currentText);
    }
}
