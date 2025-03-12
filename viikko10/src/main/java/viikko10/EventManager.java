package viikko10;

import java.time.MonthDay;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import viikko10.datamodel.Event;
import viikko10.providers.EventProvider;
import viikko10.filters.EventFilter;

/**
 * Manages and queries the events available from event providers.
 */
public class EventManager {
    private static final EventManager INSTANCE = new EventManager();

    private final List<EventProvider> eventProviders;
    private final List<Event> events;

    private EventManager() {
        this.eventProviders = new ArrayList<>();
        this.events = new ArrayList<>();
    }

    /**
     * Gets the only instance of the event manager.
     *
     * @return the instance
     */
    public static EventManager getInstance() {
        return INSTANCE;
    }

    /**
     * Adds an event provider to the manager's list if it isn't
     * already there.

     * @param provider the event provider to add
     * @return <code>true</code> if the provider was added, <code>false</code> otherwise
     */
    public boolean addEventProvider(EventProvider provider) {
        if (this.eventProviders.stream().noneMatch(
                (p) -> p.getIdentifier().equals(provider.getIdentifier()))) {
            this.eventProviders.add(provider);
            return true;
        }
        return false;
    }

    /**
     * Removes the specified event provider from the manager's list.
     *
     * @param providerId the identifier of the event provider to remove
     * @return <code>true</code> if the provider was removed, <code>false</code> if not
     */
    public boolean removeEventProvider(String providerId) {
        return this.eventProviders.removeIf(
                p -> p.getIdentifier().equals(providerId));
    }

    /**
     * Get all the events available from all registered event providers.
     *
     * @return the list of all events
     */
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>(this.events);

        Consumer<EventProvider> adder =
                provider -> events.addAll(provider.getEvents());
        this.eventProviders.forEach(adder);

        return events;
    }

        /**
     * Adds an event to the manager's list of events.
     *
     * @param event the event to add
     */
    public void addEvent(Event event) {
        this.events.add(event);
    }

    /**

    /*
    public List<Event> getEventsOfDate(MonthDay monthDay) {
        List<Event> events = new ArrayList<>();

        for (EventProvider provider : this.eventProviders) {
            events.addAll(provider.getEventsOfDate(monthDay));
        }

        return events;
    }
    */

    /**
     * Gets the number of event providers for the manager.
     *
     * @return the number of event providers
     */
    public int getEventProviderCount() {
        return this.eventProviders.size();
    }

    /**
     * Gets an event provider by its identifier.
     *
     * @param identifier the identifier of the event provider
     * @return the event provider, or null if not found
     */
    public EventProvider getEventProviderById(String identifier) {
        return this.eventProviders.stream()
                .filter(provider -> provider.getIdentifier().equals(identifier))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets the identifiers of all event providers of the manager.
     *
     * @return list of provider identifiers
     */
    public List<String> getEventProviderIdentifiers() {
        return this.eventProviders.stream()
                .map(EventProvider::getIdentifier)
                .toList();
    }

    /**
     * Gets the events that are accepted by the specified filter.
     *
     * @param filter the filter
     * @return list of events
     */
    public List<Event> getFilteredEvents(EventFilter filter) {
        return this.getAllEvents().stream()
                .filter(event -> filter.accepts(event))
                .toList();
    }
}
