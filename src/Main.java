import java.util.HashSet;
import java.util.Random;

/**
 * @author <a href = "mailto:imehandzhiev@intracol.com">imehandzhiev</a>
 * @since 06-Nov-17
 */
public class Main {
    public static void main(String[] args) {
        final int length = 8 + 1;
        final int puzzleSize = (int) Math.sqrt(8);
        int[][] initPuzzle = new int[puzzleSize + 1][puzzleSize + 1];
        HashSet<Integer> numbers = new HashSet<>(length);

        for (int x = 0; x <= puzzleSize; ++x) {
            for (int y = 0; y <= puzzleSize; ++y) {
                int random = new Random().nextInt(length);
                while (numbers.contains(random)) {
                    random = new Random().nextInt(length);
                }
                initPuzzle[x][y] = random;
                numbers.add(random);
            }
        }

        for (int row = 0; row < initPuzzle.length; row++) {
            for (int col = 0; col < initPuzzle[row].length; col++) {
                System.out.println("X: " + row + " Y: " + col + " NUMBER: " + initPuzzle[row][col]);
                // Do stuff
            }
        }
    }
}
