public class EnvTest {
    public static void main(String[] args) {
        String name = args[0];
        System.out.println(name + " = " + System.getenv(name));
    }
}
