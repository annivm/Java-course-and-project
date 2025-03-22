package viikko12.providers.web;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Month;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import viikko12.datamodel.AnnualEvent;
import viikko12.datamodel.Category;
import viikko12.datamodel.Event;
import viikko12.datamodel.SingularEvent;
import viikko12.providers.EventProvider;

public class WebEventProvider implements EventProvider {
    private final String identifier;
    private URI uri;
    private MonthDay date;
    private HttpClient client;
    private final ObjectMapper mapper;

    private final List<Event> events;

    public WebEventProvider(URI serverUri, String identifier) {
        this.uri = serverUri;
        this.identifier = identifier;
        this.client = HttpClient.newHttpClient();
        this.mapper = createObjectMapper();
        this.events = new ArrayList<>();
    }

    public void setMonthDay (MonthDay date) {
        this.date = date;
    }

    private static ObjectMapper createObjectMapper() {
        SimpleModule module = new SimpleModule("EventDeserializer");
        module.addDeserializer(Event.class, new EventDeserializer());
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);

        return mapper;
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
            Month eventMonth;
            int eventDay;
            if (event instanceof SingularEvent) {
                SingularEvent s = (SingularEvent) event;
                eventMonth = s.getDate().getMonth();
                eventDay = s.getDate().getDayOfMonth();
            } else if (event instanceof AnnualEvent) {
                AnnualEvent a = (AnnualEvent) event;
                eventMonth = a.getMonthDay().getMonth();
                eventDay = a.getMonthDay().getDayOfMonth();
            } else {
                throw new UnsupportedOperationException(
                        "Operation not supported for " +
                        event.getClass().getName());
            }
            if (monthDay.getMonth() == eventMonth && monthDay.getDayOfMonth() == eventDay) {
                result.add(event);
            }
        }

        return result;
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public List<Event> getEvents() {
        if (date == null) {
            date = MonthDay.now();
        }

        try {
            var eventsParameters = String.format("?date=%s", date.toString().substring(2));
            //System.out.println("date: " + eventsParameters.toString());
            String URIstring = uri.toString() + eventsParameters;
            URI serverUri = new URI(URIstring);
            //System.out.println(serverUri.toString());

            HttpRequest request = HttpRequest.newBuilder()
            .uri(serverUri)
            .GET()
            .build();

            String bodyString = null;
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            bodyString = response.body();
            int status = response.statusCode();
            // if (status != 200) {
            //     System.err.printf("HTTP response: %d%n", status);
            //     System.err.println("Response body = " + bodyString);
            // } else {
            //     System.out.println("Response headers: " + response.headers());
            //     System.out.println("Response body = " + bodyString);
            // }

            // Create a custom deserializer for Event objects
            // and register it with Jackson's object mapper.

            // Make a custom collection type for Jackson
            // representing a List<Event>.
            JavaType customClassCollection = mapper.getTypeFactory().constructCollectionType(List.class, Event.class);

            // Use Jackson to parse the response body string as a List<Event>.
            List<Event> webEvents = mapper.readValue(bodyString, customClassCollection);
            this.events.addAll(webEvents);

        } catch (IOException | InterruptedException ex) {
            System.err.println("Error sending HTTP request: " + ex.getLocalizedMessage());
        } catch (URISyntaxException use) {
            System.err.println("Error making URI: " + use.getLocalizedMessage());
        }


        return this.events;
    }

}
