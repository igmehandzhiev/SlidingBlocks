import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author <a href = "mailto:imehandzhiev@intracol.com">imehandzhiev</a>
 * @since 06-Nov-17
 */
class SlidingBlocksApp {

    private static final LowestFScore lowestFScore = new LowestFScore();
    @NotNull
    private static List<State> path = new LinkedList<>();

    private static final Set<State> closedSet = new HashSet<>();
    private static final PriorityQueue<State> openStates = new PriorityQueue<>(lowestFScore);

    @NotNull
    static List<State> aStar(int n) {

        final State goal = new State(StateUtilities.getGoalPuzzle(n), Integer.MAX_VALUE, 0);
        State initState = StateUtilities.generateInitState(n);

        while (!StateUtilities.isSolvable(initState.getPuzzle(), initState.getEmptyCoordinates())) {
            initState = StateUtilities.generateInitState(n);
        }

        openStates.add(initState);

        while (!openStates.isEmpty()) {

            State current = openStates.peek();

            if (current.equals(goal)) {
                return path = reconstructPath(current);
            }

            closedSet.add(openStates.poll());

            for (State neighbour :
                    currentNeighbours(current, current.getEmptyCoordinates())) {

                if (closedSet.contains(neighbour)) {
                    continue;
                }

                if (!openStates.contains(neighbour)) {
                    openStates.add(neighbour);
                }
            }
        }
        return path;
    }

    @NotNull
    private static Iterable<? extends State> currentNeighbours(@NotNull State current, int[] emptyCoordinates) {


        HashSet<State> children = new LinkedHashSet<>();
        if (canMove(current, Direction.LEFT, emptyCoordinates)) {
            children.add(generateChild(current, Direction.LEFT, emptyCoordinates));
        }
        if (canMove(current, Direction.RIGHT, emptyCoordinates)) {
            children.add(generateChild(current, Direction.RIGHT, emptyCoordinates));
        }
        if (canMove(current, Direction.UP, emptyCoordinates)) {
            children.add(generateChild(current, Direction.UP, emptyCoordinates));
        }
        if (canMove(current, Direction.DOWN, emptyCoordinates)) {
            children.add(generateChild(current, Direction.DOWN, emptyCoordinates));
        }

        return children;
    }

    private static boolean canMove(@NotNull State current, @NotNull Direction direction, int[] emptyCoordinates) {

        switch (direction) {
            case LEFT:
                return emptyCoordinates[1] - 1 >= 0;
            case RIGHT:
                return emptyCoordinates[1] + 1 <= ((current.getPuzzle()[0].length) - 1);
            case UP:
                return emptyCoordinates[0] - 1 >= 0;
            case DOWN:
                return emptyCoordinates[0] + 1 <= ((current.getPuzzle()[0].length) - 1);
            default:
                return false;
        }
    }


    @NotNull
    private static State generateChild(@NotNull State current, @NotNull Direction direction, int[] emptyCoordinates) {
        final int[][] currentPuzzle = current.getPuzzle();
        int[][] childPuzzle = new int[currentPuzzle.length][];
        for (int i = 0; i < currentPuzzle.length; i++) {
            int[] tempMatrix = currentPuzzle[i];
            int tempLength = tempMatrix.length;
            childPuzzle[i] = new int[tempLength];
            System.arraycopy(tempMatrix, 0, childPuzzle[i], 0, tempLength);

        }

        int temp;
        int[] childEmptyCoordinates = new int[2];
        switch (direction) {
            case DOWN:
                childEmptyCoordinates[0] = emptyCoordinates[0] + 1;
                childEmptyCoordinates[1] = emptyCoordinates[1];
                temp = childPuzzle[childEmptyCoordinates[0]][childEmptyCoordinates[1]];
                childPuzzle[childEmptyCoordinates[0]][childEmptyCoordinates[1]] = 0;
                childPuzzle[emptyCoordinates[0]][emptyCoordinates[1]] = temp;
                break;
            case UP:
                childEmptyCoordinates[0] = emptyCoordinates[0] - 1;
                childEmptyCoordinates[1] = emptyCoordinates[1];
                temp = childPuzzle[childEmptyCoordinates[0]][childEmptyCoordinates[1]];
                childPuzzle[childEmptyCoordinates[0]][childEmptyCoordinates[1]] = 0;
                childPuzzle[emptyCoordinates[0]][emptyCoordinates[1]] = temp;
                break;
            case RIGHT:
                childEmptyCoordinates[0] = emptyCoordinates[0];
                childEmptyCoordinates[1] = emptyCoordinates[1] + 1;
                temp = childPuzzle[childEmptyCoordinates[0]][childEmptyCoordinates[1]];
                childPuzzle[childEmptyCoordinates[0]][childEmptyCoordinates[1]] = 0;
                childPuzzle[emptyCoordinates[0]][emptyCoordinates[1]] = temp;
                break;
            case LEFT:
                childEmptyCoordinates[0] = emptyCoordinates[0];
                childEmptyCoordinates[1] = emptyCoordinates[1] - 1;
                temp = childPuzzle[childEmptyCoordinates[0]][childEmptyCoordinates[1]];
                childPuzzle[childEmptyCoordinates[0]][childEmptyCoordinates[1]] = 0;
                childPuzzle[emptyCoordinates[0]][emptyCoordinates[1]] = temp;
                break;
        }

        State childState = new State(childPuzzle);
        childState.sethScore(StateUtilities.hCostEstimate(childState.getPuzzle()));
        childState.setEmptyCoordinates(childEmptyCoordinates);
        childState.setParent(current);

        return childState;
    }


    @NotNull
    private static List<State> reconstructPath(@NotNull State current) {
        LinkedList<State> totalPath = new LinkedList<>();

        while (current.getParent() != null) {
            totalPath.addFirst(current);
            current = current.getParent();
        }

        totalPath.addFirst(current);
        return totalPath;
    }


}
