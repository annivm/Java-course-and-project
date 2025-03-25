package viikko13.providers;

import viikko13.EventFactory;
import viikko13.datamodel.AnnualEvent;
import viikko13.datamodel.Category;
import viikko13.datamodel.Event;
import viikko13.datamodel.SingularEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.MonthDay;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Provides events stored in an SQLite database.
 * Uses the sqlite-jdbc driver (see pom.xml).
 */
public class SQLiteEventProvider implements EventProvider {
    private String url;
    private Map<Integer, String> categories;

    /**
     * Constructs a new SQLite event provider with a database filename.
     *
     * @param fileName the name of the SQLite database file
     */
    public SQLiteEventProvider(String fileName) {
        this.url = "jdbc:sqlite:" + fileName;
        // TODO: normalize path separators to '/'
        System.out.println("Database URL string = " + this.url);

        // Get the categories found in the database and cache them.
        // This way we don't need to keep fetching them over again.
        // We pass an empty list of IDs so that we get them all.
        this.categories = this.getCategories(List.of());
        System.out.printf("Got %d categories from database%n", this.categories.keySet().size());
    }

    /*
       Gets the categories from the database as a map
       where keys are numeric category IDs and values are
       simple category strings. If the list of category IDs
       is non-empty, gets the specific categories, otherwise all of them.
     */
    private Map<Integer, String> getCategories(List<Integer> categoryIds) {
        Map<Integer, String> result = new HashMap<>();

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select category_id, primary_name, secondary_name from category");
        if (!categoryIds.isEmpty()) {
            // Construct a comma-separated list of category IDs
            // that can be used in the SQL WHERE clause, then
            // append it to the query builder.
            String idList = categoryIds.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
            queryBuilder.append(String.format(" where category_id in (%s)", idList));
        }
        var query = queryBuilder.toString();
        //System.out.println("Category query = " + query);

        // Use the try-with-resources statement to get a connection,
        // a statement, and a result set, so that they will be closed
        // automatically.
        try (var connection = DriverManager.getConnection(url);
             var statement = connection.createStatement();
             var rs = statement.executeQuery(query)) {
            //System.out.println("Connected to SQLite database.");
            while (rs.next()) {
                int categoryId = rs.getInt("category_id");
                String primaryName = rs.getString("primary_name");
                String secondaryName = rs.getString("secondary_name");
                result.put(categoryId, primaryName + "/" + secondaryName);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return result;
    }

    /**
     * Get all events from the database.
     *
     * @return a list of events
     */
    @Override
    public List<Event> getEvents() {
        // We should have a map of ID -> category fetched
        // when this provider was constructed.

        List<Event> result = new ArrayList<>();

        var query = "SELECT event_date, event_description, category_id FROM event";

        // Use the try-with-resources statement to get a connection,
        // a statement, and a result set, so that they will be closed
        // automatically.
        try (var connection = DriverManager.getConnection(url);
             var statement = connection.createStatement();
             var rs = statement.executeQuery(query)) {
            //System.out.println("Connected to SQLite database.");
            while (rs.next()) {
                String dateString = rs.getString("event_date");
                String descriptionString = rs.getString("event_description");
                int categoryId = rs.getInt("category_id");
                //System.out.printf("%s%s%d%n", dateString, descriptionString, categoryId);

                Event event = EventFactory.makeEvent(dateString, descriptionString,
                        this.categories.get(categoryId));
                result.add(event);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return result;
    }

    // Gets the category ID for the specified category.
    // Note that there could be categories with the same value
    // but different IDs. This method will find one of them, but
    // it's a good idea to make sure that the category values
    // stored in the database are unique.
    private Integer getCategoryId(Category category) {
        for (Map.Entry<Integer, String> entry : this.categories.entrySet()) {
            if (entry.getValue().equals(category.toString())) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public List<Event> getEventsOfCategory(Category category) {
        // We should have the categories fetched already.
        // Find the category ID of the specified category.
        Integer categoryId = this.getCategoryId(category);
        if (categoryId == null) {  // category not found
            return List.of();  // return empty list
        }

        List<Event> result = new ArrayList<>();

        var query = "SELECT event_date, event_description FROM event"
                + " WHERE category = " + categoryId;

        try (var connection = DriverManager.getConnection(url);
             var statement = connection.createStatement();
             var rs = statement.executeQuery(query)) {
            while (rs.next()) {
                String dateString = rs.getString("event_date");
                String descriptionString = rs.getString("event_description");

                Event event = EventFactory.makeEvent(dateString, descriptionString,
                        this.categories.get(categoryId));
                result.add(event);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return result;
    }

    @Override
    public List<Event> getEventsOfDate(MonthDay monthDay) {
        List<Event> result = new ArrayList<>();

        var query = "SELECT strftime('%m-%d', event_date) as ed, event_description, category_id FROM event"
                + " WHERE ed = '" + monthDay.toString().substring(2) + "'";

        try (var connection = DriverManager.getConnection(url);
             var statement = connection.createStatement();
             var rs = statement.executeQuery(query)) {
            while (rs.next()) {
                String dateString = rs.getString("ed");
                String descriptionString = rs.getString("event_description");
                int categoryId = rs.getInt("category_id");
                String categoryString = this.categories.get(categoryId);

                Event event = EventFactory.makeEvent("--" + dateString,
                        descriptionString, categoryString);
                result.add(event);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return result;
    }

    @Override
    public String getIdentifier() {
        return "sqlite";
    }

    public void addEvent(Event event) {

        var categoryId = getCategoryId(event.getCategory());
        //System.out.println(categoryId);

        // If the category is not found, throw an exception.
        // Todo-> create new category if not found
        if (categoryId == null) {
            throw new IllegalArgumentException("Category not found");
        }

        var query = "";
        if (event instanceof AnnualEvent){
            query = "INSERT INTO event (event_date, event_description, category_id)"
                + " VALUES ('" + event.getMonthDay().toString() + "', '" + event.getDescription() + "', "
                + categoryId + ")";
        }
        if ( event instanceof SingularEvent){
            SingularEvent singularEvent = (SingularEvent) event;
            query = "INSERT INTO event (event_date, event_description, category_id)"
                + " VALUES ('" + singularEvent.getDate().toString() + "', '" + event.getDescription() + "', "
                + categoryId + ")";
        }

        try (var connection = DriverManager.getConnection(url);
             var statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

}
