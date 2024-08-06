package ca.ucalgary.edu.ensf380;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;

class CurrentTimePanelTest {
    private CurrentTimePanel currentTimePanel;

    @BeforeEach
    void setUp() throws Exception {
        currentTimePanel = new CurrentTimePanel();
    }

    @AfterEach
    void tearDown() throws Exception {
        currentTimePanel = null;
    }

    @Test
    void testCurrentTimePanel() {
        assertNotNull(currentTimePanel, "CurrentTimePanel object should be initialized");

        // Check if the timeLabel is properly initialized
        JLabel timeLabel = (JLabel) currentTimePanel.getComponent(0);
        assertNotNull(timeLabel, "timeLabel should be initialized");
        assertEquals(Font.PLAIN, timeLabel.getFont().getStyle(), "Font style should be plain");
        assertEquals(30, timeLabel.getFont().getSize(), "Font size should be 30");
        assertEquals(Color.WHITE, timeLabel.getForeground(), "Foreground color should be white");
        assertEquals(JLabel.CENTER, timeLabel.getHorizontalAlignment(), "Horizontal alignment should be centered");

        // Verify that the time label updates (this indirectly tests the timeFormat)
        String initialTime = timeLabel.getText();
        try {
            Thread.sleep(1100); // Sleep for a little more than one second to allow time to update
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String updatedTime = timeLabel.getText();
        assertNotEquals(initialTime, updatedTime, "The time label should update every second");
    }
}
