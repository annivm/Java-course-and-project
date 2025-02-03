import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


public class Birthday {
    public static void main(String[] args) {

        System.out.println("Please save your birthday as an enviromental variable called BIRTHDAY (form: YYYY-MM-DD)");
        String birthday_String = System.getenv("BIRTHDAY");

        LocalDate birthday = LocalDate.parse(birthday_String);
        LocalDate now = LocalDate.now();

        if (now.getMonth() == birthday.getMonth() && now.getDayOfMonth() == birthday.getDayOfMonth()){
            System.out.println("HAPPY BIRTHDAY!");
        }else{
            System.out.println("Today is not your birthday...");
        }

        long ageInDays = ChronoUnit.DAYS.between(birthday, now);

        if (birthday.isAfter(now)){
            System.out.println("Your birthday is in the future???");
        }
        else{
            System.out.println("Your are " + ageInDays + " days old!");
        }
        if (ageInDays % 1000 == 0){
            System.out.println("What a nice round number!");
        }
    }
}
