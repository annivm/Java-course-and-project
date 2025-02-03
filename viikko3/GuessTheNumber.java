import java.util.Random;
import java.util.Scanner;

public class GuessTheNumber {

    public static void main( String[] args){

        Scanner input = new Scanner(System.in); // luodaan inputille scanner

        Random r = new Random();    // luodaan uusi olio
        int number = r.nextInt(100) + 1;    // tallennetaan muuttujaan olion arvo
        int guesses = 7;    // jäljellä olevat arvaukset
        int guess;      // käyttäjän arvaus


        System.out.println("This is a numeronarvauspeli.");

        while (guesses > 0) {
            System.out.println("Number of guesses left: " + guesses);
            System.out.print("Your guess: ");

            // pieni virheenkäsittely käyttäjän syötteelle
            try {
                guess = input.nextInt();
            } catch (Exception e) {
                System.out.println("Please enter a number.");
                input.nextLine();
                continue;
            }
            guesses--;

            if (guess == number) {
                System.out.println("Congratulations, You guessed the right number!");
                System.exit(0);
            }else if(guesses == 0){
                System.out.println("Wrong again!");
                System.out.println("Game over :(");
                System.exit(0);
            }else{
                System.out.println("Wrong, try again!");
            }
        }
        input.close();
    }
}