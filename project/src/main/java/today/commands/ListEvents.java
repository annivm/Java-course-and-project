package today.commands;

import java.time.MonthDay;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import today.*;
import today.datamodel.Event;
import today.datamodel.AnnualEvent;
import today.datamodel.SingularEvent;
import today.datamodel.Category;
import today.datamodel.AnnualEventComparator;
import today.datamodel.SingularEventComparator;

import today.filters.DateCategoryFilter;
import today.filters.DateFilter;
import today.filters.EventFilter;
import today.providers.web.WebEventProvider;

@Command(name = "listevents", description = "Shows a list of events")
public class ListEvents implements Runnable {
    @Option(names = {"-c", "--category"}, description = "Category or categories of events to list, separated by commas")
    String categoryOptionString;

    @Option(names = {"-d", "--date"}, description = "Date of events to list in the format MM-dd (default is today)")
    String dateOptionString;

    @Override
    public void run() {
        // Handle given categories
        List<Category> categories = new ArrayList<>();

        if (this.categoryOptionString != null) {
            String[] categoryStrings = this.categoryOptionString.split(",");
            for (String catStr : categoryStrings) {
                try {
                    categories.add(Category.parse(catStr.trim()));
                } catch (IllegalArgumentException iae) {
                    System.err.println("Invalid category string: '" + catStr + "'. Please use format MM-dd");
                    return;
                }
            }
        }

        // Parse given date
        MonthDay monthDay = null;
        if (this.dateOptionString != null) {
            try {
                monthDay = MonthDay.parse("--" + this.dateOptionString);
            } catch (DateTimeParseException dtpe) {
                System.err.println("Invalid date string: '" + this.dateOptionString + "'");
                return;
            }

        } else {
            monthDay = MonthDay.now();
        }
        System.out.printf("\nEvents for %s:%n%n", monthDay);

        EventManager manager = EventManager.getInstance();

        // Pass the date for WebEventProvider
        WebEventProvider webEventProvider = (WebEventProvider) manager.getEventProviderByID("web");
        if (webEventProvider != null) {
            webEventProvider.setMonthDay(monthDay);
        }

        // Get all events from all providers
        Set<Event> filteredEvents = new HashSet<>();

        if (!categories.isEmpty()) {
            for (Category cat : categories) {
                EventFilter filter = new DateCategoryFilter(monthDay, cat);
                filteredEvents.addAll(manager.getFilteredEvents(filter));
            }
        } else {
            EventFilter filter = new DateFilter(monthDay);
            filteredEvents.addAll(manager.getFilteredEvents(filter));
        }

        // Print the events
        List<AnnualEvent> annualEvents = new ArrayList<>();
        List<SingularEvent> singularEvents = new ArrayList<>();

        for (Event event : filteredEvents) {
            if (event instanceof AnnualEvent) {
                annualEvents.add((AnnualEvent) event);
            } else if (event instanceof SingularEvent) {
                singularEvents.add((SingularEvent) event);
            }
        }

        if (!annualEvents.isEmpty()) {
            System.out.println("Annual events:");
            Collections.sort(annualEvents, new AnnualEventComparator());

            for (AnnualEvent a : annualEvents) {
                final Category cat = a.getCategory();
                System.out.printf(
                        "- %s (%s)%n",
                        a.getDescription(),
                        cat.toString());
            }
        } else {
            System.out.println("No annual events found");
        }

        if (!singularEvents.isEmpty()) {
            System.out.println("\nSingular events:");
            Collections.sort(singularEvents, new SingularEventComparator());
            Collections.reverse(singularEvents);

            for (SingularEvent s : singularEvents) {
                int year = s.getDate().getYear();
                final Category cat = s.getCategory();
                System.out.printf(
                        "%d: %s (%s)%n",
                        year,
                        s.getDescription(),
                        cat.toString());
            }
        } else {
            System.out.println("\nNo singular events found");
        }
    }
}
