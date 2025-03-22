package viikko12.commands;

import java.time.MonthDay;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import viikko12.*;
import viikko12.datamodel.Event;
import viikko12.datamodel.AnnualEvent;
import viikko12.datamodel.SingularEvent;
import viikko12.datamodel.Category;
import viikko12.datamodel.AnnualEventComparator;
import viikko12.datamodel.SingularEventComparator;

import viikko12.filters.DateCategoryFilter;
import viikko12.filters.DateFilter;
import viikko12.filters.EventFilter;
import viikko12.providers.web.WebEventProvider;

@Command(name = "listevents")
public class ListEvents implements Runnable {
    @Option(names = "-c", description = "Category of events to list")
    String categoryOptionString;

    @Option(names = "-d", description = "Date of events to list in the format MM-dd (default is today)")
    String dateOptionString;

    @Override
    public void run() {
        Category category = null;

        if (this.categoryOptionString != null) {
            try {
                category = Category.parse(this.categoryOptionString);
            } catch (IllegalArgumentException iae) {
                System.err.println("Invalid category string: '" + this.categoryOptionString + "'");
                return;
            }
        }

        EventManager manager = EventManager.getInstance();

        // Now we either have a valid Category instance or it is null.

        MonthDay monthDay = null;
        if (this.dateOptionString != null) {
            try {
                monthDay = MonthDay.parse("--" + this.dateOptionString);

                WebEventProvider provider = (WebEventProvider) manager.getEventProvider("web");
                provider.setMonthDay(MonthDay.parse("--03-20"));

            } catch (DateTimeParseException dtpe) {
                System.err.println("Invalid date string: '" + this.dateOptionString + "'");
                return;
            }
        } else {
            //System.out.println("DEBUG: No date specified, default to now");
            monthDay = MonthDay.now();
        }

        EventFilter filter = null;
        // We always have a valid monthDay (defaults to 'now'),
        // so just check if we need category filtering.
        if (category != null) {
            filter = new DateCategoryFilter(monthDay, category);
        } else {
            filter = new DateFilter(monthDay);
        }
        // We actually seem to have no use for CategoryFilter.

        List<Event> filteredEvents = manager.getFilteredEvents(filter);

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
            System.out.println("Observed today:");
            Collections.sort(annualEvents, new AnnualEventComparator());

            for (AnnualEvent a : annualEvents) {
                System.out.printf(
                        "- %s (%s) %n",
                        a.getDescription(),
                        a.getCategory());
            }
        }

        if (!singularEvents.isEmpty()) {
            System.out.println("\nToday in history:");
            Collections.sort(singularEvents, new SingularEventComparator());
            Collections.reverse(singularEvents);

            for (SingularEvent s : singularEvents) {
                int year = s.getDate().getYear();

                System.out.printf(
                        "%d: %s (%s)%n",
                        year,
                        s.getDescription(),
                        s.getCategory());
            }
        }
    }
}
