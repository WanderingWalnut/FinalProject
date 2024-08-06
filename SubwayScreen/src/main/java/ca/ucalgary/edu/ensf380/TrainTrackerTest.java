package ca.ucalgary.edu.ensf380;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

class TrainTrackerTest {
    private TrainTracker trainTracker;

    @BeforeEach
    void setUp() throws Exception {
        // Create a temporary CSV file for testing
        try (PrintWriter writer = new PrintWriter(new FileWriter("Map/Map.csv"))) {
            writer.println("StationName,LineName,CityName,StationCode,StationType,X,Y,CommonStations");
            writer.println("Station1,Line1,City1,ST01,Type1,100.0,200.0,LineA");
            writer.println("Station2,Line1,City2,ST02,Type2,150.0,250.0,LineA");
            writer.println("Station3,Line2,City3,ST03,Type3,200.0,300.0,LineB");
            writer.println("Station4,Line2,City4,ST04,Type4,250.0,350.0,LineB");
        }

        // Initialize the TrainTracker object with userTrain value
        trainTracker = new TrainTracker(1);
    }

    @AfterEach
    void tearDown() throws Exception {
        // Delete the temporary CSV file after each test
        java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get("Map/Map.csv"));
    }

    @Test
    void testTrainTracker() {
        assertNotNull(trainTracker, "TrainTracker object should be initialized");
    }

    @Test
    void testGetStation() {
        Station station = trainTracker.getStation("ST01");
        assertNotNull(station, "Station should not be null for valid station code");
        assertEquals("ST01", station.getCode(), "Station code should match the expected value");
        assertEquals("Station1", station.getName(), "Station name should match the expected value");
        assertEquals(new Point(100, 200), new Point(station.getX(), station.getY()), "Station coordinates should match the expected values");
    }

    @Test
    void testGetTrackedLine() {
        assertNull(trainTracker.getTrackedLine(), "Tracked line should be null initially");
    }

    @Test
    void testGetTrackedTrainNumber() {
        assertEquals(0, trainTracker.getTrackedTrainNumber(), "Tracked train number should be 0 initially");
    }

    @Test
    void testGetUserTrain() {
        assertEquals(1, trainTracker.getUserTrain(), "User train should match the initialized value");
    }

    @Test
    void testGetStationsForLine() {
        List<Station> lineAStations = trainTracker.getStationsForLine("LineA");
        assertEquals(2, lineAStations.size(), "LineA should have 2 stations");
        assertEquals("ST01", lineAStations.get(0).getCode(), "First station of LineA should be ST01");
        assertEquals("ST02", lineAStations.get(1).getCode(), "Second station of LineA should be ST02");
    }



    @Test
    void testGetPreviousStation() {
        Station currentStation = trainTracker.getStation("ST02");
        Station previousStation = trainTracker.getPreviousStation(currentStation);
        assertNotNull(previousStation, "Previous station should not be null");
        assertEquals("ST01", previousStation.getCode(), "Previous station should be ST01 for current station ST02");
    }

    @Test
    void testGetNextStation() {
        Station currentStation = trainTracker.getStation("ST01");
        Station nextStation = trainTracker.getNextStation(currentStation);
        assertNotNull(nextStation, "Next station should not be null");
        assertEquals("ST02", nextStation.getCode(), "Next station should be ST02 for current station ST01");
    }
}
