package control;

import model.Episode;
import org.apache.commons.io.FileUtils;
import org.apache.commons.text.WordUtils;

import java.io.IOException;

public class EpisodeController {

    public static Episode createEpisode(String s) {

        String sceneFormat = "(.+)([sS]\\d{2}[eE]\\d{2})(.+)(\\w{3})";
        String plexFormat = "(.+) - S(\\d{2})E(\\d{2})\\.(.+)";

        if (s.matches(plexFormat)) {
            String tokenize = s.replaceAll(plexFormat, "$1|$2|$3|$4");
            String[] name = tokenize.split("\\|");

            return new Episode(name[0], name[1], name[2], name[3], s);

        } else if (s.matches(sceneFormat)) {
            String tokenize = s.replaceAll(sceneFormat, "$1|$2|$3|$4");
            String[] name = tokenize.split("\\|");

            name[0] = name[0].replaceAll("\\.", " ").trim();
            name[0] = WordUtils.capitalize(name[0]).replaceAll("Of", "of");

            name[1] = name[1].replaceAll("[sS](\\d{2})[eE](\\d{2})", "S$1E$2");

            if (name[3].equals("srt"))
                name[3] = "pt.srt";

            String season = name[1].replaceAll("S(\\d{2}).+", "$1");
            String number = name[1].replaceAll(".+E(\\d{2})", "$1");

            return new Episode(name[0], season, number, name[3], s);

        } else {
            return null;
        }


    }

    public static void moveToDestination(Episode episode, String source, String destination) {
        String episodeName = episode.getSeries() + "/" + "Season " + episode.getSeason() + "/" + episode.toString();

        try {
            FileUtils.moveFile(
                    FileUtils.getFile(source + episode.getOriginalFileName()),
                    FileUtils.getFile(destination + episodeName)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(episode.getOriginalFileName() + " -> " + destination + episodeName);
    }

}
