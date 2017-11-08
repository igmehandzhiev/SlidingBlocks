import java.util.Comparator;

/**
 * @author <a href = "mailto:imehandzhiev@intracol.com">imehandzhiev</a>
 * @since 08-Nov-17
 */
public class LowestFScore implements Comparator<State> {

    @Override
    public int compare(State o1, State o2) {
        return Integer.compare(o1.getfScore(), o2.getfScore());
    }
}
