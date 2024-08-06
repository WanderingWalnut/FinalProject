package ca.ucalgary.edu.ensf380;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StationTest {
    private Station station;

    @BeforeEach
    void setUp() throws Exception {
        // Initialize the Station object before each test
        station = new Station("ST01", "Station Name", 100, 200, "LineA,LineB,LineC");
    }

    @AfterEach
    void tearDown() throws Exception {
        // Clean up after each test if necessary
    }

    @Test
    void testStation() {
        // Test the constructor and ensure the object is initialized correctly
        assertNotNull(station, "Station object should be initialized");
    }

    @Test
    void testGetCode() {
        assertEquals("ST01", station.getCode(), "Station code should match the initialized value");
    }

    @Test
    void testGetName() {
        assertEquals("Station Name", station.getName(), "Station name should match the initialized value");
    }

    @Test
    void testGetX() {
        assertEquals(100, station.getX(), "Station x coordinate should match the initialized value");
    }

    @Test
    void testGetY() {
        assertEquals(200, station.getY(), "Station y coordinate should match the initialized value");
    }

    @Test
    void testGetCommonStations() {
        assertEquals("LineA,LineB,LineC", station.getCommonStations(), "Common stations should match the initialized value");
    }

    @Test
    void testGetLineCode() {
        assertEquals("LineA", station.getLineCode(), "First line code should be extracted correctly from common stations");
    }

    @Test
    void testGetLineCodeWithNullCommonStations() {
        // Test with null commonStations
        Station stationWithNullCommonStations = new Station("ST02", "Station Name 2", 300, 400, null);
        assertNull(stationWithNullCommonStations.getLineCode(), "Line code should be null if common stations is null");
    }

    @Test
    void testGetLineCodeWithEmptyCommonStations() {
        // Test with empty commonStations
        Station stationWithEmptyCommonStations = new Station("ST03", "Station Name 3", 500, 600, "");
        assertNull(stationWithEmptyCommonStations.getLineCode(), "Line code should be null if common stations is empty");
    }
}
