package today.filters;

import today.datamodel.Event;

/**
 * Filter that accepts any event.
 */
public class IdentityFilter extends EventFilter {
    @Override
    public boolean accepts(Event event) {
        return true;
    }
}
