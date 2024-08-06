package ca.ucalgary.edu.ensf380;

import javax.swing.*;

/**
     * 
     * @param args command line arguments
     */
public class MainApp {
    public static void main(String[] args) {
        System.out.println("Updated Code Running");
        if (args.length != 1) {
            System.err.println("Usage: java MainApp <trainNumber>");
            System.exit(1);
        }

        int userTrain = Integer.parseInt(args[0]);

        JFrame frame = new JFrame("Subway Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(2560, 1600); // Adjust the size as needed
        frame.add(new AdvertisementDisplay(userTrain));
        frame.setVisible(true);
    }
}
