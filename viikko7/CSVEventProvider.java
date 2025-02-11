
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Month;
import java.time.MonthDay;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

public class CSVEventProvider implements EventProvider {
    private List<Event> events;

    public CSVEventProvider(String fileName) {
        this.events = new ArrayList<>();

        // Read all lines from the CSV file
        // and create events with a helper method.
        // This is just a placeholder, and a
        // very bad way to do this;
        // there are just too many special cases in CSV.
        // Always use a library to handle the heavy lifting.
        // You have been warned! The next version will fix this.
        Path path = Paths.get(fileName);
        try {
            List<String> lines = Files.readAllLines(path);

            for (String line : lines) {
                this.events.add(makeEvent(line));
            }

            System.out.printf("Read %d events from CSV file%n", this.events.size());
        } catch (IOException ioe) {
            System.err.println("File '" + fileName + "' not found");
        }

        // Note that if there was an IOException, the list of events
        // will be empty. It is important to create the list before
        // any attempts to read from a file!
    }

    @Override
    public List<Event> getEvents() {
        return this.events;
    }

    @Override
    public List<Event> getEventsOfCategory(Category category) {
        List<Event> result = new ArrayList<Event>();
        for (Event event : this.events) {
            if (event.getCategory().equals(category)) {
                result.add(event);
            }
        }
        return result;
    }

    @Override
    public List<Event> getEventsOfDate(MonthDay monthDay) {
        List<Event> result = new ArrayList<Event>();

        for (Event event : this.events) {
            final Month eventMonth = event.getDate().getMonth();
            final int eventDay = event.getDate().getDayOfMonth();
            if (monthDay.getMonth() == eventMonth && monthDay.getDayOfMonth() == eventDay) {
                result.add(event);
            }
        }

        return result;
    }

    private Event makeEvent(String row) {
        String[] parts = row.split(",");
        LocalDate date = LocalDate.parse(parts[0]);
        String description = parts[1];
        String categoryString = parts[2];
        String[] categoryParts = categoryString.split("/");
        String primary = categoryParts[0];
        String secondary = null;
        if (categoryParts.length == 2) {
            secondary = categoryParts[1];
        }
        Category category = new Category(primary, secondary);
        return new Event(date, description, category);
    }
}
