package ca.ucalgary.edu.ensf380;


public class Station {
    private final String code;
    private final String name;
    private final int x;
    private final int y;
    private final String commonLines;

    public Station(String code, String name, int x, int y, String commonLines) {
        this.code = code;
        this.name = name;
        this.x = x;
        this.y = y;
        this.commonLines = commonLines;
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

    public String getCommonLines() {
        return commonLines;
    }

    public String getLineCode() {
        if (commonLines != null && !commonLines.isEmpty()) {
            return commonLines.split(",")[0].trim();
        }
        return null;
    }
}
