import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * @author <a href = "mailto:imehandzhiev@intracol.com">imehandzhiev</a>
 * @since 06-Nov-17
 */
public class State {
    private int[][] puzzle;

    private int hScore;
    private State parent;


    int[] getEmptyCoordinates() {
        return emptyCoordinates;
    }

    void setEmptyCoordinates(int... emptyCoordinates) {
        this.emptyCoordinates = emptyCoordinates;
    }

    private int[] emptyCoordinates;

    State(int[][] puzzle) {
        this.setPuzzle(puzzle);
    }

    State(int[][] puzzle, int gScore, int hScore) {
        this.setPuzzle(puzzle);
        this.setgScore(gScore);
        this.sethScore(hScore);
    }

    int[][] getPuzzle() {
        return puzzle;
    }

    private void setPuzzle(int[]... puzzle) {
        this.puzzle = puzzle;
    }

    int getfScore() {
        return getgScore() + gethScore();
    }

    private int getgScore() {
        return parent != null ? (parent.getgScore() + 1) : 0;
    }

    void setgScore(int parentGScore) {
    }

    private int gethScore() {
        return hScore;
    }

    void sethScore(int hScore) {
        this.hScore = hScore;

    }

    State getParent() {
        return parent;
    }

    void setParent(State parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        State state = (State) o;

        return Arrays.deepEquals(puzzle, state.puzzle);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(puzzle);
    }

    @NotNull
    @Override
    public String toString() {
        return
                printArray(puzzle)
                        +
                        ", gScore=" + getgScore() +
                        ", hScore=" + gethScore() +
                        ", fScore=" + getfScore() +
                        ", emptyCoordinates=" + Arrays.toString(emptyCoordinates) +
                        '}';
    }

    private String printArray(@NotNull int[][] array) {
        StringBuilder sb = new StringBuilder();
        for (int[] row :
                array) {
            sb.append(Arrays.toString(row));
            sb.append("\n");
        }
        return sb.toString();
    }
}
