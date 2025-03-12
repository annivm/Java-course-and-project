package viikko10.commands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import viikko10.EventManager;
import viikko10.datamodel.AnnualEvent;
import viikko10.datamodel.Category;
import viikko10.datamodel.Event;
import viikko10.datamodel.SingularEvent;
import viikko10.providers.CSVEventProvider;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Command(name = "addevent", description = "Adds a new event")
public class AddEvent implements Runnable {

    @Option(names = {"-d", "--date"}, description = "The date of the event (yyyy-MM-dd)", required = true)
    private String eventDate;

    @Option(names = {"-desc", "--description"}, description = "The description of the event", required = true)
    private String eventDescription;

    @Option(names = {"-c", "--category"}, description = "The category of the event", required = true)
    private String eventCategory;

    @Option(names = {"-p", "--provider"}, description = "The identifier of the event provider")
    private String eventProviderId = "standard"; // Default identifier if not provided

    @Override
    public void run() {
        EventManager manager = EventManager.getInstance();
        CSVEventProvider provider = (CSVEventProvider) manager.getEventProviderById(eventProviderId);

        if (provider == null) {
            System.err.printf("Event provider with identifier '%s' not found%n", eventProviderId);
            return;
        }

        try {
            Event event;
            Category category = Category.parse(eventCategory);
            // yyyy-MM-dd format
            if (eventDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                LocalDate date = LocalDate.parse(eventDate);
                event = new SingularEvent(date, eventDescription, category);
            // --MM-dd format
            } else if (eventDate.matches("--\\d{2}-\\d{2}")) {
                MonthDay date = MonthDay.parse(eventDate, DateTimeFormatter.ofPattern("--MM-dd"));
                event = new AnnualEvent(date, eventDescription, category);
            } else {
                throw new DateTimeParseException("Invalid date format", eventDate, 0);
            }
            provider.addEvent(event);
            System.out.printf("Event '%s' on '%s' added successfully%n", eventDescription, eventDate);
        } catch (DateTimeParseException dtpe) {
            System.err.println("Error in date format: " + dtpe.getLocalizedMessage());
        } catch (IllegalArgumentException iae) {
            System.err.println("Invalid category: " + iae.getLocalizedMessage());
        }
    }
}
