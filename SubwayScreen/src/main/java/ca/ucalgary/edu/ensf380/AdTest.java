package ca.ucalgary.edu.ensf380;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AdTest {
    private Ad ad;

    @BeforeEach
    public void setUp() {
        ad = new Ad("Test Content", "image", "path/to/image");
    }

    @Test
    public void testGetContent() {
        assertEquals("Test Content", ad.getContent());
    }

    @Test
    public void testSetContent() {
        ad.setContent("New Content");
        assertEquals("New Content", ad.getContent());
    }

    @Test
    public void testGetMediaType() {
        assertEquals("image", ad.getMediaType());
    }

    @Test
    public void testSetMediaType() {
        ad.setMediaType("video");
        assertEquals("video", ad.getMediaType());
    }

    @Test
    public void testGetMediaPath() {
        assertEquals("path/to/image", ad.getMediaPath());
    }

    @Test
    public void testSetMediaPath() {
        ad.setMediaPath("path/to/image");
        assertEquals("path/to/image", ad.getMediaPath());
    }

    @Test
    public void testToString() {
        String expected = "Ad{content='Test Content', mediaType='image', mediaPath='path/to/image'}";
        assertEquals(expected, ad.toString());
    }
}
