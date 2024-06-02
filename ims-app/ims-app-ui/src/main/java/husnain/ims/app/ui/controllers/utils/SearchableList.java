package husnain.ims.app.ui.controllers.utils;

import java.util.function.Function;
import javafx.collections.ObservableList;

/**
 * FUTURE ENHANCEMENT: none.
 *
 * @author Husnain Arif
 */
public class SearchableList<T> {

    private final ObservableList<T> unfiltered;
    private final Function<ObservableList<T>, ObservableList<T>> identity;
    private final Function<String, ObservableList<T>> search;

    public SearchableList(ObservableList<T> unfiltered, Function<String, ObservableList<T>> search) {
        this.unfiltered = unfiltered;
        this.identity = Function.identity();
        this.search = search;
    }

    public ObservableList<T> getFiltered(String query) {
        return new StringCheck(query).isNullOrBlank() ? identity.apply(unfiltered) : search.apply(query);
    }
}
