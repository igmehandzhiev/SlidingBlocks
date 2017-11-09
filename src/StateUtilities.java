import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

public class StateUtilities {

    private static final Map<Integer, int[]> goalMap = new HashMap<>();

    @NotNull
    static State generateInitState(int n) {
        final int length = n + 1;
        final int puzzleSize = (int) Math.sqrt(n);
        int[][] initPuzzle = new int[puzzleSize + 1][puzzleSize + 1];
        HashSet<Integer> numbers = new HashSet<>(length);

        int[] emptyCoordinates = new int[2];

        for (int x = 0; x <= puzzleSize; ++x) {
            for (int y = 0; y <= puzzleSize; ++y) {
                int random = new Random().nextInt(length);
                while (numbers.contains(random)) {
                    random = new Random().nextInt(length);
                }
                initPuzzle[x][y] = random;
                numbers.add(random);

                if (random == 0) {
                    emptyCoordinates[0] = x;
                    emptyCoordinates[1] = y;

                }
            }
        }
        State initState = new State(initPuzzle);
        initState.setgScore(0);
        initState.sethScore(hCostEstimate(initPuzzle));
        initState.setEmptyCoordinates(emptyCoordinates);
        initState.setParent(null);

        return initState;
    }

    @NotNull
    static int[][] getGoalPuzzle(int n) {
        final int puzzleSize = (int) Math.sqrt(n);
        int[][] goalPuzzle = new int[puzzleSize + 1][puzzleSize + 1];
        int number = 1;

        for (int x = 0; x <= puzzleSize; ++x) {
            for (int y = 0; y <= puzzleSize; ++y) {
                goalPuzzle[x][y] = number;
                int[] coordinates = new int[2];
                coordinates[0] = x;
                coordinates[1] = y;
                goalMap.put(number, coordinates);
                number++;
            }
        }
        goalPuzzle[puzzleSize][puzzleSize] = 0;
        int[] zeroCoordinates = new int[2];
        zeroCoordinates[0] = puzzleSize;
        zeroCoordinates[1] = puzzleSize;
        goalMap.put(0, zeroCoordinates);

        return goalPuzzle;
    }

    @NotNull
    static Integer hCostEstimate(@NotNull int[][] start) {
        Integer sum = 0;
        for (int x = 0; x < start.length; ++x) {
            for (int y = 0; y < start.length; ++y) {
                final int i = start[x][y];
                final int x2 = goalMap.get(i)[0];
                final int y2 = goalMap.get(i)[1];
                sum += (Math.abs(x - x2) + Math.abs(y - y2));
            }
        }
        return sum;
    }

    static boolean isSolvable(@NotNull int[][] puzzle, int[] emptyCoordinates) {
        int[] tiles = new int[puzzle.length * puzzle.length - 1];
        int i = 0;
        for (int[] row :
                puzzle) {
            for (int number :
                    row) {
                if (number == 0) {
                    continue;
                }
                tiles[i++] = number;
            }
        }

        int inversions = 0;

        for (int current = 0; current < tiles.length; ++current) {
            for (int next = current + 1; next < tiles.length; ++next) {

                if (tiles[current] > tiles[next]) {
                    inversions++;
                }
            }
        }

        /*
        The formula says:
        If the grid width is odd, then the number of inversions in a solvable situation is even.

        If the grid width is even, and the blank is on an even row counting from the bottom (second-last, fourth-last etc),
        then the number of inversions in a solvable situation is odd.

        If the grid width is even, and the blank is on an odd row counting from the bottom (last, third-last, fifth-last etc)
        then the number of inversions in a solvable situation is even.

        ( (grid width odd) && (#inversions even) )  ||
        ( (grid width even) && ((blank on odd row from bottom) == (#inversions even)) )
         */

        return ((puzzle.length % 2 == 1) && (inversions % 2 == 0)) ||
                ((puzzle.length % 2 == 0) && ((emptyCoordinates[0] % 2 == 0) == (inversions % 2 == 0)));
    }
}
