package viikko13.providers;

import viikko13.datamodel.Category;
import viikko13.datamodel.Event;

import java.util.List;
import java.time.MonthDay;

public interface EventProvider {
    List<Event> getEvents();
    List<Event> getEventsOfCategory(Category category);
    List<Event> getEventsOfDate(MonthDay monthDay);
    String getIdentifier();
}
