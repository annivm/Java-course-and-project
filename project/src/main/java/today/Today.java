package today;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeParseException;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import today.commands.AddEvent;
import today.commands.ListEvents;
import today.commands.ListProviders;
import today.providers.CSVEventProvider;
import today.providers.EventProvider;
import today.providers.SQLiteEventProvider;
import today.providers.web.WebEventProvider;

@Command(name = "today", subcommands = { ListProviders.class, ListEvents.class, AddEvent.class }, description = "Shows events from history and annual observations")
public class Today {
    public Today() {
        // Gets the singleton manager. Later calls to getInstance
        // will return the same reference.
        EventManager manager = EventManager.getInstance();

        // Construct a path to a file in the user's home directory,
        // in a subdirectory called ".today".
        String homeDirectory = System.getProperty("user.home");
        String configDirectory = ".today";

        Path csvPath = Paths.get(homeDirectory, configDirectory, "events.csv");
        //System.out.println("CSV-file path = " + csvPath.toString());

        // Create the events file if it doesn't exist
        if (!Files.exists(csvPath)) {
            try {
                Files.createFile(csvPath);
            } catch (IOException e) {
                System.err.println("Unable to create events file");
                System.exit(1);
            }
        }

        String eventProviderId = "csv";

        // Add a CSV event provider that reads from the given file.
        manager.addEventProvider(
                new CSVEventProvider(csvPath.toString(), eventProviderId));

        // Add an SQLite database event provider.
        Path databasePath = Paths.get(homeDirectory, configDirectory, "events.sqlite3");
        EventProvider sqliteEventProvider = new SQLiteEventProvider(databasePath);
        manager.addEventProvider(sqliteEventProvider);

        // Add a web event provider
        try{
            var serverAddress = "https://todayserver-89bb2a1b2e80.herokuapp.com/";
            var serverEventsPath = "api/v1/events";
            var serverUriString = String.format("%s%s", serverAddress, serverEventsPath);
            URI serverUri = new URI(serverUriString);

            WebEventProvider webProvider = new WebEventProvider(serverUri, "web");
            manager.addEventProvider(webProvider);

            // System.out.println("Api-path: " + serverUriString);
        } catch (URISyntaxException use) {
            System.err.println("Error making URI: " + use.getLocalizedMessage());
        } catch (DateTimeParseException dtpe) {
            System.err.println("Error in date format: " + dtpe.getLocalizedMessage());
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Today()).execute(args);
        System.exit(exitCode);
    }
}
