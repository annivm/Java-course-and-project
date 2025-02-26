package viikko8;

import java.util.List;
import java.util.Arrays;

public class Today {
    public static void main(String[] args) {

        // Gets the singleton manager. Later calls to getInstance
        // will return the same reference.
        EventManager manager = EventManager.getInstance();

        // Add a couple of event providers for testing purposes
        manager.addEventProvider(new FirstEventProvider("first"));
        manager.addEventProvider(new SecondEventProvider("second"));

        // Replace with a valid path to the events.csv file on your own computer!
        final String fileName = "events.csv";

        // Add csv event provider
        manager.addEventProvider(new CSVEventProvider(fileName));

        // Get all events
        List<Event> events = manager.getAllEvents();

        // Get count of eventproviders and events
        // and print them
        int providerCount = manager.getEventProviderCount();
        int eventCount = events.size();
        System.out.printf("Manager has %d event providers,%n", providerCount);
        System.out.printf("with a total of %d events.%n", eventCount);

        // Get the event provider identifiers and print them.
        List<String> identifiers = manager.getEventProviderIdentifiers();
        System.out.println("Event providers: "
            + Arrays.toString(identifiers.toArray()));

        // Remove the CSV event provider, then get the counts again.
        manager.removeEventProvider("CSV");

        providerCount = manager.getEventProviderCount();
        events = manager.getAllEvents();  // refresh event list
        eventCount = events.size();
        System.out.printf("Manager has %d event providers,%n", providerCount);
        System.out.printf("with a total of %d events.%n", eventCount);

        identifiers = manager.getEventProviderIdentifiers();
        System.out.println("Event providers: "
            + Arrays.toString(identifiers.toArray()));
    }
}