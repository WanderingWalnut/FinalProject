package ca.ucalgary.edu.ensf380;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AdvertisementDisplayTest {
    private AdvertisementDisplay advertisementDisplay;
    
    @BeforeEach
    void setUp() throws Exception {
        advertisementDisplay = new AdvertisementDisplay(10); // example arguments
    }

    @AfterEach
    void tearDown() throws Exception {
        advertisementDisplay = null;
    }

    @Test
    void testConstructor() {
        assertNotNull(advertisementDisplay);
    }


    @Test
    void testProcessTrainData() {
        String trainData = "R: T1(R10, F), T2(R15, F), T3(R38, B), T4(R42, B)";
        advertisementDisplay.processTrainData(trainData);
        // Add assertions to verify the state of AdvertisementDisplay after processing train data
    }

}
