package husnain.ims.app.model;

/**
 * FUTURE ENHANCEMENT: This class needs equality tests (that is, the
 * implementation of
 * {@link Object#equals(java.lang.Object) equals} and
 * {@link Object#hashCode() hashCode}) so that it can be used in
 * {@code Collection} implementations that depend on equality tests for adding
 * and removing elements.
 * <p>
 * This class represents a product part that's manufactured on site.
 *
 * @author Husnain Arif
 */
public class InHouse extends Part {

    private int machineId;

    /**
     * Creates a new {@code InHouse} instance.
     *
     * @param id    a unique id
     * @param name  the name of the {@link Part in-house part}
     * @param price the price of the in-house part
     * @param stock the number of {@code InHouse} objects in stock
     * @param min   the minimum number of in-house parts allowed in stock
     * @param max   the maximum number of in-house parts allowed in stock
     */
    public InHouse(int id, String name, double price, int stock, int min, int max) {
        super(id, name, price, stock, min, max);
    }

    /**
     * Retrieves the id of the machine that was used to manufacture this
     * {@code InHouse} part.
     *
     * @return the id of the machine that was used to manufacture this
     *         {@code InHouse} part.
     */
    public int getMachineId() {
        return machineId;
    }

    /**
     * Sets a new id of the machine that was used to manufacture this
     * {@code InHouse} part.
     *
     * @param machineId the new id of the machine that was used to manufacture
     *                  this {@code InHouse} part.
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

}
