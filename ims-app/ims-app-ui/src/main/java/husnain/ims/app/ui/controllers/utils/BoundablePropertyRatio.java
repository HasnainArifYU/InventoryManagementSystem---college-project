package husnain.ims.app.ui.controllers.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.beans.binding.NumberExpressionBase;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * FUTURE ENHANCEMENT: none.
 *
 * @author Husnain Arif
 */
public class BoundablePropertyRatio implements ChangeListener<Number> {

    private final NumberExpressionBase base;
    private final List<PropertyRatio> pRatios;

    public BoundablePropertyRatio(NumberExpressionBase base, Collection<PropertyRatio> pRatios) {
        this.base = base;
        this.pRatios = new ArrayList<>(pRatios);
    }

    @Override
    public void changed(ObservableValue<? extends Number> obs, Number ov, Number nv) {
        pRatios.forEach(this::bind);
    }

    private void bind(PropertyRatio pr) {
        this.bind(pr.property(), pr.ratio());
    }

    private void bind(Property p, double d) {
        if (p.isBound()) {
            p.unbind();
        }

        p.bind(base.multiply(d));
    }

}
