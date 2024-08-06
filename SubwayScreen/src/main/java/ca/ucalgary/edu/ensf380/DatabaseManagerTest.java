package ca.ucalgary.edu.ensf380;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

class DatabaseManagerTest {

    @BeforeEach
    void setUp() throws Exception {
        // Create a new database and table before each test
        DatabaseManager.createNewDatabase();
        DatabaseManager.createTable();
    }

    @AfterEach
    void tearDown() throws Exception {
        // Delete the database file after each test to ensure a clean slate
        File dbFile = new File("subway_ads.db");
        if (dbFile.exists()) {
            dbFile.delete();
        }
    }

    @Test
    void testCreateNewDatabase() {
        File dbFile = new File("subway_ads.db");
        assertTrue(dbFile.exists(), "Database file should exist after creation.");
    }

    @Test
    void testCreateTable() {
        // Verify table creation by inserting and retrieving data
        DatabaseManager.insertAd("Test Ad", "IMAGE", "path/to/image.jpg");
        List<Ad> ads = DatabaseManager.getAdvertisements();
        assertFalse(ads.isEmpty(), "Table should exist and allow insertion and retrieval of data.");
    }

    @Test
    void testInsertAd() {
        DatabaseManager.insertAd("Test Ad", "IMAGE", "path/to/image.jpg");
        List<Ad> ads = DatabaseManager.getAdvertisements();
        assertFalse(ads.isEmpty(), "There should be at least one ad in the database after insertion.");
        Ad ad = ads.get(0);
        assertEquals("Test Ad", ad.getContent(), "The content of the ad should match the inserted value.");
        assertEquals("IMAGE", ad.getMediaType(), "The media type of the ad should match the inserted value.");
        assertEquals("path/to/image.jpg", ad.getMediaPath(), "The media path of the ad should match the inserted value.");
    }

    @Test
    void testGetAdvertisements() {
        DatabaseManager.insertAd("Test Ad 1", "IMAGE", "path/to/image1.jpg");
        DatabaseManager.insertAd("Test Ad 2", "VIDEO", "path/to/video1.mp4");

        List<Ad> ads = DatabaseManager.getAdvertisements();
        assertEquals(2, ads.size(), "There should be two ads in the database.");

        Ad ad1 = ads.get(0);
        assertEquals("Test Ad 1", ad1.getContent(), "The content of the first ad should match the inserted value.");
        assertEquals("IMAGE", ad1.getMediaType(), "The media type of the first ad should match the inserted value.");
        assertEquals("path/to/image1.jpg", ad1.getMediaPath(), "The media path of the first ad should match the inserted value.");

        Ad ad2 = ads.get(1);
        assertEquals("Test Ad 2", ad2.getContent(), "The content of the second ad should match the inserted value.");
        assertEquals("VIDEO", ad2.getMediaType(), "The media type of the second ad should match the inserted value.");
        assertEquals("path/to/video1.mp4", ad2.getMediaPath(), "The media path of the second ad should match the inserted value.");
    }
}
