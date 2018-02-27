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
import org.apache.commons.cli.*;


public class Main {

    public static void main(String[] args) {

        Options options = new Options();
		
		Option input = new Option("s", "source", true, "source folder");
        input.setRequired(true);
        options.addOption(input);

        Option output = new Option("d", "destination", true, "destination folder");
        output.setRequired(true);
        options.addOption(output);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;
        
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
            return;
        }
        
        String source = cmd.getOptionValue("source");
        String destination = cmd.getOptionValue("destination");

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
