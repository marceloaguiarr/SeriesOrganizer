import control.EpisodeController;
import model.Episode;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {

        String source = args[0];
        String destination = args[1];

        try (Stream<Path> stream = Files.list(Paths.get(source))) {
            List<String> joined = stream
                    .map(o -> o.getFileName().toString())
                    .filter(path -> !path.startsWith("."))
                    .sorted()
                    .collect(Collectors.toList());

            List<Episode> episodes = new ArrayList<>();
            List<String> fails = new ArrayList<>();

            joined.forEach(s -> {
                Episode e = EpisodeController.createEpisode(s);

                if (e != null)
                    episodes.add(e);
                else
                    fails.add(s);
            });

            System.out.println("Successfully moved");
            episodes.forEach(e -> EpisodeController.moveToDestination(e, source, destination));

            if (!fails.isEmpty()) {
                System.out.println("\nBadly formated files:");
                fails.forEach(System.out::println);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
