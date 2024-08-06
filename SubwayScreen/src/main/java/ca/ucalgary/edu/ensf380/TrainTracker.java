package ca.ucalgary.edu.ensf380;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainTracker {
    private Map<String, Station> stations = new HashMap<>();
    private String trackedLine;
    private int trackedTrainNumber;
    private int userTrain;

    /**
 * @param trackedLine is passed as a String type and trackedTrainNumber is passed as an int type 
 */
    public TrainTracker(int userTrain) {
        this.userTrain = userTrain;
        try {
            // Adjust the path to the Map.csv file
            readCSVFile("Map/Map.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 
 * @param filePath is passed as a String type
 * @throws IOException if an error is found   
 */
    private void readCSVFile(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true; // To skip the header
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the header line
                }
                String[] values = line.split(",");
                if (values.length >= 7) { // Ensure there are at least 7 elements
                    String stationCode = values[3]; // Use StationCode
                    String stationName = values[0]; // Use StationName
                    int x = (int) Double.parseDouble(values[5]); // Use X coordinate
                    int y = (int) Double.parseDouble(values[6]); // Use Y coordinate
                    String commonStations = values.length >= 8 ? values[7] : ""; // Use Common Stations if available

                    // Debug statement to check the parsed data
                    System.out.println("Parsed Station - Code: " + stationCode + ", Name: " + stationName
                            + ", X: " + x + ", Y: " + y + ", Common Stations: " + commonStations);

                    Station station = new Station(stationCode, stationName, x, y, commonStations);
                    stations.put(stationCode, station);

                    // Conditional message for stations with common stations
                    if (!commonStations.isEmpty()) {
                        System.out.println("You can change lines here at " + stationName + " (" + stationCode
                                + "). Common Stations: " + commonStations);
                    }
                }
                if (values.length < 6) {
                    // Debug statement to show if a line is skipped due to incomplete data
                    System.err.println("Incomplete data for line: " + line);
                }
            }
        }
    }

    /** 
 * @param code
 * @return stations.get(code) is returned with as a Station type
 */

    public Station getStation(String code) {
        return stations.get(code);
    }

    /** 
 * 
 * @return trackedLine is returned with as a String type 
 */
    public String getTrackedLine() {
        return trackedLine;
    }
    /** 
 * 
 * @return trackedTrainNumber is returned as an int type
 */
    public int getTrackedTrainNumber() {
        return trackedTrainNumber;
    }
/** 
 * @param line which is of String type is passed 
 * @return lineStations is returned as a  List<Station> type
 * 
 */
    public int getUserTrain() {
        return userTrain;
    }
/** 
 * @param code
 * @return stations.get(code) is returned with the type List<Station>
 */
    public List<Station> getStationsForLine(String line) {
        List<Station> lineStations = new ArrayList<>();
        for (Station station : stations.values()) {
            String commonStations = station.getCommonStations();
            if (commonStations == null) {
                commonStations = ""; // Ensure commonStations is not null
            }
            if (commonStations.contains(line) || line.equals(station.getLineCode())) {
                lineStations.add(station);
            }
        }
        return lineStations;
    }
/** 
 * @param currentStation is passed as a Station type
 * @return returns null OR returns path.get(currentIndex + 1) as a Station type if logical condition is met
 */
    public List<Station> getPathForTrain() {
        return getStationsForLine(trackedLine);
    }
/** 
 * @param currentStation is passed as a Station Type
 * @return returns null OR path.get(currentIndex - 1) as a Station type depending on if logical condition is met
 */
    public Station getPreviousStation(Station currentStation) {
        List<Station> lineStations = getStationsForLine(currentStation.getLineCode());
        System.out.println("Line stations: " + lineStations);
        int currentIndex = lineStations.indexOf(currentStation);
        System.out.println("Current index: " + currentIndex);
        if (currentIndex > 0) {
            return lineStations.get(currentIndex - 1);
        }
        return null;
    }

    public Station getNextStation(Station currentStation) {
        List<Station> lineStations = getStationsForLine(currentStation.getLineCode());
        System.out.println("Line stations: " + lineStations);
        int currentIndex = lineStations.indexOf(currentStation);
        System.out.println("Current index: " + currentIndex);
        if (currentIndex < lineStations.size() - 1) {
            return lineStations.get(currentIndex + 1);
        }
        return null;
    }
}
