import java.util.*;

/**
 * @author <a href = "mailto:imehandzhiev@intracol.com">imehandzhiev</a>
 * @since 06-Nov-17
 */
public class SlidingBlocksApp {

    static final Comparator<State> lowestFScore = new LowestFScore();
    static LinkedList<State> path = new LinkedList<>();
    static HashMap<Integer, int[]> goalMap = new HashMap<>();


    private static HashSet<State> closedSet = new HashSet<>();
    private static PriorityQueue<State> openStates = new PriorityQueue<>(lowestFScore);
    private static HashMap<State, State> cameFrom = new HashMap<>();

    private static int iterations = 0;

    static LinkedList<State> aStar(int n) {

        final State goal = new State(getGoalPuzzle(n), (int) Math.pow(n, n), 0);
        final State initState = generatePuzzle(n);

        System.out.println("INIT_STATE" + initState);

        openStates.add(initState);

        while (!openStates.isEmpty()) {

            State current = openStates.peek();

            int i = 0;
            for (State st :
                    openStates) {
                System.out.println("CURRENT " + (i++) + ": " + st);
            }

            if (current.equals(goal)) {
                return path = reconstructPath(cameFrom, current);
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

                Integer tempFScore = neighbour.getfScore();
                Integer tempGScore = current.getgScore() + 1;
                if (tempFScore >= current.getfScore() && tempGScore >= neighbour.getgScore()) {
                    continue;
                }
                if (tempGScore <= neighbour.getgScore()) {
                }
                neighbour.setgScore(tempGScore);
                openStates.add(neighbour);


//                System.out.println("ADD_CHILD : " + iterations++);
                iterations++;
                cameFrom.put(neighbour, current);
            }
        }
        return path;


    }

    static State generatePuzzle(int n) {
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


        HashSet<State> neighbours = new LinkedHashSet<>();
        if (canMove(current, Direction.LEFT, emptyCoordinates)) {
            neighbours.add(move(current, Direction.LEFT, emptyCoordinates));
        }
        if (canMove(current, Direction.RIGHT, emptyCoordinates)) {
            neighbours.add(move(current, Direction.RIGHT, emptyCoordinates));
        }
        if (canMove(current, Direction.UP, emptyCoordinates)) {
            neighbours.add(move(current, Direction.UP, emptyCoordinates));
        }
        if (canMove(current, Direction.DOWN, emptyCoordinates)) {
            neighbours.add(move(current, Direction.DOWN, emptyCoordinates));
        }

        iterations++;
//        System.out.println("GENERATE_NEIGHBOURS : " + iterations++);

        return neighbours;
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


    private static State move(State current, Direction direction, int[] emptyCoordinates) {
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
        childState.setgScore(current.getgScore() + 1);
        childState.sethScore(hCostEstimate(childState.getPuzzle()));
        childState.setEmptyCoordinates(childEmptyCoordinates);

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


    private static LinkedList<State> reconstructPath(HashMap<State,
            State> cameFrom, State current) {
        LinkedList<State> totalPath = new LinkedList<>();
        totalPath.add(current);
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            totalPath.addLast(current);
        }
        return totalPath;
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
