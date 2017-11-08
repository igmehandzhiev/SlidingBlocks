import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * SlidingBlocksApp Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Nov 8, 2017</pre>
 */
public class SlidingBlocksAppTest {

    final int n = 8;


    /**
     * Method: aStar(State start, int[] emptyCoordinates)
     */
    @Test
    public void testAStar() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: generateInitState(int n)
     */
    @Test
    public void testGeneratePuzzle() throws Exception {
        State initState = SlidingBlocksApp.generateInitState(n);
        HashSet<Integer> numbers = new HashSet<>();

        for (int[] row :
                initState.getPuzzle()) {
            for (int num :
                    row) {
                numbers.add(num);
            }
        }

        assertEquals(numbers.size(), n + 1);
    }

    /**
     * Method: getGoalPuzzle(int n)
     */
    @Test
    public void testGetGoalPuzzle() throws Exception {
        final int[][] expectedGoalPuzzle = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0},

        };

        final int[][] goalPuzzle = SlidingBlocksApp.getGoalPuzzle(n);

        assertTrue(Arrays.deepEquals(expectedGoalPuzzle, goalPuzzle));
    }

    /**
     * Method: main(String[] args)
     */
    @Test
    public void testMain() throws Exception {
//TODO: Test goes here... 
    }


    /**
     * Method: currentNeighbours(State current, int[] emptyCoordinates)
     */
    @Test
    public void testCurrentNeighbours() throws Exception {
        Class<?> s = Class.forName("SlidingBlocksApp");
        final Object instance = s.newInstance();

        final int[][] testPuzzle = {
                {1, 2, 3},
                {4, 0, 6},
                {7, 8, 5},

        };
        State testState = new State(testPuzzle);

        int[] emptyCoordinates = new int[2];
        emptyCoordinates[0] = 0;
        emptyCoordinates[1] = 0;


        final int[][] leftNeighbourPuzzle = {
                {1, 2, 3},
                {0, 4, 6},
                {7, 8, 5},

        };
        State leftNeighbour = new State(leftNeighbourPuzzle);

        final int[][] rightNeighbourPuzzle = {
                {1, 2, 3},
                {4, 6, 0},
                {7, 8, 5},

        };
        State rightNeighbour = new State(rightNeighbourPuzzle);

        final int[][] upNeighbourPuzzle = {
                {1, 0, 3},
                {4, 2, 6},
                {7, 8, 5},

        };
        State upNeighbour = new State(upNeighbourPuzzle);

        final int[][] downNeighbourPuzzle = {
                {1, 2, 3},
                {4, 8, 6},
                {7, 0, 5},

        };
        State downNeighbour = new State(downNeighbourPuzzle);

        LinkedList<State> testNeighbours = new LinkedList<>();
        testNeighbours.addLast(leftNeighbour);
        testNeighbours.addLast(rightNeighbour);
        testNeighbours.addLast(upNeighbour);
        testNeighbours.addLast(downNeighbour);

        try {

            Method method = SlidingBlocksApp.class.getDeclaredMethod("currentNeighbours", State.class, int[].class);
//            method.setAccessible(true);
            final Object o = method.invoke(instance, testState, emptyCoordinates);
            HashSet<State> neighbours = (HashSet<State>) o;
            for (State neighbour :
                    neighbours) {
                assertTrue(Arrays.deepEquals(neighbour.getPuzzle(), testNeighbours.poll().getPuzzle()));
            }
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
    }

    /**
     * Method: currentNeighbours(State current, int[] emptyCoordinates)
     */
    @Test
    public void testCurrentNeighboursCannotMove() throws Exception {
        Class<?> s = Class.forName("SlidingBlocksApp");
        final Object instance = s.newInstance();

        final int[][] testPuzzle = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0},

        };
        State testState = new State(testPuzzle);

        int[] emptyCoordinates = new int[2];
        emptyCoordinates[0] = 2;
        emptyCoordinates[1] = 2;


        final int[][] leftNeighbourPuzzle = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 0, 8},

        };
        State leftNeighbour = new State(leftNeighbourPuzzle);


        final int[][] upNeighbourPuzzle = {
                {1, 2, 3},
                {4, 5, 0},
                {7, 8, 6},

        };
        State upNeighbour = new State(upNeighbourPuzzle);


        LinkedList<State> testNeighbours = new LinkedList<>();
        testNeighbours.addLast(leftNeighbour);
        testNeighbours.addLast(upNeighbour);


        try {
            Method method = SlidingBlocksApp.class.getDeclaredMethod("currentNeighbours", State.class, int[].class);
            method.setAccessible(true);
            final Object o = method.invoke(instance, testState, emptyCoordinates);
            HashSet<State> neighbours = (HashSet<State>) o;
            for (State neighbour :
                    neighbours) {
                assertTrue(Arrays.deepEquals(neighbour.getPuzzle(), testNeighbours.poll().getPuzzle()));
            }
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
    }

    /**
     * Method: canMove(State current, Direction direction, int[] emptyCoordinates)
     */
    @Test
    public void testCanMove() throws Exception {

        final int[][] testPuzzle = {
                {0, 2, 3},
                {4, 1, 6},
                {7, 8, 5},

        };
        State testState = new State(testPuzzle);

        int[] emptyCoordinates = new int[2];
        emptyCoordinates[0] = 0;
        emptyCoordinates[1] = 0;

        Class<?> s = Class.forName("SlidingBlocksApp");
        final Object instance = s.newInstance();


        Method canMove = SlidingBlocksApp.class.getDeclaredMethod("canMove", State.class, Direction.class, int[].class);
        canMove.setAccessible(true);
        assertTrue(!(Boolean) canMove.invoke(instance, testState, Direction.LEFT, emptyCoordinates));

    }

    /**
     * Method: move(State current, Direction direction, int[] emptyCoordinates)
     */
    @Test
    public void testMove() throws Exception {
//TODO: Test goes here... 
/* 
try { 
   Method method = SlidingBlocksApp.getClass().getMethod("move", State.class, Direction.class, int[].class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: hCostEstimate(int[][] start)
     */
    @Test
    public void testHCostEstimate() throws Exception {
//TODO: Test goes here... 
/* 
try { 
   Method method = SlidingBlocksApp.getClass().getMethod("hCostEstimate", int[][].class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: reconstructPath(HashMap<State,
     * State> cameFrom, State current)
     */
    @Test
    public void testReconstructPath() throws Exception {
//TODO: Test goes here... 
/* 
try { 
   Method method = SlidingBlocksApp.getClass().getMethod("reconstructPath", HashMap<State,
.class, State.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

} 
