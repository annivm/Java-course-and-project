import java.util.Arrays;

public class Sum {
    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++){
            System.out.println(i + " = " + args[i]);
        }

        System.out.println(Arrays.toString(args));

        int [] numbers = new int[args.length];
        System.out.println(Arrays.toString(numbers));

        //int sum = 0;
        for (int i = 0; i < args.length; i++ ){
            try{
                numbers[i] = Integer.parseInt(args[i]);
                //sum += numbers[i];
            }catch(NumberFormatException nfe){
                System.err.println("Not a number: " + args[i]);
            }
        }
        //System.out.println(Arrays.toString(args));
        int sum = Arrays.stream(numbers).sum();
        System.out.println(sum);
    }
}