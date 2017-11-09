import java.util.List;
import java.util.Scanner;

/**
 * @author <a href = "mailto:imehandzhiev@intracol.com">imehandzhiev</a>
 * @since 06-Nov-17
 */
public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int n = input.nextInt();

        while ((n < 4) || ((Math.sqrt(n + 1) % 2 != 1.0) && (Math.sqrt(n + 1) % 2 != 0.0))) {
            System.out.println((Math.sqrt(n + 1) % 2 != 1.0));
            System.out.println((Math.sqrt(n + 1) % 2));
            System.out.println("Type proper number: ");
            n = input.nextInt();
        }
        input.close();

        final List<State> path = SlidingBlocksApp.aStar(n);

        System.out.println("Path Size is: " + path.size());
        for (State state :
                path) {
            System.out.println(state);
        }
    }
}
