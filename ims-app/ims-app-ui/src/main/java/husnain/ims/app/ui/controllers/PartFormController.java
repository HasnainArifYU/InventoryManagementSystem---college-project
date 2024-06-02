package husnain.ims.app.ui.controllers;

import husnain.ims.app.crud.utils.IdSequence;
import husnain.ims.app.model.InHouse;
import husnain.ims.app.model.OutSourced;
import husnain.ims.app.model.Part;
import husnain.ims.app.ui.controllers.utils.InputError;
import husnain.ims.app.ui.controllers.utils.Named;
import husnain.ims.app.ui.controllers.utils.RegexCheck;
import husnain.ims.app.ui.controllers.utils.StockLevel;
import husnain.ims.app.ui.controllers.utils.StringCheck;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleGroup;

/**
 * FUTURE ENHANCEMENT: none.
 * <p>
 * FXML Controller class for the {@code PartForm.fxml} file.
 *
 * @author Husnain Arif
 */
public class PartFormController {

    private static final Logger LOG = Logger.getLogger(PartFormController.class.getName());
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
    private static final InputError BLANK_MACHINE_ID_OR_COMPANY_NAME_ERR = new InputError("Invalid machine id or company name", NOT_BLANK);
    private static final InputError INVALID_MACHINE_ID_ERR = new InputError("Invalid machine id", BE_A_NUMBER);
    private static final String INPUT_ERROR_CLASS = "input-error";
    private static final String DOUBLES_REGEX = "\\d+|\\d+\\.\\d+";
    private static final String INTEGERS_REGEX = "\\d+";
    private final Named.DialogType type;
    private final Part part;
    private final ObservableSet<InputError> inputErrors;
    @FXML
    private Label titleLabel;
    @FXML
    private RadioButton inhouseRadioButton;
    @FXML
    private RadioButton outsourcedRadioButton;
    @FXML
    private ToggleGroup productTypeToggleGrp;
    @FXML
    private TextField idTextField;
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
    private Label companyOrMachineIdLabel;
    @FXML
    private TextField nameOrMachineIdTextField;
    @FXML
    private Label errorLabel;

    PartFormController() {
        this(Named.DialogType.ADD, null);
    }

    PartFormController(Part part) {
        this(Named.DialogType.MODIFY, Objects.requireNonNull(part, "Part to modify should not be null"));
    }

    private PartFormController(Named.DialogType type, Part part) {
        this.type = type;
        this.part = part;
        this.inputErrors = FXCollections.observableSet(new LinkedHashSet<>());
    }

    Part getPart() {
        Part modifiedPart;
        var name = nameTextField.getText();
        var price = Double.parseDouble(priceTextField.getText());
        var stock = Integer.parseInt(stockTextField.getText());
        var minStock = Integer.parseInt(minStockTextField.getText());
        var maxStock = Integer.parseInt(maxStockTextField.getText());

        if (Objects.isNull(part)) {
            var id = IdSequence.getInstance().next();

            if (inhouseRadioButton.isSelected()) {
                var inhouse = new InHouse(id, name, price, stock, minStock, maxStock);
                var machineId = Integer.parseInt(nameOrMachineIdTextField.getText());

                inhouse.setMachineId(machineId);

                modifiedPart = inhouse;
            } else {
                var outSourced = new OutSourced(id, name, price, stock, minStock, maxStock);

                outSourced.setCompanyName(nameOrMachineIdTextField.getText());

                modifiedPart = outSourced;
            }
        } else {
            if ((part instanceof InHouse) && outsourcedRadioButton.isSelected()) {
                var outSourced = new OutSourced(part.getId(), name, price, stock, minStock, maxStock);

                outSourced.setCompanyName(nameOrMachineIdTextField.getText());

                modifiedPart = outSourced;
            } else if ((part instanceof OutSourced) && inhouseRadioButton.isSelected()) {
                var inhouse = new InHouse(part.getId(), name, price, stock, minStock, maxStock);
                var machineId = Integer.parseInt(nameOrMachineIdTextField.getText());

                inhouse.setMachineId(machineId);

                modifiedPart = inhouse;
            } else {
                part.setName(name);
                part.setStock(stock);
                part.setPrice(price);
                part.setMax(maxStock);
                part.setMin(minStock);

                if (part instanceof InHouse inHouse) {
                    inHouse.setMachineId(Integer.parseInt(nameOrMachineIdTextField.getText()));
                } else {
                    ((OutSourced) part).setCompanyName(nameOrMachineIdTextField.getText());
                }

                modifiedPart = part;
            }
        }

        return modifiedPart;
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

    @FXML
    void initialize() {
        this.initTitle(type.toString());
        this.initFocus();

        companyOrMachineIdLabel.textProperty().bind(this.createFieldForProductType());

        if (Objects.isNull(part)) {
            inhouseRadioButton.setSelected(true);
            idTextField.setText("Auto Gen - Disabled");
        } else {
            var inhousePart = part instanceof InHouse;

            productTypeToggleGrp.selectToggle(inhousePart ? inhouseRadioButton : outsourcedRadioButton);

            idTextField.setText(Integer.toString(part.getId()));
            nameTextField.setText(part.getName());
            stockTextField.setText(Integer.toString(part.getStock()));
            priceTextField.setText(Double.toString(part.getPrice()));
            maxStockTextField.setText(Integer.toString(part.getMax()));
            minStockTextField.setText(Integer.toString(part.getMin()));
            nameOrMachineIdTextField.setText(
                    inhousePart
                            ? Integer.toString(((InHouse) part).getMachineId())
                            : ((OutSourced) part).getCompanyName()
            );
        }
    }

    private void initTitle(String typeName) {
        titleLabel.setText(String.format("%s Part", typeName));
    }

    private void initFocus() {
        nameTextField.skinProperty().addListener(o -> nameTextField.requestFocus());
    }

    private StringBinding createFieldForProductType() {
        return Bindings.createStringBinding(
                () -> {
                    var inhouseSelected = Objects.equals(
                            inhouseRadioButton,
                            productTypeToggleGrp.getSelectedToggle()
                    );
                    return inhouseSelected ? "Machine ID" : "Company Name";
                },
                productTypeToggleGrp.selectedToggleProperty()
        );
    }

    private void updateErrors() {
        var name = nameTextField.getText();
        var stock = stockTextField.getText();
        var price = priceTextField.getText();
        var min = minStockTextField.getText();
        var max = maxStockTextField.getText();
        var companyNameOrMachineId = nameOrMachineIdTextField.getText();

        var blankName = new StringCheck(name).isNullOrBlank();
        var blankStock = new StringCheck(stock).isNullOrBlank();
        var nonNumberStock = new RegexCheck(stock, INTEGERS_REGEX).doesntMatch();
        var blankPrice = new StringCheck(price).isNullOrBlank();
        var nonNumberPrice = new RegexCheck(price, DOUBLES_REGEX).doesntMatch();
        var blankMaxStock = new StringCheck(max).isNullOrBlank();
        var nonNumberMaxStock = new RegexCheck(max, INTEGERS_REGEX).doesntMatch();
        var blankMinStock = new StringCheck(min).isNullOrBlank();
        var nonNumberMinStock = new RegexCheck(min, INTEGERS_REGEX).doesntMatch();
        var blankCompanyNameOrMachineId = new StringCheck(companyNameOrMachineId).isNullOrBlank();
        var nonNumberMachineId = new RegexCheck(companyNameOrMachineId, INTEGERS_REGEX).doesntMatch();

        this.updateError(nameTextField, blankName, BLANK_NAME_ERR);
        this.updateError(stockTextField, blankStock, BLANK_STOCK_ERR);
        this.updateError(stockTextField, nonNumberStock, INVALID_STOCK_ERR);
        this.updateError(priceTextField, blankPrice, BLANK_PRICE_ERR);
        this.updateError(priceTextField, nonNumberPrice, INVALID_PRICE_ERR);
        this.updateError(maxStockTextField, blankMaxStock, BLANK_MAX_STOCK_ERR);
        this.updateError(maxStockTextField, nonNumberMaxStock, INVALID_MAX_STOCK_ERR);
        this.updateError(minStockTextField, blankMinStock, BLANK_MIN_STOCK_ERR);
        this.updateError(minStockTextField, nonNumberMinStock, INVALID_MIN_STOCK_ERR);

        if (inhouseRadioButton.isSelected()) {
            this.updateError(
                    nameOrMachineIdTextField,
                    blankCompanyNameOrMachineId,
                    BLANK_MACHINE_ID_OR_COMPANY_NAME_ERR
            );
            this.updateError(
                    nameOrMachineIdTextField,
                    nonNumberMachineId,
                    INVALID_MACHINE_ID_ERR
            );
        } else {
            this.updateError(
                    nameOrMachineIdTextField,
                    false,
                    INVALID_MACHINE_ID_ERR
            );
            this.updateError(
                    nameOrMachineIdTextField,
                    blankCompanyNameOrMachineId,
                    BLANK_MACHINE_ID_OR_COMPANY_NAME_ERR
            );
        }

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
