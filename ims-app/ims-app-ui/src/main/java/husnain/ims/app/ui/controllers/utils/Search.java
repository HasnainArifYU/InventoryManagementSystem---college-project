package husnain.ims.app.ui.controllers.utils;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * FUTURE ENHANCEMENT: none.
 *
 * @author Husnain Arif
 */
public class Search<T> implements Function<String, ObservableList<T>> {

    private static final Logger LOG = Logger.getLogger(Search.class.getName());
    private final Function<String, ObservableList<T>> nameSearch;
    private final Function<Integer, T> idSearch;

    public Search(Function<String, ObservableList<T>> nameSearch, Function<Integer, T> idSearch) {
        this.nameSearch = nameSearch;
        this.idSearch = idSearch;
    }

    @Override
    public ObservableList<T> apply(String query) {
        ObservableList<T> list;

        if (new RegexCheck(query, "\\d+").doesntMatch()) {
            list = nameSearch.apply(query);
        } else {
            var o = (Optional<T>) Optional.empty();

            try {
                o = Optional.ofNullable(idSearch.apply(Integer.parseInt(query)));
            } catch (NumberFormatException | NoSuchElementException exc) {
                LOG.log(Level.WARNING, "An expection occured: {0}", exc.getMessage());
            }

            list = o.map(List::of)
                    .map(FXCollections::observableList)
                    .orElse(FXCollections.emptyObservableList());
        }

        return list;
    }

}
