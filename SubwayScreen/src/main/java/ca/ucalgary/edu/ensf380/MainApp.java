package ca.ucalgary.edu.ensf380;

import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        // Create the main frame for the application
        JFrame frame = new JFrame("Subway Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(2560, 1600); // Adjust the size as needed

        // Create and add the AdvertisementDisplay panel
        AdvertisementDisplay adDisplay = new AdvertisementDisplay();
        frame.add(adDisplay);

        frame.setVisible(true);
    }
}
