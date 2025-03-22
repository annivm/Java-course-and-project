package viikko12.filters;

import viikko12.datamodel.Event;

/**
 * Abstract base class for event filters.
 */
public abstract class EventFilter {
    /**
     * Predicate for accepting or rejecting the specified event.
     *
     * @param event the event to accept or reject
     * @return <code>true</code> if the event is accepted,
     * <code>false</code> if not
     */
    public boolean accepts(Event event) {
        return false;
    }
}
