package ca.ucalgary.edu.ensf380;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:subway_ads.db";

    // Method to create a new SQLite database
    /**
     * @throws SQLException if a database access error occurs or the URL is null
     */
    public static void createNewDatabase() {
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                System.out.println("A new database has been created.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Method to create a new table in the database
    /**
     * @throws SQLException if database access/connect error occurs
     */
    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS advertisements (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " content TEXT,\n"
                + " media_type TEXT NOT NULL,\n"
                + " media_path TEXT\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(URL);
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table created.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 
     * @param content   content the textual content of the advertisement
     * @param mediaType the type of media
     * @param mediaPath the path of the media file
     * @throws SQLException if database access/connect error occurs
     */
    // Method to insert a new advertisement into the database
    public static void insertAd(String content, String mediaType, String mediaPath) {
        String sql = "INSERT INTO advertisements(content, media_type, media_path) VALUES(?,?,?)";

        try (Connection conn = DriverManager.getConnection(URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, content);
            pstmt.setString(2, mediaType);
            pstmt.setString(3, mediaPath);
            pstmt.executeUpdate();
            System.out.println("Advertisement inserted.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 
     * @return a list of {@code Ad} objects containing the content, media type, and
     *         media path of each advertisement
     * @throws SQLException if a database access error occurs or the URL is null
     */
    // Method to retrieve advertisements from the database
    public static List<Ad> getAdvertisements() {
        String sql = "SELECT content, media_type, media_path FROM advertisements";
        List<Ad> ads = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String content = rs.getString("content");
                String mediaType = rs.getString("media_type");
                String mediaPath = rs.getString("media_path");
                ads.add(new Ad(content, mediaType, mediaPath));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return ads;
    }

    /**
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        createNewDatabase();
        createTable();

        // Insert sample ads into the database
        insertAd(null, "IMAGE", "data/ads/ad1.jpeg");
        insertAd(null, "IMAGE", "data/ads/ad2.jpeg");
        insertAd(null, "IMAGE", "data/ads/ad3.jpeg");
        insertAd(null, "IMAGE", "data/ads/ad4.jpeg");
        insertAd(null, "IMAGE", "data/ads/ad5.jpeg");

        // Print out advertisements to verify insertion
        List<Ad> ads = getAdvertisements();
        for (Ad ad : ads) {
            System.out.println(ad);
        }
    }
}
