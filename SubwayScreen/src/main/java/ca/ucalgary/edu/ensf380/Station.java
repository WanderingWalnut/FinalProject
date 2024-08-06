package ca.ucalgary.edu.ensf380;

public class Station {
    private final String code;
    private final String name;
    private final int x;
    private final int y;
    private final String commonStations;

    public Station(String code, String name, int x, int y, String commonStations) {
        this.code = code;
        this.name = name;
        this.x = x;
        this.y = y;
        this.commonStations = commonStations != null ? commonStations : ""; // Initialize to empty string if null
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getCommonStations() {
        return commonStations;
    }

    public String getLineCode() {
        if (commonStations != null && !commonStations.isEmpty()) {
            return commonStations.split(",")[0].trim();
        }
        return null;
    }
}
