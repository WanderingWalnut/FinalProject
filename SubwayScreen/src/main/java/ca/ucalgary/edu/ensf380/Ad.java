package ca.ucalgary.edu.ensf380;

public class Ad {
    private String content;
    private String mediaType;
    private String mediaPath;

    public Ad(String content, String mediaType, String mediaPath) {
        this.content = content;
        this.mediaType = mediaType;
        this.mediaPath = mediaPath;
    }

    // Getters and Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    @Override
    public String toString() {
        return "Ad{" +
                "content='" + content + '\'' +
                ", mediaType='" + mediaType + '\'' +
                ", mediaPath='" + mediaPath + '\'' +
                '}';
    }
}
