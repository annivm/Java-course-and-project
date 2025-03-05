package viikko9;

import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Today {
    public static void main(String[] args) {
        // Gets the singleton manager. Later calls to getInstance
        // will return the same reference.
        EventManager manager = EventManager.getInstance();

        // Add a CSV event provider that reads from the given file.
        // Replace with a valid path to the events.csv file on your own computer!
        String fileName = "/Users/anniv/.today/events.csv";
        manager.addEventProvider(new CSVEventProvider(fileName));

        fileName = "/Users/anniv/.today/singular-events.csv";
        manager.addEventProvider(new CSVEventProvider(fileName));

        /*
        MonthDay today = MonthDay.now();
        List<Event> allEvents = manager.getEventsOfDate(today);
        List<AnnualEvent> annualEvents = new ArrayList<>();
        List<SingularEvent> singularEvents = new ArrayList<>();
        for (Event event : allEvents) {
            if (event instanceof AnnualEvent) {
                annualEvents.add((AnnualEvent) event);
            } else if (event instanceof SingularEvent) {
                singularEvents.add((SingularEvent) event);
            }
        }

        System.out.println("Today:");
        Collections.sort(annualEvents, new AnnualEventComparator());

        for (AnnualEvent a : annualEvents) {
            System.out.printf(
                    "- %s (%s) %n",
                    a.getDescription(),
                    a.getCategory());
        }
        //System.out.printf("%d events%n", annualEvents.size());



        System.out.println("\nToday in history:");
        Collections.sort(singularEvents, new SingularEventComparator());
        Collections.reverse(singularEvents);

        for (SingularEvent s : singularEvents) {
            int year = s.getDate().getYear();
            if (year < 2015) {
                continue;
            }

            System.out.printf(
                    "%d: %s (%s)%n",
                    year,
                    s.getMonthDay(),
                    s.getDescription(),
                    s.getCategory());
        }
        //System.out.printf("%d events%n", singularEvents.size());
        */

        System.out.println("\nFiltered events with DateFilter:");
        MonthDay testDay = MonthDay.parse("--03-03");
        int year = 2023;
        DateFilter filter23 = new DateFilter(testDay);
        // System.out.println(filter23.getMonthDay());
        // System.out.println(filter23.getYear());
        List<Event> dateFilteredEvents = manager.getFilteredEvents(filter23);
        for (Event event : dateFilteredEvents){

            System.out.printf(
                    "%s %s%n",
                    event.getMonthDay(),
                    event.getDescription(),
                    event.getCategory());
        }


        System.out.println("\nFiltered events with CategoryFilter:");
        Category test = new Category("pvm");
        CategoryFilter filterTest = new CategoryFilter(test);
        List<Event> categoryFilteredEvents = manager.getFilteredEvents(filterTest);
        for (Event event : categoryFilteredEvents){

            System.out.printf(
                    "%s %s%n",
                    event.getMonthDay(),
                    event.getCategory());
        }


        System.out.println("\nFiltered events with DateCategoryFilter:");
        testDay = MonthDay.parse("--03-03");
        DateCategoryFilter filter = new DateCategoryFilter(testDay, test);
        List<Event> dateCategoryFilteredEvents = manager.getFilteredEvents(filter);
        for (Event event : dateCategoryFilteredEvents){

            System.out.printf(
                    "%s %s %s%n",
                    event.getMonthDay(),
                    event.getCategory(),
                    event.getDescription());
        }
    }
}
