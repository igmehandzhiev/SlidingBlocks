import java.util.*;

/**
 * @author <a href = "mailto:imehandzhiev@intracol.com">imehandzhiev</a>
 * @since 06-Nov-17
 */
public class SlidingBlocksApp {

    static final LowestFScore lowestFScore = new LowestFScore();
    static LinkedList<State> path = new LinkedList<>();
    static HashMap<Integer, int[]> goalMap = new HashMap<>();

    private static Set<State> closedSet = new HashSet<>();
    private static PriorityQueue<State> openStates = new PriorityQueue<>(lowestFScore);
//    private static HashMap<State, State> cameFrom = new HashMap<>();

    private static int iterations = 0;

    static LinkedList<State> aStar(int n) {

        final State goal = new State(getGoalPuzzle(n), Integer.MAX_VALUE, 0);
        State initState = generateInitState(n);

        while (!isSolvable(initState.getPuzzle(), initState.getEmptyCoordinates())) {
            initState = generateInitState(n);
        }

        System.out.println("INIT_STATE" + initState);

        openStates.add(initState);

        int i = 0;
        while (!openStates.isEmpty()) {

            State current = openStates.peek();

//            System.out.println("Open States :" + openStates.size());
//            for (State st :
//                    openStates) {
//
//
//                System.out.println(i + " " + st);
//            }
//            i++;
//
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

//                System.out.println("ADD_CHILD : " + iterations++);
                iterations++;
            }
        }
        return path;


    }

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

    private static Iterable<? extends State> currentNeighbours(State current, int[] emptyCoordinates) {


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

        iterations++;
//        System.out.println("GENERATE_NEIGHBOURS : " + iterations++);

        return children;
    }

    private static boolean canMove(State current, Direction direction, int[] emptyCoordinates) {

//        System.out.println("CAN_MOVE : " + iterations++);
        iterations++;

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


    private static State generateChild(State current, Direction direction, int[] emptyCoordinates) {
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
        childState.sethScore(hCostEstimate(childState.getPuzzle()));
        childState.setEmptyCoordinates(childEmptyCoordinates);
        childState.setParent(current);
        iterations++;
//        System.out.println("RETURN_NEW_CHILD : " + iterations++);
        return childState;
    }

//
//    private static State lowestFScore(HashSet<State> openSet, HashMap<State, Integer> fScore) {
//        Iterator<State> itr = openSet.iterator();
//        State lowestFScore = itr.next();
//        while (itr.hasNext()) {
//            final State next = itr.next();
//            if (fScore.get(next) < fScore.get(lowestFScore)) {
//                lowestFScore = next;
//            }
//        }
//        return lowestFScore;
//    }

    private static Integer hCostEstimate(int[][] start) {
        Integer sum = 0;
        for (int x = 0; x < start.length; ++x) {
            for (int y = 0; y < start.length; ++y) {
                final int i = start[x][y];
                final int x2 = goalMap.get(i)[0];
                final int y2 = goalMap.get(i)[1];
                sum += (Math.abs(x - x2) + Math.abs(y - y2));
                iterations++;
            }
        }
        iterations++;
        return sum;
    }


    private static LinkedList<State> reconstructPath(State current) {
        LinkedList<State> totalPath = new LinkedList<>();

        while (current.getParent() != null) {
            totalPath.addFirst(current);
            current = current.getParent();
        }

        totalPath.addFirst(current);
        return totalPath;
    }

    private static boolean isSolvable(int[][] puzzle, int[] emptyCoordinates) {
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

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        final int n = input.nextInt();
        input.close();

        final LinkedList<State> path = SlidingBlocksApp.aStar(n);

        System.out.println("Path Size is: " + path.size());
        for (State state :
                path) {
            System.out.println(state);
        }
    }
}
