package husnain.ims.app.ui.controllers.utils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Labeled;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * FUTURE ENHANCEMENT: none.
 *
 * @author Husnain Arif
 */
public class SearchListener<T> implements ChangeListener<String> {

    private final SearchableList<T> searchableList;
    private final Labeled resultsLabel;
    private final TableView<T> table;

    public SearchListener(SearchableList<T> searchableList, TextInputControl searchControl, Labeled resultsLabel, TableView<T> table) {
        this.searchableList = searchableList;
        this.resultsLabel = resultsLabel;
        this.table = table;
        //Clear text when Esc key is pressed
        searchControl.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                searchControl.clear();
            }
        });
        // Select all text when this editor is selected
        searchControl.setOnMousePressed(event -> searchControl.selectAll());
        searchControl.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                searchControl.selectAll();
            }
        }));
    }

    @Override
    public void changed(ObservableValue<? extends String> ov, String oldText, String newText) {
        var filtered = searchableList.getFiltered(newText);

        resultsLabel.setText(filtered.isEmpty() ? "No result found for query \"%s\"".formatted(newText) : null);
        table.setItems(filtered);
    }

}
