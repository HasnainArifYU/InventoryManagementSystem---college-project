package husnain.ims.app.ui.controllers;

import husnain.ims.app.ui.controllers.utils.FormattedPriceCell;
import husnain.ims.app.crud.Inventory;
import husnain.ims.app.model.Part;
import husnain.ims.app.model.Product;
import husnain.ims.app.ui.InventoryManagementApp;
import husnain.ims.app.ui.controllers.utils.PropertyRatio;
import husnain.ims.app.ui.controllers.utils.BoundablePropertyRatio;
import husnain.ims.app.ui.controllers.utils.PlaceholderLabel;
import husnain.ims.app.ui.controllers.utils.Search;
import husnain.ims.app.ui.controllers.utils.SearchListener;
import husnain.ims.app.ui.controllers.utils.SearchableList;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FUTURE ENHANCEMENT: none.
 * <p>
 * FXML Controller class for the {@code MainForm.fxml} file.
 *
 * @author Husnain Arif
 */
public class MainFormController {

    private static final Logger LOG = Logger.getLogger(MainFormController.class.getName());
    @FXML
    private TextField searchPartsTextField;
    @FXML
    private Label partsResultsLabel;
    @FXML
    private TableView<Part> partsTable;
    @FXML
    private TableColumn<Part, Integer> partIdColumn;
    @FXML
    private TableColumn<Part, String> partNameColumn;
    @FXML
    private TableColumn<Part, Integer> partInvLevelColumn;
    @FXML
    private TableColumn<Part, Double> partPriceColumn;
    @FXML
    private Button addPartButton;
    @FXML
    private TextField searchProductsTextField;
    @FXML
    private Label productsResultsLabel;
    @FXML
    private TableView<Product> productsTable;
    @FXML
    private TableColumn<Product, Integer> productIdColumn;
    @FXML
    private TableColumn<Product, String> productNameColumn;
    @FXML
    private TableColumn<Product, Integer> productInvLevelColumn;
    @FXML
    private TableColumn<Product, Double> productPriceColumn;

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     */
    @FXML
    void initialize() {
        this.initTablePlaceholders();
        this.setupColumnWidths();
        this.initFocus();

        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        partPriceColumn.setCellFactory(callBck -> new FormattedPriceCell());

        partsTable.setItems(Inventory.getAllParts());

        searchPartsTextField.textProperty().addListener(new SearchListener<>(
                new SearchableList<>(
                        Inventory.getAllParts(),
                        new Search<>(Inventory::lookupPart, Inventory::lookupPart)
                ),
                searchPartsTextField,
                partsResultsLabel,
                partsTable
        )
        );

        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        productPriceColumn.setCellFactory(callBck -> new FormattedPriceCell());

        productsTable.setItems(Inventory.getAllProducts());

        searchProductsTextField.textProperty().addListener(new SearchListener<>(
                new SearchableList<>(
                        Inventory.getAllProducts(),
                        new Search<>(Inventory::lookupProduct, Inventory::lookupProduct)
                ),
                searchProductsTextField,
                productsResultsLabel,
                productsTable
        )
        );

    }

    /**
     * Shows a {@link Dialog} that could possibly create a new {@link Part}
     *
     * @param event the event caused by the pressing of the {@code Add} button.
     */
    @FXML
    void addPart(ActionEvent event) {
        try {
            this.showPartDialog(null);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        event.consume();
    }

    /**
     * Shows a {@link Dialog} that could possibly modify a selected {@link Part}
     *
     * @param event the event caused by the pressing of the {@code Modify}
     *              button.
     */
    @FXML
    void modifyPart(ActionEvent event) {
        var selectedPart = partsTable.getSelectionModel().getSelectedItem();

        if (Objects.isNull(selectedPart)) {
            var alert = new Alert(Alert.AlertType.ERROR, null);

            alert.setHeaderText("Can't modify. No part is selected.");
            alert.show();
        } else {
            try {
                this.showPartDialog(selectedPart);
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }

        event.consume();
    }

    /**
     * Shows a {@link Dialog} that could possibly delete a selected {@link Part}
     *
     * @param event the event caused by the pressing of the {@code Delete}
     *              button.
     */
    @FXML
    void deletePart(ActionEvent event) {
        var selectedPart = partsTable.getSelectionModel().getSelectedItem();

        if (Objects.isNull(selectedPart)) {
            var alert = new Alert(Alert.AlertType.ERROR, null);

            alert.setHeaderText("Can't delete. No part is selected.");
            alert.show();
        } else {
            var yesBtn = new ButtonType("Yes", ButtonBar.ButtonData.NO);
            var noBtn = new ButtonType("No", ButtonBar.ButtonData.OK_DONE);

            var alert = new Alert(Alert.AlertType.CONFIRMATION, null, yesBtn, noBtn);

            alert.setTitle("Delete");
            alert.setHeaderText(String.format("Deleting the part \"%s\" is non-reversible. Continue?", selectedPart.getName()));

            alert.showAndWait()
                    .filter(btn -> Objects.equals(btn, yesBtn))
                    .ifPresent(btn -> Inventory.deletePart(selectedPart));
        }

        event.consume();
    }

    /**
     * Shows a {@link Dialog} that could possibly create a new {@link Product}
     *
     * @param event the event caused by the pressing of the {@code Add} button.
     */
    @FXML
    void addProduct(ActionEvent event) {
        try {
            this.showProductDialog(null);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        event.consume();
    }

    /**
     * Shows a {@link Dialog} that could possibly modify a selected
     * {@link Product}
     *
     * @param event the event caused by the pressing of the {@code Modify}
     *              button.
     */
    @FXML
    void modifyProduct(ActionEvent event) {
        var selectedProduct = productsTable.getSelectionModel().getSelectedItem();

        if (Objects.isNull(selectedProduct)) {
            var alert = new Alert(Alert.AlertType.ERROR, null);

            alert.setHeaderText("Can't modify. No product is selected.");
            alert.show();
        } else {
            try {
                this.showProductDialog(selectedProduct);
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }

        event.consume();
    }

    /**
     * Shows a {@link Dialog} that could possibly delete a selected
     * {@link Product}
     *
     * @param event the event caused by the pressing of the {@code Delete}
     *              button.
     */
    @FXML
    void deleteProduct(ActionEvent event) {
        var selectedProduct = productsTable.getSelectionModel().getSelectedItem();

        if (Objects.isNull(selectedProduct)) {
            var alert = new Alert(Alert.AlertType.ERROR, null);

            alert.setHeaderText("Can't delete. No product is selected.");
            alert.show();
        } else {
            var assocParts = selectedProduct.getAllAssociatedParts();

            if (assocParts.isEmpty()) {
                var yesBtn = new ButtonType("Yes", ButtonBar.ButtonData.NO);
                var noBtn = new ButtonType("No", ButtonBar.ButtonData.OK_DONE);

                var alert = new Alert(Alert.AlertType.CONFIRMATION, null, yesBtn, noBtn);

                alert.setTitle("Delete");
                alert.setHeaderText(String.format("Deleting the product \"%s\" is non-reversible. Continue?", selectedProduct.getName()));

                alert.showAndWait()
                        .filter(btn -> Objects.equals(btn, yesBtn))
                        .ifPresent(btn -> Inventory.deleteProduct(selectedProduct));
            } else {
                var alert = new Alert(Alert.AlertType.WARNING, null);

                alert.setHeaderText("Shouldn't delete. Product has associated %d part(s).".formatted(assocParts.size()));
                alert.show();
            }
        }

        event.consume();
    }

    /**
     * Shows a {@link Dialog} that could possibly delete a selected exit the
     * application
     *
     * @param event the event caused by the pressing of the {@code Exit} button.
     */
    @FXML
    void exitApplication(ActionEvent event) {
        this.showExitDialog(event);
    }

    private void initTablePlaceholders() {
        partsTable.setPlaceholder(new PlaceholderLabel("<No parts available>"));
        productsTable.setPlaceholder(new PlaceholderLabel("<No products available>"));
    }

    private void setupColumnWidths() {
        partsTable.widthProperty().addListener(
                new BoundablePropertyRatio(
                        partsTable.widthProperty(),
                        List.of(new PropertyRatio(partIdColumn.maxWidthProperty(), 0.11),
                                new PropertyRatio(partNameColumn.maxWidthProperty(), 0.38),
                                new PropertyRatio(partInvLevelColumn.maxWidthProperty(), 0.22),
                                new PropertyRatio(partPriceColumn.maxWidthProperty(), 0.25)
                        )
                )
        );

        productsTable.widthProperty().addListener(
                new BoundablePropertyRatio(
                        productsTable.widthProperty(),
                        List.of(new PropertyRatio(productIdColumn.maxWidthProperty(), 0.11),
                                new PropertyRatio(productNameColumn.maxWidthProperty(), 0.38),
                                new PropertyRatio(productInvLevelColumn.maxWidthProperty(), 0.22),
                                new PropertyRatio(productPriceColumn.maxWidthProperty(), 0.25)
                        )
                )
        );
    }

    private void initFocus() {
        addPartButton.skinProperty().addListener(o -> addPartButton.requestFocus());
    }

    private void showExitDialog(ActionEvent event) {
        var alert = new Alert(Alert.AlertType.CONFIRMATION, null, ButtonType.YES, ButtonType.NO);

        alert.setTitle("Exit");
        alert.setHeaderText("Continue managing inventory?");

        alert.showAndWait()
                .filter(response -> Objects.equals(response, ButtonType.NO))
                .map(btnType -> this.doExit(event));
    }

    private Void doExit(ActionEvent event) {
        var source = (Node) event.getSource();
        var stage = (Stage) source.getScene().getWindow();
        //Close the application
        stage.close();
        //Stop further propagation of the action
        event.consume();
        //There's nothing to return
        return null;
    }

    private void showProductDialog(Product selectedProduct) throws IOException {
        var index = Inventory.getAllProducts().indexOf(selectedProduct);
        var add = Objects.isNull(selectedProduct);
        var url = InventoryManagementApp.class.getResource("ProductForm.fxml");
        var loader = new FXMLLoader(url);
        var controller = add ? new ProductFormController() : new ProductFormController(selectedProduct);

        loader.setController(controller);

        var dlg = (DialogPane) loader.load();
        var alert = new Alert(Alert.AlertType.NONE);
        var saveBtn = new ButtonType("Save", ButtonBar.ButtonData.YES);

        dlg.getButtonTypes().setAll(saveBtn, ButtonType.CANCEL);
        alert.setDialogPane(dlg);

        var saveButton = (Button) dlg.lookupButton(saveBtn);

        saveButton.addEventFilter(ActionEvent.ACTION, handler -> {
            if (!controller.getInputErrors().isEmpty()) {
                handler.consume();
                controller.updateErrorText();
            } else {
                if (add) {
                    Inventory.addProduct(controller.getProduct());
                } else {
                    Inventory.updateProduct(index, controller.getProduct());
                }
            }
        });

        alert.show();
    }

    private void showPartDialog(Part selectedPart) throws IOException {
        var index = Inventory.getAllParts().indexOf(selectedPart);
        var add = Objects.isNull(selectedPart);
        var url = InventoryManagementApp.class.getResource("PartForm.fxml");
        var loader = new FXMLLoader(url);
        var controller = add ? new PartFormController() : new PartFormController(selectedPart);

        loader.setController(controller);

        var dlg = (DialogPane) loader.load();
        var alert = new Alert(Alert.AlertType.NONE);
        var saveBtn = new ButtonType("Save", ButtonBar.ButtonData.YES);

        dlg.getButtonTypes().setAll(saveBtn, ButtonType.CANCEL);
        alert.setDialogPane(dlg);

        var saveButton = (Button) dlg.lookupButton(saveBtn);

        saveButton.addEventFilter(ActionEvent.ACTION, handler -> {
            if (!controller.getInputErrors().isEmpty()) {
                handler.consume();
                controller.updateErrorText();
            } else {
                if (add) {
                    Inventory.addPart(controller.getPart());
                } else {
                    Inventory.updatePart(index, controller.getPart());
                }
            }
        });

        alert.show();
    }

}
