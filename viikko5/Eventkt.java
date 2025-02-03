import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;

public class Eventkt {
    public static void main(String[] args) {
        Event[] events = new Event[]{
            new Event (LocalDate.of(2024, 9, 16), "macOS 15 Sequoia released", new Category("apple","macos")),
            new Event (LocalDate.of(2023, 9, 26), "macOS 14 Sonoma released", new Category("apple", "macos")),
            new Event (LocalDate.of(2022, 10, 24), "macOS 13 Ventura released", new Category("apple", "macos")),
            new Event (LocalDate.of(2021, 10, 25), "macOS 12 Monterey released", new Category("apple", "macos")),
            new Event (LocalDate.of(2020, 11, 12), "macOS 11 Big Sur released", new Category("apple", "macos"))
        };
        String[] strings = new String[5];

        for (int i = 0; i < events.length; i++){

            // haetaan kuvaus taulukosta
            String desc = events[i].getDescription();

            // etsitään aloitus ja lopetus kohdat nimen ympäriltä
            StringBuilder x = new StringBuilder(desc);
            int indexOfStart = x.indexOf(" ");
            int indexOfSEnd = x.indexOf(" released");

            // haetaan versio substringinä
            desc = desc.substring(indexOfStart, indexOfSEnd);

            // haetaan localdate päivämäärästä viikonpäivä
            DayOfWeek weekday = events[i].getDate().getDayOfWeek();

            // rakennetaan uusi string
            StringBuilder builder = new StringBuilder("macOS");
            builder.append(desc);
            builder.append(" was released on a ");
            builder.append(weekday);

            // muutetaan stringbuilder -> string
            String complete = builder.toString();
            System.out.println(complete);

            // haetaan pelkkä versionnimi
            String versionName = desc.substring(4);
            strings[i] = versionName;

        }
        // taulukon järjestely ja tulostus
        Arrays.sort(strings);
        System.out.println("In alphabetical order: " + Arrays.toString(strings));
    }
}
