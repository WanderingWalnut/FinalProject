package ca.ucalgary.edu.ensf380;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TrainMap {
    private Map<String, Point> stationCoordinates;

    public TrainMap() {
        stationCoordinates = new HashMap<>();
        loadStationCoordinates();
    }

    private void loadStationCoordinates() {
        String csvFile = "Map/Map.csv";
        String line;
        String csvSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine(); // Skip the header line
            while ((line = br.readLine()) != null) {
                String[] station = line.split(csvSplitBy);
                String stationCode = station[3].trim(); // StationCode
                int x = (int) Double.parseDouble(station[5].trim());
                int y = (int) Double.parseDouble(station[6].trim());
                stationCoordinates.put(stationCode, new Point(x, y));
                System.out.println("Loaded station: " + stationCode + " with coordinates: (" + x + ", " + y + ")");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Point getStationCoordinates(String stationCode) {
        System.out.println("Looking up coordinates for station code: " + stationCode);
        return stationCoordinates.get(stationCode);
    }
}
