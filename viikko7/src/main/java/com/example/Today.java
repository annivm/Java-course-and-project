package com.example;

import com.example.Event;

import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Today {
    public static void main(String[] args) {
        // Replace with a valid path to the events.csv file on your own computer!
        final String fileName = "events.csv";
        EventProvider provider = new CSVEventProvider(fileName);

        final MonthDay monthDay = MonthDay.of(2, 10);

        // Get events for given day, any year, any category, newest first
        List<Event> events = provider.getEventsOfDate(monthDay);
        events.sort(Comparator.comparing(Event::getDate).reversed());

        for (Event event : events) {
            System.out.println(event);
        }
    }
}