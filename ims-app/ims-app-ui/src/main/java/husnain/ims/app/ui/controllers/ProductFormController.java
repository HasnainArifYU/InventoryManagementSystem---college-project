package husnain.ims.app.ui.controllers;

import husnain.ims.app.crud.Inventory;
import husnain.ims.app.crud.utils.IdSequence;
import husnain.ims.app.model.Part;
import husnain.ims.app.model.Product;
import husnain.ims.app.ui.controllers.utils.PropertyRatio;
import husnain.ims.app.ui.controllers.utils.BoundablePropertyRatio;
import husnain.ims.app.ui.controllers.utils.FormattedPriceCell;
import husnain.ims.app.ui.controllers.utils.InputError;
import husnain.ims.app.ui.controllers.utils.Named;
import husnain.ims.app.ui.controllers.utils.PlaceholderLabel;
import husnain.ims.app.ui.controllers.utils.RegexCheck;
import husnain.ims.app.ui.controllers.utils.Search;
import husnain.ims.app.ui.controllers.utils.SearchListener;
import husnain.ims.app.ui.controllers.utils.SearchableList;
import husnain.ims.app.ui.controllers.utils.StockLevel;
import husnain.ims.app.ui.controllers.utils.StringCheck;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FUTURE ENHANCEMENT: none.
 * <p>
 * FXML Controller class for the {@code ProductForm.fxml} file.
 *
 * @author Husnain Arif
 */
public class ProductFormController {

    private static final Logger LOG = Logger.getLogger(ProductFormController.class.getName());
    private static final String INPUT_ERROR_CLASS = "input-error";
    private static final String DOUBLES_REGEX = "\\d+|\\d+\\.\\d+";
    private static final String INTEGERS_REGEX = "\\d+";
    private static final String NOT_BLANK = "should not be blank";
    private static final String BE_A_NUMBER = "should be a number";
    private static final InputError BLANK_NAME_ERR = new InputError("Invalid name", NOT_BLANK);
    private static final InputError BLANK_STOCK_ERR = new InputError("Invalid inv", NOT_BLANK);
    private static final InputError INVALID_STOCK_ERR = new InputError("Invalid inv", BE_A_NUMBER);
    private static final InputError OUT_OF_RANGE_STOCK_ERR = new InputError("Invalid inv", "should be greater than min and less than max");
    private static final InputError BLANK_PRICE_ERR = new InputError("Invalid price", NOT_BLANK);
    private static final InputError INVALID_PRICE_ERR = new InputError("Invalid price", BE_A_NUMBER);
    private static final InputError BLANK_MAX_STOCK_ERR = new InputError("Invalid max", NOT_BLANK);
    private static final InputError INVALID_MAX_STOCK_ERR = new InputError("Invalid max", BE_A_NUMBER);
    private static final InputError OUT_OF_RANGE_MAX_STOCK_ERR = new InputError("Invalid max", "should be greater than min");
    private static final InputError BLANK_MIN_STOCK_ERR = new InputError("Invalid min", NOT_BLANK);
    private static final InputError INVALID_MIN_STOCK_ERR = new InputError("Invalid min", BE_A_NUMBER);
    private static final InputError OUT_OF_RANGE_MIN_STOCK_ERR = new InputError("Invalid min", "should be less than max");
    private final Named.DialogType type;
    private final ObservableSet<InputError> inputErrors;
    private final Product product;
    private final ObservableList<Part> associatedParts;
    @FXML
    private Label titleLabel;
    @FXML
    private TextField prodIdTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField stockTextField;
    @FXML
    private TextField priceTextField;
    @FXML
    private TextField maxStockTextField;
    @FXML
    private TextField minStockTextField;
    @FXML
    private Label errorLabel;
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
    private TableView<Part> assocPartsTable;
    @FXML
    private TableColumn<Part, Integer> assocPartIdColumn;
    @FXML
    private TableColumn<Part, String> assocPartNameColumn;
    @FXML
    private TableColumn<Part, Integer> assocInvLevelColumn;
    @FXML
    private TableColumn<Part, Double> assocPriceColumn;

    ProductFormController() {
        this(Named.DialogType.ADD, null);
    }

    ProductFormController(Product product) {
        this(Named.DialogType.MODIFY, Objects.requireNonNull(product, "Product to modify should not be null"));
    }

    private ProductFormController(Named.DialogType type, Product product) {
        this.type = type;
        this.product = product;
        this.inputErrors = FXCollections.observableSet(new LinkedHashSet<>());
        this.associatedParts = FXCollections.observableArrayList();
    }

    Product getProduct() {
        Product modifiedProduct;
        var name = nameTextField.getText();
        var price = Double.parseDouble(priceTextField.getText());
        var stock = Integer.parseInt(stockTextField.getText());
        var minStock = Integer.parseInt(minStockTextField.getText());
        var maxStock = Integer.parseInt(maxStockTextField.getText());

        if (Objects.isNull(product)) {
            var id = IdSequence.getInstance().next();

            modifiedProduct = new Product(id, name, price, stock, minStock, maxStock);
            //Add the parts that we're chosen in this form
            modifiedProduct.getAllAssociatedParts().setAll(associatedParts);
        } else {
            product.setName(name);
            product.setStock(stock);
            product.setPrice(price);
            product.setMax(maxStock);
            product.setMin(minStock);

            //Add the parts that we're chosen in this form
            product.getAllAssociatedParts().setAll(associatedParts);

            modifiedProduct = product;
        }

        return modifiedProduct;
    }

    Set<InputError> getInputErrors() {
        this.updateErrors();

        return Collections.unmodifiableSet(inputErrors);
    }

    void updateErrorText() {
        var ls = System.lineSeparator();
        var text = inputErrors.stream()
                .map(InputError::toString)
                .collect(Collectors.joining(ls, String.format("%d Error(s)%s", inputErrors.size(), ls), ls));

        errorLabel.setText(inputErrors.isEmpty() ? null : text);
    }

    /**
     * Initializes the controller class.
     */
    @FXML
    void initialize() {
        this.initTitle(type.toString());
        this.initFocus();
        this.initTablePlaceholders();
        this.setupColumnWidths();

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
        ));

        assocPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocInvLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        assocPriceColumn.setCellFactory(callBck -> new FormattedPriceCell());

        assocPartsTable.setItems(associatedParts);

        if (Objects.isNull(product)) {
            prodIdTextField.setText("Auto Gen - Disabled");
        } else {
            prodIdTextField.setText(Integer.toString(product.getId()));
            nameTextField.setText(product.getName());
            stockTextField.setText(Integer.toString(product.getStock()));
            priceTextField.setText(Double.toString(product.getPrice()));
            maxStockTextField.setText(Integer.toString(product.getMax()));
            minStockTextField.setText(Integer.toString(product.getMin()));
            associatedParts.setAll(product.getAllAssociatedParts());
        }
    }

    @FXML
    void addAssociatedPart(ActionEvent event) {
        var selectedPart = partsTable.getSelectionModel().getSelectedItem();

        if (Objects.isNull(selectedPart)) {
            var alert = new Alert(Alert.AlertType.ERROR, null);

            alert.setHeaderText("Can't add. No part is selected.");
            alert.show();
        } else {
            associatedParts.add(selectedPart);
        }

        event.consume();
    }

    @FXML
    void removeAssociatedPart(ActionEvent event) {
        var selectedPart = assocPartsTable.getSelectionModel().getSelectedItem();

        if (Objects.isNull(selectedPart)) {
            var alert = new Alert(Alert.AlertType.ERROR, null);

            alert.setHeaderText("Can't remove. No associated part is selected.");
            alert.show();
        } else {
            var yesBtn = new ButtonType("Yes", ButtonBar.ButtonData.NO);
            var noBtn = new ButtonType("No", ButtonBar.ButtonData.OK_DONE);

            var alert = new Alert(Alert.AlertType.CONFIRMATION, null, yesBtn, noBtn);

            alert.setTitle("Remove");
            alert.setHeaderText(String.format("Removing the part \"%s\" will dissociate it with the product. Continue?", selectedPart.getName()));

            alert.showAndWait()
                    .filter(btn -> Objects.equals(btn, yesBtn))
                    .ifPresent(btn -> associatedParts.remove(selectedPart));
        }

        event.consume();
    }

    private void initTitle(String typeName) {
        titleLabel.setText(String.format("%s Product", typeName));
    }

    private void initFocus() {
        nameTextField.skinProperty().addListener(o -> nameTextField.requestFocus());
    }

    private void initTablePlaceholders() {
        partsTable.setPlaceholder(new PlaceholderLabel("<No parts available>"));
        assocPartsTable.setPlaceholder(new PlaceholderLabel("<No associated parts available>"));
    }

    private void setupColumnWidths() {
        partsTable.widthProperty().addListener(
                new BoundablePropertyRatio(
                        partsTable.widthProperty(),
                        List.of(new PropertyRatio(partIdColumn.maxWidthProperty(), 0.11),
                                new PropertyRatio(partNameColumn.maxWidthProperty(), 0.38),
                                new PropertyRatio(partInvLevelColumn.maxWidthProperty(), 0.22),
                                new PropertyRatio(partPriceColumn.maxWidthProperty(), 0.26)
                        )
                )
        );

        assocPartsTable.widthProperty().addListener(
                new BoundablePropertyRatio(
                        assocPartsTable.widthProperty(),
                        List.of(new PropertyRatio(assocPartIdColumn.maxWidthProperty(), 0.11),
                                new PropertyRatio(assocPartNameColumn.maxWidthProperty(), 0.38),
                                new PropertyRatio(assocInvLevelColumn.maxWidthProperty(), 0.22),
                                new PropertyRatio(assocPriceColumn.maxWidthProperty(), 0.26)
                        )
                )
        );
    }

    private void updateErrors() {
        var name = nameTextField.getText();
        var stock = stockTextField.getText();
        var price = priceTextField.getText();
        var min = minStockTextField.getText();
        var max = maxStockTextField.getText();

        var blankName = new StringCheck(name).isNullOrBlank();
        var blankStock = new StringCheck(stock).isNullOrBlank();
        var nonNumberStock = new RegexCheck(stock, INTEGERS_REGEX).doesntMatch();
        var blankPrice = new StringCheck(price).isNullOrBlank();
        var nonNumberPrice = new RegexCheck(price, DOUBLES_REGEX).doesntMatch();
        var blankMaxStock = new StringCheck(max).isNullOrBlank();
        var nonNumberMaxStock = new RegexCheck(max, INTEGERS_REGEX).doesntMatch();
        var blankMinStock = new StringCheck(min).isNullOrBlank();
        var nonNumberMinStock = new RegexCheck(min, INTEGERS_REGEX).doesntMatch();

        this.updateError(nameTextField, blankName, BLANK_NAME_ERR);
        this.updateError(stockTextField, blankStock, BLANK_STOCK_ERR);
        this.updateError(stockTextField, nonNumberStock, INVALID_STOCK_ERR);
        this.updateError(priceTextField, blankPrice, BLANK_PRICE_ERR);
        this.updateError(priceTextField, nonNumberPrice, INVALID_PRICE_ERR);
        this.updateError(maxStockTextField, blankMaxStock, BLANK_MAX_STOCK_ERR);
        this.updateError(maxStockTextField, nonNumberMaxStock, INVALID_MAX_STOCK_ERR);
        this.updateError(minStockTextField, blankMinStock, BLANK_MIN_STOCK_ERR);
        this.updateError(minStockTextField, nonNumberMinStock, INVALID_MIN_STOCK_ERR);

        try {
            this.updateError(stockTextField,
                    new StockLevel(
                            Integer.parseInt(stock),
                            Integer.parseInt(min),
                            Integer.parseInt(max)
                    ).stockIsOutOfRange(),
                    OUT_OF_RANGE_STOCK_ERR
            );
            this.updateError(maxStockTextField,
                    new StockLevel(
                            Integer.parseInt(stock),
                            Integer.parseInt(min),
                            Integer.parseInt(max)
                    ).minExceedsMaxStock(),
                    OUT_OF_RANGE_MAX_STOCK_ERR
            );
            this.updateError(minStockTextField,
                    new StockLevel(
                            Integer.parseInt(stock),
                            Integer.parseInt(min),
                            Integer.parseInt(max)
                    ).minExceedsMaxStock(),
                    OUT_OF_RANGE_MIN_STOCK_ERR
            );
        } catch (NumberFormatException exc) {
            LOG.log(Level.FINE, null, exc);
        }
    }

    private void updateError(TextInputControl textInput, boolean invalid, InputError error) {
        var styleClass = textInput.getStyleClass();

        if (invalid) {
            inputErrors.add(error);
            if (!styleClass.contains(INPUT_ERROR_CLASS)) {
                styleClass.add(INPUT_ERROR_CLASS);
            }
        } else {
            inputErrors.remove(error);
            styleClass.setAll(styleClass.stream().filter(sc -> !Objects.equals(sc, INPUT_ERROR_CLASS)).toArray(String[]::new));
        }
    }

}
