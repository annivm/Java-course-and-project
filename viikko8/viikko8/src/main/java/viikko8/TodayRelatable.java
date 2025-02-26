package viikko8;

public interface TodayRelatable {
    public enum Relation {
        BEFORE_TODAY, TODAY, AFTER_TODAY
    }

    Relation getTodayRelation();
    long getTodayDifference();
}