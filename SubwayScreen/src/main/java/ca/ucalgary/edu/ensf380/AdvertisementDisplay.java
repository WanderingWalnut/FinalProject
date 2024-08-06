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
import java.io.StringReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.imageio.ImageIO;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.regex.Pattern;
import ca.ucalgary.edu.ensf380.weather.WeatherFetch;

public class AdvertisementDisplay extends JPanel {
    private AdPanel adPanel;
    private Timer timer;
    private List<Ad> ads;
    private int currentAdIndex = 0;
    private ImageIcon mapIcon;
    private BufferedImage mapImage;
    private Map<Integer, Point> trainPositions = new HashMap<>(); // Train number to coordinates
    private TrainMap trainMap = new TrainMap(); // Initialize TrainMap
    private JLabel weatherVisualLabel; // Label to display weather visual
    private JLabel weatherLabel; // Label to display weather data
    private String weatherCondition; // Store the weather condition
    private JLabel weatherConditionLabel; // Label to display weather condition text
    private JTextArea newsTextArea; // Text area to display news data
    private CurrentTimePanel currentTimePanel; // Panel to display current time
    private TrainTracker trainTracker; // Train tracker instance
    private JLabel previousStationLabel;
    private JLabel currentStationLabel;
    private JLabel nextStationsLabel;
    private int trackedTrainNumber;
    private int userTrain;

    public AdvertisementDisplay(int userTrain) {
        this.userTrain = userTrain;
        this.trainTracker = new TrainTracker(userTrain);
        // rest of your initialization code
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

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
        newsPanel.setPreferredSize(new Dimension(1706, 70)); // Adjust dimensions as needed
        newsPanel.setLayout(new BorderLayout());
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2; // Span across two columns
        gbc.gridheight = 1;
        gbc.weightx = 2;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        add(newsPanel, gbc);

        // Initialize the news text area
        newsTextArea = new JTextArea();
        newsTextArea.setEditable(false);
        newsTextArea.setLineWrap(true);
        newsTextArea.setWrapStyleWord(true);
        newsTextArea.setFont(new Font("Serif", Font.PLAIN, 24));
        newsTextArea.setBackground(Color.BLUE); // Ensure the background matches the panel
        newsTextArea.setForeground(Color.WHITE); // Set text color to white

        JScrollPane newsScrollPane = new JScrollPane(newsTextArea);
        newsPanel.add(newsScrollPane, BorderLayout.CENTER);

        // Fetch and display news
        fetchAndDisplayNews("Calgary"); // Replace "Calgary" with the desired keyword

        // Initialize TrainTracker
        trainTracker = new TrainTracker(userTrain);

        // Add a panel for next train data below the news panel
        JPanel nextTrainPanel = new JPanel();
        nextTrainPanel.setBackground(Color.YELLOW); // Placeholder color
        nextTrainPanel.setPreferredSize(new Dimension(1706, 100)); // Adjust dimensions as needed
        nextTrainPanel.setLayout(new BoxLayout(nextTrainPanel, BoxLayout.Y_AXIS));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // Span across two columns
        gbc.gridheight = 1;
        gbc.weightx = 2;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        add(nextTrainPanel, gbc);

        previousStationLabel = new JLabel("Previous Station: Skyline Heights Station", SwingConstants.CENTER);
        previousStationLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        previousStationLabel.setForeground(Color.BLACK);

        currentStationLabel = new JLabel("Current Station: Cedar Heights Station", SwingConstants.CENTER);
        currentStationLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        currentStationLabel.setForeground(Color.BLACK);

        nextStationsLabel = new JLabel(
                "<html>Next Stations:<br>Hillside Station<br>North Hills Station<br>Bayview Heights Station</html>",
                SwingConstants.CENTER);
        nextStationsLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        nextStationsLabel.setForeground(Color.BLACK);

        nextTrainPanel.add(previousStationLabel);
        nextTrainPanel.add(currentStationLabel);
        nextTrainPanel.add(nextStationsLabel);

        // Add a panel for weather data to the right of the ad panel
        JPanel weatherPanel = new JPanel();
        weatherPanel.setBackground(Color.BLACK); // Placeholder color
        weatherPanel.setPreferredSize(new Dimension(854, 1600)); // Adjust dimensions as needed
        weatherPanel.setLayout(new BoxLayout(weatherPanel, BoxLayout.Y_AXIS)); // Set BoxLayout for vertical alignment

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1; // Span one column
        gbc.gridheight = 3; // Span across three rows
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(weatherPanel, gbc); // Ensure correct constraints are used

        // Add a JLabel to display weather data
        weatherLabel = new JLabel("Fetching weather...", SwingConstants.CENTER);
        weatherLabel.setFont(new Font("Serif", Font.PLAIN, 30)); // Adjust font size as needed
        weatherLabel.setForeground(Color.WHITE); // Adjust color as needed
        weatherVisualLabel = new JLabel("", SwingConstants.CENTER); // Center-align text and image
        weatherConditionLabel = new JLabel("", SwingConstants.CENTER); // Label for weather condition
        weatherConditionLabel.setFont(new Font("Serif", Font.PLAIN, 30)); // Adjust font size as needed
        weatherConditionLabel.setForeground(Color.WHITE); // Adjust color as needed

        // Initialize the CurrentTimePanel
        currentTimePanel = new CurrentTimePanel();

        // Add weatherLabel, weatherVisualLabel, weatherConditionLabel, and
        // currentTimePanel to weatherPanel
        weatherLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        weatherVisualLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        weatherConditionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentTimePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentTimePanel.setBackground(Color.BLACK); // Adjust color as needed

        weatherPanel.add(Box.createVerticalGlue()); // Add vertical space
        weatherPanel.add(weatherLabel);
        weatherPanel.add(Box.createVerticalStrut(20)); // Add space between the labels
        weatherPanel.add(weatherVisualLabel);
        weatherPanel.add(Box.createVerticalStrut(20)); // Add space between the labels
        weatherPanel.add(weatherConditionLabel); // Add weather condition label
        weatherPanel.add(Box.createVerticalStrut(20)); // Add space between the labels
        weatherPanel.add(currentTimePanel); // Add current time panel
        weatherPanel.add(Box.createVerticalGlue()); // Add vertical space

        // Add a component listener to handle the size of weatherVisualLabel
        weatherVisualLabel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateWeatherVisual();
            }
        });

        // Add a component listener to handle the size of weatherVisualLabel
        weatherVisualLabel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateWeatherVisual();
            }
        });

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

        // Fetch and display weather data
        fetchAndDisplayWeather();
    }

    // Modify the fetchAndDisplayWeather method in AdvertisementDisplay class
    private void fetchAndDisplayWeather() {
        new Thread(() -> {
            try {
                String weatherData = WeatherFetch.fetchHTML(
                        "https://wttr.in/Calgary?format=" + URLEncoder.encode("%t+%c", StandardCharsets.UTF_8));
                String parsedWeather = WeatherFetch.parseHTML(weatherData);
                String[] weatherParts = parsedWeather.split(" ");
                weatherCondition = weatherParts[1]; // Extract condition

                String conditionText = getConditionText(weatherCondition);

                SwingUtilities.invokeLater(() -> {
                    weatherLabel.setText(parsedWeather);
                    weatherLabel.setFont(new Font("Serif", Font.PLAIN, 24)); // Ensure larger font size is set
                    weatherLabel.setForeground(Color.WHITE); // Ensure the font color is set to white
                    weatherConditionLabel.setText(conditionText); // Set the weather condition text
                    updateWeatherVisual();
                });
            } catch (Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> weatherLabel.setText("Weather data not found."));
            }
        }).start();
    }

    private String getConditionText(String condition) {
        switch (condition) {
            case "☀️":
                return "Sunny";
            case "🌧️":
                return "Rainy";
            case "⛅":
            case "⛅️": // Note the different character variations
                return "Partly Cloudy";
            case "☁️":
                return "Cloudy";
            case "❄️":
                return "Snowy";
            case "🌦":
                return "Showers";
            case "⛈️":
                return "Thunderstorm";
            default:
                return "Weather condition not recognized.";
        }
    }

    private class AdTask extends TimerTask {
        @Override
        public void run() {
            SwingUtilities.invokeLater(() -> {
                if (ads.size() > 0) {
                    if (currentAdIndex % 2 == 0) { // Show map every second interval (10 seconds for ads, 5 seconds for
                                                   // map)
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

        int colonIndex = line.indexOf(": ");
        if (colonIndex == -1) {
            System.err.println("Unrecognized train data format (no colon): " + line);
            return;
        }

        String directionData = line.substring(colonIndex + 2);
        System.out.println("Direction data: " + directionData);
        String[] trainData = directionData.split(", (?=T\\d+\\()");

        for (String train : trainData) {
            System.out.println("Processing train entry: " + train);

            Pattern pattern = Pattern.compile("T(\\d+)\\((\\w{3}), ([FB])\\)");
            Matcher matcher = pattern.matcher(train);
            if (matcher.matches()) {
                int trainNumber = Integer.parseInt(matcher.group(1));
                String stationCode = matcher.group(2);
                String direction = matcher.group(3).equals("F") ? "forward" : "backward";

                System.out.println("Train number: " + trainNumber);
                System.out.println("Station code: " + stationCode);
                System.out.println("Direction: " + direction);

                if (trainNumber == userTrain) {
                    System.out.println("This is the User's Train");
                    // Fetch and display details for userTrain
                    System.out.println("User's Train details:");
                    System.out.println("Train number: " + trainNumber);
                    System.out.println("Station code: " + stationCode);
                    System.out.println("Direction: " + direction);

                    Station currentStation = trainTracker.getStation(stationCode);
                    if (currentStation != null) {
                        System.out.println("Current Station: " + currentStation.getName());
                        Station previousStation = trainTracker.getPreviousStation(currentStation);
                        Station nextStation = trainTracker.getNextStation(currentStation);

                        System.out.println(
                                "Previous Station: " + (previousStation != null ? previousStation.getName() : "N/A"));
                        System.out.println("Next Station: " + (nextStation != null ? nextStation.getName() : "N/A"));
                    } else {
                        System.out.println("No station found for code: " + stationCode);
                    }
                }

                Point coordinates = trainMap.getStationCoordinates(stationCode);
                if (coordinates != null) {
                    System.out.println("Parsed train data - Train number: " + trainNumber + ", coordinates: ("
                            + coordinates.x + ", " + coordinates.y + ")");
                    trainPositions.put(trainNumber, coordinates);

                    if (trainNumber == trackedTrainNumber) {
                        Station currentStation = trainTracker.getStation(stationCode);
                        SwingUtilities.invokeLater(() -> updateNextTrainPanel(currentStation));
                    }
                } else {
                    System.err.println("No coordinates found for station: " + stationCode);
                }
            } else {
                System.err.println("Unrecognized train data format (matcher): " + train);
            }
        }
    }

    private void updateNextTrainPanel(Station currentStation) {
        if (currentStation == null) {
            System.out.println("updateNextTrainPanel: currentStation is null");
            previousStationLabel.setText("Previous Station: N/A");
            currentStationLabel.setText("Current Station: N/A");
            nextStationsLabel.setText("<html>Next Stations:<br>N/A</html>");
            return;
        }

        System.out.println("updateNextTrainPanel: Current Station: " + currentStation.getName());

        Station previousStation = trainTracker.getPreviousStation(currentStation);
        Station nextStation = trainTracker.getNextStation(currentStation);

        System.out.println("updateNextTrainPanel: Previous Station: "
                + (previousStation != null ? previousStation.getName() : "N/A"));
        System.out.println(
                "updateNextTrainPanel: Next Station: " + (nextStation != null ? nextStation.getName() : "N/A"));

        List<Station> nextStations = new ArrayList<>();
        Station tempNextStation = nextStation;
        for (int i = 0; i < 3 && tempNextStation != null; i++) {
            nextStations.add(tempNextStation);
            tempNextStation = trainTracker.getNextStation(tempNextStation);
        }

        previousStationLabel
                .setText("Previous Station: " + (previousStation != null ? previousStation.getName() : "N/A"));
        currentStationLabel.setText("Current Station: " + currentStation.getName());
        nextStationsLabel.setText("<html>Next Stations:<br>" + nextStations.stream()
                .map(Station::getName)
                .reduce((s1, s2) -> s1 + "<br>" + s2)
                .orElse("N/A") + "</html>");

        // Force repaint
        SwingUtilities.invokeLater(() -> {
            previousStationLabel.repaint();
            currentStationLabel.repaint();
            nextStationsLabel.repaint();
        });

        System.out.println("updateNextTrainPanel: Panel updated");
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
            System.out.println("Drawing train number " + trainNumber + " on map at coordinates: (" + scaledX + ", "
                    + scaledY + ")");
        }

        g2d.dispose();
        adPanel.setAdImage(updatedMap);
        System.out.println("Map updated with new train positions.");
    }

    private void updateWeatherVisual() {
        if (weatherCondition != null && !weatherCondition.isEmpty()) {
            ImageIcon weatherVisual = WeatherFetch.getWeatherVisual(weatherCondition);

            if (weatherVisual != null) {
                int maxWidth = 230; // Adjust this value as needed
                int maxHeight = 230; // Adjust this value as needed

                Image originalImage = weatherVisual.getImage();

                double aspectRatio = (double) originalImage.getHeight(null) / originalImage.getWidth(null);

                int targetWidth = maxWidth;
                int targetHeight = (int) (maxWidth * aspectRatio);

                if (targetHeight > maxHeight) {
                    targetHeight = maxHeight;
                    targetWidth = (int) (maxHeight / aspectRatio);
                }

                Image scaledImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
                ImageIcon scaledWeatherVisual = new ImageIcon(scaledImage);

                weatherVisualLabel.setIcon(scaledWeatherVisual);
            } else {
                weatherVisualLabel.setIcon(null); // Clear the icon if no visual is found
                System.err.println("Weather visual not found for condition: " + weatherCondition);
            }
        }
    }

    private void fetchAndDisplayNews(String keyword) {
        new Thread(() -> {
            try {
                List<String> newsTitles = NewsFetcher.fetchNews(keyword);
                SwingUtilities.invokeLater(() -> {
                    NewsFetcher.startNewsScrolling(newsTextArea, newsTitles); // Start scrolling news with headlines
                });
            } catch (Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> newsTextArea.setText("Failed to load news."));
            }
        }).start();
    }

    private void updateNextTrainStations(Station currentStation) {
        if (currentStation == null) {
            previousStationLabel.setText("Previous Station: N/A");
            currentStationLabel.setText("Current Station: N/A");
            nextStationsLabel.setText("<html>Next Stations:<br>N/A</html>");
            return;
        }

        List<Station> lineStations = trainTracker.getStationsForLine(this.trainTracker.getTrackedLine());
        int currentIndex = lineStations.indexOf(currentStation);

        previousStationLabel.setText(
                "Previous Station: " + (currentIndex > 0 ? lineStations.get(currentIndex - 1).getName() : "N/A"));
        currentStationLabel.setText("Current Station: " + currentStation.getName());

        StringBuilder nextStations = new StringBuilder("<html>Next Stations:<br>");
        for (int i = 1; i <= 3; i++) {
            int nextIndex = currentIndex + i;
            nextStations.append("Next Station ").append(i).append(": ")
                    .append(nextIndex < lineStations.size() ? lineStations.get(nextIndex).getName() : "N/A")
                    .append("<br>");
        }
        nextStations.append("</html>");
        nextStationsLabel.setText(nextStations.toString());
    }

    private void updateMapWithNextTrainData() {
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

        // Draw only the tracked train's position
        int trackedTrainNumber = this.trainTracker.getTrackedTrainNumber();
        Point pos = trainPositions.get(trackedTrainNumber);

        if (pos != null) {
            // Scale the coordinates
            int scaledX = (int) (pos.x * scaleX);
            int scaledY = (int) (pos.y * scaleY);

            g2d.setColor(Color.RED);
            g2d.fillOval(scaledX - 5, scaledY - 5, 10, 10);
            System.out.println("Drawing train number " + trackedTrainNumber + " on map at coordinates: (" + scaledX
                    + ", " + scaledY + ")");
        }

        g2d.dispose();
        adPanel.setAdImage(updatedMap);
        System.out.println("Map updated with new train positions.");
    }

}