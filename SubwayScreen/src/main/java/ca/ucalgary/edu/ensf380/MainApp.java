package ca.ucalgary.edu.ensf380;

import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java MainApp <line> <trainNumber>");
            System.exit(1);
        }

        String line = args[0];
        int trainNumber = Integer.parseInt(args[1]);

        JFrame frame = new JFrame("Subway Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(2560, 1600); // Adjust the size as needed
        frame.add(new AdvertisementDisplay(line, trainNumber));
        frame.setVisible(true);
    }
}
