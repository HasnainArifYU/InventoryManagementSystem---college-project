package husnain.ims.app.ui.controllers.utils;

import javafx.beans.property.Property;

/**
 * FUTURE ENHANCEMENT: none.
 *
 * @author Husnain Arif
 */
public record PropertyRatio<T extends Number>(Property<T> property, Double ratio) {

    public PropertyRatio(Property<T> property, Double ratio) {
        this.property = property;
        this.ratio = ratio;
    }

}
