package model;

public class Episode {

    private String series;
    private String season;
    private String number;
    private String extension;
    private String originalFileName;

    public Episode(String series, String season, String number, String extension, String originalFileName) {
        this.series = series;
        this.season = season;
        this.number = number;
        this.extension = extension;
        this.originalFileName = originalFileName;
    }

    public String getSeries() {
        return series;
    }

    public String getSeason() {
        return season;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    @Override
    public String toString() {
        return series + " - S" + season + "E" + number + "." + extension;
    }
}
