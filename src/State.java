import java.util.Arrays;

/**
 * @author <a href = "mailto:imehandzhiev@intracol.com">imehandzhiev</a>
 * @since 06-Nov-17
 */
public class State {
    int[][] puzzle;

    int gScore;
    int hScore;
    int fScore = gScore + hScore;

    public int[] getEmptyCoordinates() {
        return emptyCoordinates;
    }

    public void setEmptyCoordinates(int[] emptyCoordinates) {
        this.emptyCoordinates = emptyCoordinates;
    }

    int[] emptyCoordinates;

    public State(int[][] puzzle) {
        this.setPuzzle(puzzle);
    }

    public State(int[][] puzzle, int gScore, int hScore) {
        this.setPuzzle(puzzle);
        this.setgScore(gScore);
        this.sethScore(hScore);
    }

    public int[][] getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(int[][] puzzle) {
        this.puzzle = puzzle;
    }

    public int getfScore() {
        return fScore;
    }

    public int getgScore() {
        return gScore;
    }

    public void setgScore(int parentGScore) {
        this.gScore = parentGScore;
    }

    public int gethScore() {
        return hScore;
    }

    public void sethScore(int hScore) {
        this.hScore = hScore;

        this.fScore = getgScore() + hScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State state = (State) o;

        return Arrays.deepEquals(puzzle, state.puzzle);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(puzzle);
    }

    @Override
    public String toString() {
        return "State{" +
                "puzzle=" + printArray(puzzle) +
                ", gScore=" + gScore +
                ", hScore=" + hScore +
                ", fScore=" + fScore +
                ", emptyCoordinates=" + Arrays.toString(emptyCoordinates) +
                '}';
    }

    private String printArray(int[][] array) {
        StringBuilder sb = new StringBuilder();
        for (int[] row :
                array) {
            sb.append(Arrays.toString(row));
        }
        return sb.toString();
    }
}
