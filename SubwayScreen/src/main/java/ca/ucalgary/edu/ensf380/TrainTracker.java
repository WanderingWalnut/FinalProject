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

    public TrainTracker(String trackedLine, int trackedTrainNumber) {
        this.trackedLine = trackedLine;
        this.trackedTrainNumber = trackedTrainNumber;
        try {
            // Adjust the path to the Map.csv file
            readCSVFile("Map/Map.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
                if (values.length >= 8) { // Ensure there are at least 8 elements
                    String stationCode = values[3]; // Use StationCode
                    String stationName = values[4]; // Use StationName
                    int x = (int) Double.parseDouble(values[5]); // Use X coordinate
                    int y = (int) Double.parseDouble(values[6]); // Use Y coordinate
                    String lines = values[7]; // Use Common Stations

                    Station station = new Station(stationCode, stationName, x, y, lines);
                    stations.put(stationCode, station);
                }
            }
        }
    }

    public Station getStation(String code) {
        return stations.get(code);
    }

    public String getTrackedLine() {
        return trackedLine;
    }

    public int getTrackedTrainNumber() {
        return trackedTrainNumber;
    }

    public List<Station> getStationsForLine(String line) {
        List<Station> lineStations = new ArrayList<>();
        for (Station station : stations.values()) {
            if (station.getCommonLines().contains(line)) {
                lineStations.add(station);
            }
        }
        return lineStations;
    }

    public List<Station> getPathForTrain() {
        return getStationsForLine(trackedLine);
    }

    public Station getNextStation(Station currentStation) {
        List<Station> path = getPathForTrain();
        int currentIndex = path.indexOf(currentStation);
        if (currentIndex != -1 && currentIndex < path.size() - 1) {
            return path.get(currentIndex + 1);
        }
        return null;
    }

    public Station getPreviousStation(Station currentStation) {
        List<Station> path = getPathForTrain();
        int currentIndex = path.indexOf(currentStation);
        if (currentIndex > 0) {
            return path.get(currentIndex - 1);
        }
        return null;
    }
}
