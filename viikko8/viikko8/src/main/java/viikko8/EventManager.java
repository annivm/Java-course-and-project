package viikko8;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages and queries the events available from event providers.
 */
public class EventManager {
    private static final EventManager INSTANCE = new EventManager();

    private final List<EventProvider> eventProviders;

    private EventManager() {
        this.eventProviders = new ArrayList<>();
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
        // Check if this event provider is already on the list.
        // mikäli löytyy jo listalta, ei lisätä
        for ( EventProvider prov : this.eventProviders){
            if (prov.getIdentifier() == provider.getIdentifier()){
                System.out.println("Given EventProvider already exist");
                return false;
            }
        }
        // lisätään listalle
        this.eventProviders.add(provider);
        return true;
    }

    /**
     * Removes the specified event provider from the manager's list.
     *
     * @param providerId the identifier of the event provider to remove
     * @return <code>true</code> if the provider was removed, <code>false</code> if not
     */
    public boolean removeEventProvider(String providerId) {

        // käydään eventProviders läpi yksitellen ja verrataan annettuun providerId
        // poistetaan listalta mikäli vastaava identifier löytyy
        for ( EventProvider prov : this.eventProviders){
            if (prov.getIdentifier() == providerId){
                this.eventProviders.remove(prov);
                return true;
            }
        }
        System.out.println("No match for given providerId, no providers was removed.");
        return false;
    }

    /**
     * Get all the events available from all registered event providers.
     *
     * @return the list of all events
     */
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();

        for (EventProvider provider : this.eventProviders) {
            events.addAll(provider.getEvents());
        }

        Collections.sort(events);
        Collections.reverse(events);

        return events;
    }

    /**
     * Gets the number of event providers for the manager.
     *
     * @return the number of event providers
     */
    public int getEventProviderCount() {
        return this.eventProviders.size();
    }

    /**
     * Gets the identifiers of all event providers of the manager.
     *
     * @return list of provider identifiers
     */
    public List<String> getEventProviderIdentifiers() {

        List<String> eventProviderIdentifiers = new ArrayList<>();

        // Haetaan kaikki identifiers ja lisätään listalle
        for (EventProvider provider : this.eventProviders) {
            eventProviderIdentifiers.add(provider.getIdentifier());
        }

        // järjestely ja palautus
        Collections.sort(eventProviderIdentifiers);
        Collections.reverse(eventProviderIdentifiers);

        return eventProviderIdentifiers;

    }
}