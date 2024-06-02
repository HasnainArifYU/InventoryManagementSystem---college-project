package husnain.ims.app.ui.controllers.utils;

import java.util.Objects;
import javafx.scene.control.TableCell;

/**
 * FUTURE ENHANCEMENT: none.
 *
 * @author Husnain Arif
 */
public class FormattedPriceCell<T> extends TableCell<T, Double> {

    @Override
    protected void updateItem(Double price, boolean empty) {
        super.updateItem(price, empty);
        super.setText(Objects.isNull(price) || empty ? null : String.format("%.2f", price));
    }

}
