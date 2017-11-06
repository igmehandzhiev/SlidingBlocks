import java.util.*;

/**
 * @author <a href = "mailto:imehandzhiev@intracol.com">imehandzhiev</a>
 * @since 06-Nov-17
 */
public class SlidingBlocksApp {

    static LinkedList<int[][]> path = new LinkedList<>();

    static LinkedList<int[][]> aStar(int[][] start, int[][] goal, int[] emptyCoordinates) {
        HashSet<int[][]> closedSet = new HashSet<>();

        HashSet<int[][]> openSet = new HashSet<>();
        openSet.add(start);

        HashMap<int[][], int[][]> cameFrom = new HashMap<>();

        HashMap<int[][], Integer> gScore = new HashMap<>();
        gScore.put(start, 0);

        HashMap<int[][], Integer> fScore = new HashMap<>();
        fScore.put(start, hCostEstimate(start, goal));

        while (!openSet.isEmpty()) {
            int[][] current = lowestFScore(openSet, fScore);

            if (Arrays.equals(current, goal)) {
                return path = reconstructPath(cameFrom, current);
            }

            openSet.remove(current);
            closedSet.add(current);

            for (int[][] neighbour :
                    currentNeighbours(current)) {
                if (closedSet.contains(neighbour)) {
                    continue;
                }

                if (!openSet.contains(neighbour)) {
                    openSet.add(neighbour);
                }

                Integer tempGScore = gScore.get(current) + 1;
//                Integer tempGScore = gScore.get(current) + distBetween(current, neighbour);
                if (tempGScore >= gScore.get(neighbour)) {
                    continue;
                }

                cameFrom.put(neighbour, current);
                gScore.put(neighbour, tempGScore);
                fScore.put(neighbour, (gScore.get(neighbour) + hCostEstimate(neighbour, goal)));
            }
        }
        return path;


    }

    static int[][] generatePuzzle(int n) {
        final int length = n + 1;
        final int puzzleSize = (int) Math.sqrt(n);
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

        return initPuzzle;
    }

    static int[][] getGoalPuzzle(int n) {
        final int puzzleSize = (int) Math.sqrt(n);
        int[][] goalPuzzle = new int[puzzleSize + 1][puzzleSize + 1];
        int number = 1;

        for (int x = 0; x <= puzzleSize; ++x) {
            for (int y = 0; y <= puzzleSize; ++y) {
                goalPuzzle[x][y] = number;
                number++;
            }
        }
        goalPuzzle[puzzleSize][puzzleSize] = 0;

        return goalPuzzle;
    }

    private static Iterable<? extends int[][]> currentNeighbours(int[][] current) {
        HashSet<int[][]> neighbours = new LinkedHashSet<>();
        if (canMove(current, Direction.LEFT)) {
            neighbours.add(move(current, Direction.LEFT));
        }
        if (canMove(current, Direction.RIGHT)) {
            neighbours.add(move(current, Direction.RIGHT));
        }
        if (canMove(current, Direction.UP)) {
            neighbours.add(move(current, Direction.UP));
        }
        if (canMove(current, Direction.DOWN)) {
            neighbours.add(move(current, Direction.DOWN));
        }
        return neighbours;
    }

    private static boolean canMove(int[][] current, Direction direction, int[] emptyCoordinates) {

        switch (direction) {
            case LEFT:
                return emptyCoordinates[1] - 1 >= 0;
            case RIGHT:
                return emptyCoordinates[1] + 1 <= (Math.sqrt(current[0].length) - 1);
            case UP:
                return emptyCoordinates[0] - 1 >= 0;
            case DOWN:
                return emptyCoordinates[0] + 1 <= (Math.sqrt(current[0].length) - 1);
            default:
                return false;
        }
    }


    private static int[][] move(int[][] current, Direction direction, int[] emptyCoordinates) {
        int[][] childState = new int[current.length][];
        for (int i = 0; i < current.length; i++) {
            int[] tempMatrix = current[i];
            int tempLength = tempMatrix.length;
            childState[i] = new int[tempLength];
            System.arraycopy(tempMatrix, 0, childState[i], 0, tempLength);
        }

        switch (direction) {
            case DOWN:
                int temp = childState[emptyCoordinates[0]][emptyCoordinates[1]];

            case UP:
            case RIGHT:
            case LEFT:
        }
        return childState;
    }


    private static int[][] lowestFScore(HashSet<int[][]> openSet, HashMap<int[][], Integer> fScore) {
        Iterator<int[][]> itr = openSet.iterator();
        int[][] lowestFScore = itr.next();
        while (itr.hasNext()) {
            final int[][] next = itr.next();
            if (fScore.get(next) < fScore.get(lowestFScore)) {
                lowestFScore = next;
            }
        }
        return lowestFScore;
    }

    private static Integer hCostEstimate(int[][] start, int[][] goal) {
        Integer sum = 0;
        final int length = start.size();
        for (int i = 0; i < length; ++i) {
            final int x1 = start.get(i)[0];
            final int x2 = goal.get(i)[0];
            final int y1 = start.get(i)[1];
            final int y2 = goal.get(i)[1];
            sum += (Math.abs(x1 - x2) + Math.abs(y1 - y2));
        }
        return sum;
    }

    private static LinkedList<int[][]> reconstructPath(HashMap<int[][],
            int[][]> cameFrom, int[][] current) {
        LinkedList<int[][]> totalPath = new LinkedList<>();
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

        final int[][] initPuzzle = generatePuzzle(n);
        final int[][] goalPuzzle = getGoalPuzzle(n);

        final LinkedList<int[][]> path = aStar(initPuzzle, goalPuzzle);

        Iterator it = path.iterator();

        // Iterating the list in forward direction
        System.out.println("LinkedList elements:");
        while (it.hasNext()) {
            System.out.println(it.next());
        }


//        int[][] goalPuzzle = new int[][];
//        int number = 1;
//        final int puzzleSize = (int) Math.sqrt(8);
//
//        for (int x = 0; x <= puzzleSize; ++x) {
//            for (int y = 0; y <= puzzleSize; ++y) {
//                int[] coordinates = new int[2];
//                coordinates[0] = x;
//                coordinates[1] = y;
//                goalPuzzle.put(number, coordinates);
//                number++;
//
//            }
//        }
//
//        int[] coord = new int[2];
//        coord[0] = puzzleSize;
//        coord[1] = puzzleSize;
//        goalPuzzle.put(0, coord);
//
//        System.out.println("Hashtable contains:");
//
//    /* public Set<K> keySet(): Returns a Set view of the keys
//     * contained in this map. The set is backed by the map,
//     * so changes to the map are reflected in the set, and
//     * vice-versa.
//     */
//        Set<Integer> keys = goalPuzzle.keySet();
//
//        //Obtaining iterator over set entries
//        Iterator<Integer> itr = keys.iterator();
//
//        Integer str;
//        //Displaying Key and value pairs
//        while (itr.hasNext()) {
//            // Getting Key
//            str = itr.next();
//
//       /* public V get(Object key): Returns the value to which
//        * the specified key is mapped, or null if this map
//        * contains no mapping for the key.
//        */
//            System.out.println("Key: " + str + " & Value: " + Arrays.toString(goalPuzzle.get(str)));
//        }
    }
}
