package ca.ucalgary.edu.ensf380;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

class TrainMapTest {
    private TrainMap trainMap;

    @BeforeEach
    void setUp() throws Exception {
        // Create a temporary CSV file for testing
        try (PrintWriter writer = new PrintWriter(new FileWriter("Map/Map.csv"))) {
            writer.println("StationName,LineName,CityName,StationCode,StationType,X,Y");
            writer.println("Station1,Line1,City1,ST01,Type1,100.0,200.0");
            writer.println("Station2,Line2,City2,ST02,Type2,150.0,250.0");
        }

        // Initialize the TrainMap object
        trainMap = new TrainMap();
    }

    @AfterEach
    void tearDown() throws Exception {
        // Delete the temporary CSV file after each test
        java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get("Map/Map.csv"));
    }

    @Test
    void testTrainMap() {
        // Test the constructor and ensure the object is initialized correctly
        assertNotNull(trainMap, "TrainMap object should be initialized");
    }

    @Test
    void testGetStationCoordinates() {
        // Test the getStationCoordinates method
        Point coordinates = trainMap.getStationCoordinates("ST01");
        assertNotNull(coordinates, "Coordinates for ST01 should not be null");
        assertEquals(new Point(100, 200), coordinates, "Coordinates for ST01 should match the expected values");

        coordinates = trainMap.getStationCoordinates("ST02");
        assertNotNull(coordinates, "Coordinates for ST02 should not be null");
        assertEquals(new Point(150, 250), coordinates, "Coordinates for ST02 should match the expected values");

        coordinates = trainMap.getStationCoordinates("ST03");
        assertNull(coordinates, "Coordinates for ST03 should be null if the station code does not exist");
    }
}
