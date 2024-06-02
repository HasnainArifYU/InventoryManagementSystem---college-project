package husnain.ims.app.model;

/**
 * FUTURE ENHANCEMENT: This class needs equality tests (that is, the
 * implementation of
 * {@link Object#equals(java.lang.Object) equals} and
 * {@link Object#hashCode() hashCode}) so that it can be used in
 * {@code Collection} implementations that depend on equality tests for adding
 * and removing elements.
 * <p>
 * This class represents a product part that's outsourced from other companies.
 *
 * @author Husnain Arif
 */
public class OutSourced extends Part {

    private String companyName;

    /**
     * Creates a new {@code OutSourced} instance.
     *
     * @param id    a unique id
     * @param name  the name of the {@link Part outsourced part}
     * @param price the price of the outsourced part
     * @param stock the number of {@code OutSourced} objects in stock
     * @param min   the minimum number of outsourced parts allowed in stock
     * @param max   the maximum number of outsourced parts allowed in stock
     */
    public OutSourced(int id, String name, double price, int stock, int min, int max) {
        super(id, name, price, stock, min, max);
    }

    /**
     * Retrieves the name of the company from where this {@code OutSourced} part
     * was acquired from.
     *
     * @return the name of the company from where this {@code OutSourced} part
     *         was acquired from.
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Sets a new name of a company from where this {@code OutSourced} part was
     * acquired from.
     *
     * @param companyName the name of the company from where this
     *                    {@code OutSourced} part was acquired from.
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}
