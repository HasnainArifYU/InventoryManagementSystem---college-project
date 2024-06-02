package husnain.ims.app.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * FUTURE ENHANCEMENT: This class needs equality tests (that is, the
 * implementation of
 * {@link Object#equals(java.lang.Object) equals} and
 * {@link Object#hashCode() hashCode}) so that it can be used in
 * {@code Collection} implementations that depend on equality tests for adding
 * and removing elements.
 * <p>
 * This class represents a product that's managed by an inventory system.
 *
 * @author Husnain Arif
 */
public class Product {

    private final ObservableList<Part> associatedParts;
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * Creates a new {@code Product} instance.
     *
     * @param id    a unique id
     * @param name  the name of the product
     * @param price the price of the product
     * @param stock the number of {@code Product} objects in stock
     * @param min   the minimum number of products allowed in stock
     * @param max   the maximum number of products allowed in stock
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
        this.associatedParts = FXCollections.observableArrayList();
    }

    /**
     * Retrieves the product's id
     *
     * @return the product's id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets a new product id
     *
     * @param id the new product id to be set to this instance
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retrieves the product's name
     *
     * @return the product's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets a new product name
     *
     * @param name the new product name to be set to this instance
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the product's price
     *
     * @return the product's price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets a new product price
     *
     * @param price the new product price to be set to this instance
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Retrieves the number of products in stock
     *
     * @return the number of products in stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * Sets the new number of products in stock
     *
     * @param stock the new number of products in stock
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Retrieves the minimum number of products allowed in stock
     *
     * @return the minimum number of products allowed in stock
     */
    public int getMin() {
        return min;
    }

    /**
     * Sets the new minimum number of products allowed in stock
     *
     * @param min the new minimum number of products allowed in stock
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Retrieves the maximum number of products allowed in stock
     *
     * @return the maximum number of products allowed in stock
     */
    public int getMax() {
        return max;
    }

    /**
     * Sets the new minimum number of products allowed in stock
     *
     * @param max the new maximum number of products allowed in stock
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Associates a {@link Part} with this {@code Product} instance.
     *
     * @param part a {@link Part} to be associated with this {@code Product}
     *             instance.
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**
     * Dissociates a {@link Part} with this {@code Product} instance.
     *
     * @param selectedAssociatedPart a {@link Part} to be dissociated with this
     *                               {@code Product} instance.
     *
     * @return {@code true} if this {@code Product} was associated with the
     *         specified {@code Part}.
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        return associatedParts.remove(selectedAssociatedPart);
    }

    /**
     * Retrieves an {@link ObservableList} of all the {@code Part} instances
     * that are associated with this {@code Product} instance.
     *
     * @return an {@link ObservableList} of all the {@code Part} instances that
     *         are associated with this {@code Product} instance.
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }

}
