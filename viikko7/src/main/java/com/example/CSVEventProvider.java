package com.example;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Month;
import java.time.MonthDay;
import java.util.List;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.util.ArrayList;
import java.time.LocalDate;



public class CSVEventProvider implements EventProvider {
    private List<Event> events;

    public CSVEventProvider(String fileName) {
        this.events = new ArrayList<>();

        try {
            Path path;
            String ostype = System.getProperty("os.name").toLowerCase();
            if (ostype.contains("win")) {
                path = Paths.get((System.getenv("USERPROFILE")), ".today", fileName);
            } else if (ostype.contains("mac") || ostype.contains("nux")) {
                path = Paths.get((System.getenv("HOME")), ".today", fileName);
            } else {
                System.out.println("Failed to get OS type and homepath");
                return;
            }

            // luodaan tiedostonlukija
            FileReader reader = new FileReader(path.toString());

            // luodaan CSVReader ja luetaan tiedosto
            CSVReader csvReader = new CSVReader(reader);

            // luetaan tiedosto rivi kerrallaan
            String[] next;
            while ((next = csvReader.readNext()) != null) {
                String row = String.join(",", next);
                this.events.add(makeEvent(row));
            }
            //System.out.printf("Read %d events from CSV file%n", this.events.size());
            csvReader.close();

        } catch (InvalidPathException ipe) {
            System.err.println("Invalid path. " + ipe.getMessage());
            System.exit(1);
        } catch(FileNotFoundException fnfe) {
            System.err.println("File '" + fileName + "' not found. " + fnfe.getMessage());
            System.exit(1);
        } catch (IOException ioe) {
            System.err.println("Error reading the file " + ioe.getMessage());
            System.exit(1);
        } catch(CsvValidationException csve){
            System.err.println("Error reading CSV file." + csve.getMessage());
            System.exit(1);
        }
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