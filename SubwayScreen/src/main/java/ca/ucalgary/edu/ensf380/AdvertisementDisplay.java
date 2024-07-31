package ca.ucalgary.edu.ensf380;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.imageio.ImageIO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class AdvertisementDisplay extends JPanel {
    private AdPanel adPanel;
    private Timer timer;
    private List<Ad> ads;
    private int currentAdIndex = 0;
    private ImageIcon mapIcon;
    private BufferedImage mapImage;
    private Map<Integer, Point> trainPositions = new HashMap<>(); // Train number to coordinates
    private TrainMap trainMap; // Declare trainMap

    public AdvertisementDisplay() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Initialize trainMap
        trainMap = new TrainMap();

        // Load the map image
        try {
            mapImage = ImageIO.read(new File("Map/Map.png"));
            mapIcon = new ImageIcon(mapImage);
            System.out.println("Map image loaded successfully.");
        } catch (IOException e) {
            System.err.println("Map image not found: " + e.getMessage());
            mapIcon = null;
        }

        // Create a panel to hold the ad label
        adPanel = new AdPanel();
        adPanel.setPreferredSize(new Dimension(1706, 1067)); // Adjust dimensions as needed

        // Add the ad panel to the layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 2;
        gbc.weighty = 1;
        add(adPanel, gbc);

        // Add a panel for news data below the ad panel
        JPanel newsPanel = new JPanel();
        newsPanel.setBackground(Color.BLUE); // Placeholder color
        newsPanel.setPreferredSize(new Dimension(1706, 100)); // Adjust dimensions as needed
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2; // Span across two columns
        gbc.gridheight = 1;
        gbc.weightx = 2;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        add(newsPanel, gbc);

        // Add a panel for next train data below the news panel
        JPanel nextTrainPanel = new JPanel();
        nextTrainPanel.setBackground(Color.YELLOW); // Placeholder color
        nextTrainPanel.setPreferredSize(new Dimension(1706, 100)); // Adjust dimensions as needed
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // Span across two columns
        gbc.gridheight = 1;
        gbc.weightx = 2;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        add(nextTrainPanel, gbc);

        // Add a panel for weather data to the right of the ad panel
        JPanel weatherPanel = new JPanel();
        weatherPanel.setBackground(Color.LIGHT_GRAY); // Placeholder color
        weatherPanel.setPreferredSize(new Dimension(854, 1600)); // Adjust dimensions as needed
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1; // Span one column
        gbc.gridheight = 3; // Span across three rows
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(weatherPanel, gbc);

        // Add a component listener to handle resizing
        adPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (ads.size() > 0 && adPanel.getWidth() > 0 && adPanel.getHeight() > 0) {
                    Ad ad = ads.get(currentAdIndex / 2 % ads.size());
                    adPanel.setAdImage(ad.getMediaPath());
                }
            }
        });

        // Retrieve advertisements from the database
        ads = DatabaseManager.getAdvertisements();
        System.out.println("Advertisements loaded: " + ads.size());

        // Schedule the ad rotation task
        timer = new Timer();
        timer.schedule(new AdTask(), 0, 10000); // Rotate every 10 seconds

        // Start the SubwaySimulator process
        System.out.println("Calling startSubwaySimulator...");
        startSubwaySimulator();
    }

    private class AdTask extends TimerTask {
        @Override
        public void run() {
            SwingUtilities.invokeLater(() -> {
                if (ads.size() > 0) {
                    if (currentAdIndex % 2 == 0) { // Show map every second interval (10 seconds for ads, 5 seconds for map)
                        if (mapIcon != null) {
                            adPanel.setAdImage(mapIcon.getImage());
                            System.out.println("Displaying map.");
                            updateMapWithTrainData();
                        }
                    } else {
                        Ad ad = ads.get(currentAdIndex / 2 % ads.size());
                        adPanel.setAdImage(ad.getMediaPath());
                        System.out.println("Displaying ad: " + ad.getMediaPath());
                    }
                    currentAdIndex++;
                }
            });
        }
    }
    

    private class AdPanel extends JPanel {
        private Image adImage;
    
        public void setAdImage(String imagePath) {
            try {
                this.adImage = ImageIO.read(new File(imagePath));
                repaint();
            } catch (IOException e) {
                System.err.println("Failed to load ad image: " + e.getMessage());
            }
        }
    
        public void setAdImage(Image image) {
            this.adImage = image;
            repaint();
        }
    
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (adImage != null) {
                int imgWidth = adImage.getWidth(this);
                int imgHeight = adImage.getHeight(this);
                double imgAspect = (double) imgHeight / imgWidth;
    
                int panelWidth = getWidth();
                int panelHeight = getHeight();
                double panelAspect = (double) panelHeight / panelWidth;
    
                int drawWidth, drawHeight;
                if (imgAspect > panelAspect) {
                    drawHeight = panelHeight;
                    drawWidth = (int) (panelHeight / imgAspect);
                } else {
                    drawWidth = panelWidth;
                    drawHeight = (int) (panelWidth * imgAspect);
                }
    
                int drawX = (panelWidth - drawWidth) / 2;
                int drawY = (panelHeight - drawHeight) / 2;
    
                g.drawImage(adImage, drawX, drawY, drawWidth, drawHeight, this);
            }
        }
    }
    

    private void startSubwaySimulator() {
        try {
            System.out.println("Starting SubwaySimulator.jar...");

            // Ensure the output directory exists
            new File("Simulator/out").mkdirs();

            // Set the working directory to the exe folder inside Simulator
            ProcessBuilder builder = new ProcessBuilder("java", "-jar", "SubwaySimulator.jar", "out");
            builder.directory(new File("Simulator/exe"));

            Process process = builder.start();

            System.out.println("SubwaySimulator process started...");

            // Create a new thread to read the output from the SubwaySimulator.jar
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("Simulator output: " + line); // Ensure this is printed
                        processTrainData(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Capture any errors from the process
            new Thread(() -> {
                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                    String line;
                    while ((line = errorReader.readLine()) != null) {
                        System.err.println("Simulator error: " + line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processTrainData(String line) {
        System.out.println("Processing train data: " + line);
    
        if (line.equals("Train positions:")) {
            return;
        }
    
        String[] sections = line.split(": ");
        if (sections.length != 2) {
            System.err.println("Unrecognized train data format: " + line);
            return;
        }
    
        String[] trainData = sections[1].split(", ");
        for (String train : trainData) {
            String[] trainParts = train.split("[()]+");
            if (trainParts.length < 2) {
                System.err.println("Unrecognized train data format: " + train);
                continue;
            }
    
            String trainNumberStr = trainParts[0].substring(1).trim();
            String stationCode = trainParts[1].substring(0, 3).trim(); // Extracting station code correctly
    
            try {
                int trainNumber = Integer.parseInt(trainNumberStr);
                Point coordinates = trainMap.getStationCoordinates(stationCode);
                if (coordinates != null) {
                    System.out.println("Parsed train data - Train number: " + trainNumber + ", coordinates: (" + coordinates.x + ", " + coordinates.y + ")");
                    trainPositions.put(trainNumber, coordinates);
                } else {
                    System.err.println("No coordinates found for station: " + stationCode);
                }
            } catch (NumberFormatException e) {
                System.err.println("Error parsing train number or coordinates: " + e.getMessage());
            }
        }
    }

    private void updateMapWithTrainData() {
        if (mapImage == null) {
            System.err.println("Map image is null, cannot update train positions.");
            return;
        }
    
        BufferedImage updatedMap = new BufferedImage(mapImage.getWidth(), mapImage.getHeight(), mapImage.getType());
        Graphics2D g2d = updatedMap.createGraphics();
        g2d.drawImage(mapImage, 0, 0, null);
    
        // Get the dimensions of the map image and the panel
        int imgWidth = mapImage.getWidth();
        int imgHeight = mapImage.getHeight();
        int panelWidth = adPanel.getWidth();
        int panelHeight = adPanel.getHeight();
    
        double scaleX = (double) panelWidth / imgWidth;
        double scaleY = (double) panelHeight / imgHeight;
    
        // Draw all train positions
        for (Map.Entry<Integer, Point> entry : trainPositions.entrySet()) {
            int trainNumber = entry.getKey();
            Point pos = entry.getValue();
            
            // Scale the coordinates
            int scaledX = (int) (pos.x * scaleX);
            int scaledY = (int) (pos.y * scaleY);
    
            g2d.setColor(Color.RED);
            g2d.fillOval(scaledX - 5, scaledY - 5, 10, 10);
            System.out.println("Drawing train number " + trainNumber + " on map at coordinates: (" + scaledX + ", " + scaledY + ")");
        }
    
        g2d.dispose();
        adPanel.setAdImage(updatedMap);
        System.out.println("Map updated with new train positions.");
    }
    


    public static void main(String[] args) {
        // Create the main frame for the application
        JFrame frame = new JFrame("Subway Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(2560, 1600); // Adjust the size as needed
        frame.add(new AdvertisementDisplay());
        frame.setVisible(true);
    }

}
