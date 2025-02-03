// yhdessä lähdekooditiedostossa voi olla yksi public class -luokka
// ja tiedoston oltava samanniminen kuin luokka on

import java.util.Random;

public class RandomInteger{

    public static void main( String[] args ){
        Random r = new Random();   
    
        int value = r.nextInt(100) + 1;

        System.out.println(value);
    }

}