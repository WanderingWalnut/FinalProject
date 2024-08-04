package ca.ucalgary.edu.ensf380;

import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class CurrentTimePanel extends JPanel {
    private JLabel timeLabel;
    private SimpleDateFormat timeFormat;

    public CurrentTimePanel() {
        timeLabel = new JLabel();
        timeFormat = new SimpleDateFormat("HH:mm:ss");

        timeLabel.setFont(new Font("Serif", Font.PLAIN, 30)); // Adjust font size as needed
        timeLabel.setForeground(Color.WHITE); // Adjust color as needed
        timeLabel.setHorizontalAlignment(JLabel.CENTER);

        add(timeLabel);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateTime();
            }
        }, 0, 1000); // Update every second
    }

    private void updateTime() {
        String currentTime = timeFormat.format(new Date());
        timeLabel.setText(currentTime);
    }
}
