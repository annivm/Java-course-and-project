package viikko12;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.MonthDay;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import picocli.CommandLine;
import picocli.CommandLine.Command;

import viikko12.commands.ListProviders;
import viikko12.commands.ListEvents;
import viikko12.datamodel.*;
import viikko12.providers.CSVEventProvider;
import viikko12.providers.web.EventDeserializer;
import viikko12.providers.web.WebEventProvider;

@Command(name = "today", subcommands = { ListProviders.class, ListEvents.class }, description = "Shows events from history and annual observations")
public class Today {
    public Today() {
        // Gets the singleton manager. Later calls to getInstance
        // will return the same reference.
        EventManager manager = EventManager.getInstance();

        // Construct a path to a file in the user's home directory,
        // in a subdirectory called ".today".
        String homeDirectory = System.getProperty("user.home");
        String configDirectory = ".today";
        Path path = Paths.get(homeDirectory, configDirectory, "events.csv");
        //System.out.println("Path = " + path.toString());

        // Create the events file if it doesn't exist
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                System.err.println("Unable to create events file");
                System.exit(1);
            }
        }

        String eventProviderId = "standard";

        // Add a CSV event provider that reads from the given file.
        manager.addEventProvider(
            new CSVEventProvider(path.toString(), eventProviderId));

        /*
        // Try to add an event provider with the same ID again:
        if (!manager.addEventProvider(
                new CSVEventProvider(path.toString(), eventProviderId))) {
            System.err.printf("Event provider '%s' is already registered%n", eventProviderId);
        }
        */

        try{
        var serverAddress = "https://todayserver-89bb2a1b2e80.herokuapp.com/";
        var serverEventsPath = "api/v1/events";
        var serverUriString = String.format("%s%s", serverAddress, serverEventsPath);
        URI serverUri = new URI(serverUriString);

        WebEventProvider webProvider = new WebEventProvider(serverUri, "web");
        manager.addEventProvider(webProvider);

        // WebEventProvider provider = (WebEventProvider) manager.getEventProvider("web");
        // provider.setMonthDay(MonthDay.parse("--03-20"));

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
