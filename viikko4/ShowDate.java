import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class ShowDate {
    public static void main(String[] args ){
        TimeZone tz = TimeZone.getDefault();
        OffsetDateTime now = OffsetDateTime.now(tz.toZoneId());
        DateTimeFormatter fmt = DateTimeFormatter.RFC_1123_DATE_TIME;
        String output = now.format(fmt);
        System.out.println(output);
    }
}
