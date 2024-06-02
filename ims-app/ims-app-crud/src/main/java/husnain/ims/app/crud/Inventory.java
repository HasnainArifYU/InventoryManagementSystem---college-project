package husnain.ims.app.crud;

import husnain.ims.app.model.Part;
import husnain.ims.app.model.Product;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * FUTURE ENHANCEMENT: This class should use a DBMS implementation -- it uses instances of
 * {@link ObservableList} instead.
 * <p>
 * Utility class that offers create, read, update, and delete (CRUD) routines on
 * {@link Part} and {@link Product} caches.
 *
 * @author Husnain Arif
 */
public class Inventory {

    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Attaches a {@link Part} object to an {@link ObservableList} of
     * {@code Part} objects.
     * <p>
     * Assumes, without verification, that the supplied {@code Part} is non
     * null.
     *
     * @param part a {@code Part} instance to append to the cache of
     *             {@code Part} objects.
     */
    public static void addPart(Part part) {
        allParts.add(part);
    }

    /**
     * Attaches a {@link Product} object to an {@link ObservableList} of
     * {@code Product} objects.
     * <p>
     * Assumes, without verification, that the supplied {@code Product} is non
     * null.
     *
     * @param newProduct a {@code Product} instance to append to the cache of
     *                   {@code Product} objects.
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * Searches the cache of {@code Part} objects for a {@code Part} instance
     * with the specified {@code id}.
     * <p>
     * If it finds it, it returns it -- otherwise, it throws a
     * {@link NoSuchElementException}.
     *
     * @param partId the {@code int} value representing the {@link Part#id} of a
     *               {@code Part} that may be held in the cache of {@code Part}
     *               objects.
     *
     * @return a {@code Part} instance, held in cache, with an {@link Part#id}
     *         matching the one supplied.
     *
     * @throws NoSuchElementException if no {@code Part} with the supplied
     *                                {@code id} was found.
     */
    public static Part lookupPart(int partId) {
        return allParts.stream().filter(part -> Objects.equals(part.getId(), partId)).findFirst().orElseThrow();
    }

    /**
     * Searches the cache of {@code Product} objects for a {@code Product}
     * instance with the specified {@code id}.
     * <p>
     * If it finds it, it returns it -- otherwise, it throws a
     * {@link NoSuchElementException}.
     *
     * @param productId the {@code int} value representing the
     *                  {@link Product#id} of a {@code Product} that may be held
     *                  in the cache of {@code Product} objects.
     *
     * @return a {@code Product} instance, held in cache, with an
     *         {@link Product#id} matching the one supplied.
     *
     * @throws NoSuchElementException if no {@code Product} with the supplied
     *                                {@link Product#id} was found.
     */
    public static Product lookupProduct(int productId) {
        return allProducts.stream().filter(product -> Objects.equals(product.getId(), productId)).findFirst().orElseThrow();
    }

    /**
     * Searches the cache of {@code Part} objects for {@code Part} instances
     * whose names contain the specified {@link Part#name}.
     * <p>
     * This method never returns {@code null}. If the search is unsuccessful, it
     * returns an empty {@link FXCollections#observableArrayList()} that's empty
     * instead.
     *
     * @param partName a {@code String} value that will be checked to see if any
     *                 of the {@code Part} objects in cache contain it as part
     *                 of their names.
     *
     * @return An {@link ObservableList} that contains {@link Part} instances
     *         with names containing the specified {@code String} value. Or, an
     *         empty {@code ObservableList} if no {@code Part} instances in
     *         cache contain the specified {@code String} value.
     */
    public static ObservableList<Part> lookupPart(String partName) {
        return allParts.stream()
                .filter(part -> part.getName().toLowerCase().contains(partName.toLowerCase()))
                .collect(Collectors.collectingAndThen(Collectors.toList(), FXCollections::observableArrayList));
    }

    /**
     * Searches the cache of {@code Product} objects for {@code Product}
     * instances whose names contain the specified {@link Part#name}.
     * <p>
     * This method never returns {@code null}. If the search is unsuccessful, it
     * returns an empty {@link FXCollections#observableArrayList()} that's empty
     * instead.
     *
     * @param productName a {@code String} value that will be checked to see if
     *                    any of the {@code Product} objects in cache contain it
     *                    as part of their names.
     *
     * @return An {@link ObservableList} that contains {@link Product} instances
     *         with names containing the specified {@code String} value. Or, an
     *         empty {@code ObservableList} if no {@code Product} instances in cache
     *         contain the specified {@code String} value.
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        return allProducts.stream()
                .filter(product -> product.getName().toLowerCase().contains(productName.toLowerCase()))
                .collect(Collectors.collectingAndThen(Collectors.toList(), FXCollections::observableArrayList));
    }

    /**
     * This method has the effect of updating a {@code Part} in a specified
     * index in cache with the values from the supplied {@code Part} object.
     * <p>
     * Yet, to achieve this, it simply replaces the {@code Part} instance in
     * cache with the supplied {@code Part} instance.
     *
     * @param index        the location of the {@code Part} instance in cache
     *                     that seeks will be updated with new field values.
     * @param selectedPart the {@code Part} object that will supply the
     *                     specified cache index with an instance of itself.
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * This method has the effect of updating a {@code Product} in a specified
     * index in cache with the values from the supplied {@code Product} object.
     * <p>
     * Yet, to achieve this, it simply replaces the {@code Product} instance in
     * cache with the supplied {@code Product} instance.
     *
     * @param index      the location of the {@code Product} instance in cache
     *                   that will be updated with new field values.
     * @param newProduct the {@code Part} object that will supply the specified
     *                   cache index with an instance of itself.
     */
    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    /**
     * Removes the specified {@code Part} from cache.
     *
     * @param selectedPart the {@code Part} to be removed from cache
     *
     * @return {@code true} if the cache contained the specified {@code Part}.
     */
    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    /**
     * Removes the specified {@code Product} from cache.
     *
     * @param selectedProduct the {@code Product} to be removed from cache
     *
     * @return {@code true} if the cache contained the specified
     *         {@code Product}.
     */
    public static boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    /**
     * Gives access to the cache of {@code Part} instances that's in the form of
     * an {@link ObservableList}.
     *
     * @return the {@link ObservableList} that contains {@code Part} instances.
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Gives access to the cache of {@code Part} instances that's in the form of
     * an {@link ObservableList}.
     *
     * @return the {@link ObservableList} that contains {@code Part} instances.
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
