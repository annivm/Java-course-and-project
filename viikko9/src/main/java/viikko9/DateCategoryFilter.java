package viikko9;

import java.time.MonthDay;

public class DateCategoryFilter extends EventFilter {
    private Category category;
    private MonthDay monthDay;

    public DateCategoryFilter(MonthDay monthDay, Category category){
        this.monthDay = monthDay;
        this.category = category;
    }

    public Category getCategory(){
        return this.category;
    }

    public MonthDay getMonthDay(){
        return this.monthDay;
    }

    public boolean accepts(Event event){
        Category c = (Category) event.getCategory();
        if (event.getMonthDay().equals(this.monthDay)){
            if (c.getSecondary() == null &&
                c.getPrimary().equals(this.category.getPrimary())){
                return true;
            }
            if (c.getPrimary().equals(this.category.getPrimary()) &&
                c.getSecondary().equals(this.category.getSecondary())){
                return true;
            }
        }
        return false;
    }
}

