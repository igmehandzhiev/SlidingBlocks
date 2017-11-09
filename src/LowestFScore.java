import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/**
 * @author <a href = "mailto:imehandzhiev@intracol.com">imehandzhiev</a>
 * @since 08-Nov-17
 */
class LowestFScore implements Comparator<State> {

    @Override
    public int compare(@NotNull State o1, @NotNull State o2) {
        return Integer.compare(o1.getfScore(), o2.getfScore());
    }
}
