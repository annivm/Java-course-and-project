package viikko9;

import java.time.MonthDay;

public class DateFilter extends EventFilter {
    private MonthDay monthDay;
    private int year;

    public DateFilter(MonthDay monthDay, int year) {
        this.monthDay = monthDay;
        this.year = year;
    }

    public DateFilter(MonthDay monthDay) {
        this.monthDay = monthDay;
        this.year = 0;
    }

    public MonthDay getMonthDay(){
        return this.monthDay;
    }

    public int getYear(){
        return this.year;
    }

    public boolean accepts(Event event){
        if(event instanceof AnnualEvent){
            AnnualEvent e = (AnnualEvent) event;
            if (e.getMonthDay().equals(this.monthDay) &&
                this.year == 0) {
                return true;
            }
            else {
                return false;
            }
        }
        if (event instanceof SingularEvent) {
            SingularEvent e = (SingularEvent) event;
            if (e.getMonthDay().equals(this.monthDay) &&
                e.getYear() == this.getYear()){
                return true;
            }
        }
        return false;
    }
}
