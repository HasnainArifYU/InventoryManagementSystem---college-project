package husnain.ims.app.ui.controllers.utils;

/**
 * FUTURE ENHANCEMENT: none.
 *
 * @author Husnain Arif
 */
public class StockLevel {

    private final int stock;
    private final int min;
    private final int max;

    public StockLevel(int stock, int min, int max) {
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    public boolean stockIsOutOfRange() {
        return !(max > stock && stock > min);
    }

    public boolean minExceedsMaxStock() {
        return min > max;
    }

}
