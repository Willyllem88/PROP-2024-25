package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.Main;
import edu.upc.subgrupprop113.supermarketmanager.controllers.components.*;
import edu.upc.subgrupprop113.supermarketmanager.factories.DomainControllerFactory;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class EditShelvingUnitController {

    @FXML
    private HBox topBar;

    @FXML
    private VBox shelvingUnitContainer;

    @FXML
    private VBox emptySU;

    @FXML
    private VBox eraseSU;

    @FXML
    private VBox confirmButton;

    @FXML
    private ToastLabelController toastLabelController;

    @FXML
    private SetTemperatureController setTemperatureController;

    private ShelvingUnitEditionController shelvingUnitEditionController;

    private int shelvingUnitPosition;

    private TopBarController topBarController;

    private final DomainController domainController = DomainControllerFactory.getInstance().getDomainController();

    private PresentationController presentationController;

    private List<String> products;

    private String temperature;

    public EditShelvingUnitController(PresentationController presentationController, int shelvingUnitPosition) {
        this.presentationController = presentationController;
        this.shelvingUnitPosition = shelvingUnitPosition;
    }

    @FXML
    private void initialize() {
        prepareGoBack();
        topBarController = (TopBarController) topBar.getProperties().get("controller");

        if (topBarController != null)  {
            topBarController.setOnGoBackHandler(_ -> GoBackHandler());
            topBarController.showNewDistributionButton(false);
            topBarController.showDistributionSettings(false);
            topBarController.showCatalogButton(false);
            topBarController.showImportButton(false);
            topBarController.showNewDistributionButton(false);
        }

        PrimaryButtonController emptySU1 = (PrimaryButtonController) emptySU.getProperties().get("controller");
        if (emptySU1 != null) {
            emptySU1.setLabelText("Empty Shelving Unit");
            emptySU1.setOnClickHandler(_ -> handleEmptySU());
        }

        PrimaryButtonController eraseSU1 = (PrimaryButtonController) eraseSU.getProperties().get("controller");
        if (eraseSU1 != null) {
            eraseSU1.setLabelText("Erase Shelving Unit");
            eraseSU1.setOnClickHandler(_ -> handleEraseSU());
        }

        PrimaryButtonController confirmButton1 = (PrimaryButtonController) confirmButton.getProperties().get("controller");
        if (confirmButton1 != null) {
            confirmButton1.setLabelText("Confirm");
            confirmButton1.setOnClickHandler(_ -> handleConfirm());
        }

        updateShelvingUnit();

        // Set temperature
        String temperature = domainController.getShelvingUnit(shelvingUnitPosition).getTemperature();
        System.out.println("Temperature: " + temperature);
        setTemperatureController.setTemperature(temperature);
    }

    /**
     * Handles the empty shelving unit action.
     */
    private void handleEmptySU() {
        domainController.emptyShelvingUnit(shelvingUnitPosition);
        loadSingleShelvingUnitEdit(shelvingUnitPosition);
    }

    /**
     * Handles the erase shelving unit action.
     * Prompts the user for confirmation before deleting the shelving unit.
     */
    private void handleEraseSU() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete Shelving Unit");
        confirmationAlert.setHeaderText("Are you sure you want to delete this shelving unit?");
        confirmationAlert.setContentText("This action cannot be undone.");

        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmationAlert.getButtonTypes().setAll(yesButton, noButton);

        ButtonType result = confirmationAlert.showAndWait().orElse(noButton);

        if (result == yesButton) {
            domainController.emptyShelvingUnit(shelvingUnitPosition);
            domainController.removeShelvingUnit(shelvingUnitPosition);
            presentationController.shelvingUnitDeleted();
        }
    }

    /**
     * Handles the confirmation action.
     */
    private void handleConfirm() {
        presentationController.goBackESU();
    }

    /**
     * Updates the shelving unit display.
     */
    private void updateShelvingUnit() {
        loadSingleShelvingUnitEdit(shelvingUnitPosition);
    }

    /**
     * Loads a single shelving unit edit component.
     *
     * @param supermarketPosition the position of the shelving unit in the supermarket
     */
    private void loadSingleShelvingUnitEdit(int supermarketPosition) {
        shelvingUnitContainer.getChildren().clear();
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/components/shelvingUnit.fxml"));

            shelvingUnitEditionController = new ShelvingUnitEditionController(presentationController, supermarketPosition);
            loader.setController(shelvingUnitEditionController);

            HBox shelvingUnit = loader.load();
            shelvingUnitContainer.getChildren().add(shelvingUnit);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load Shelving Unit Component", e);
        }
    }

    /**
     * Handles the confirmation temperature action.
     *
     * @param mouseEvent
     */
    @FXML
    private void handleConfirmTemperature(MouseEvent mouseEvent) {
        try {
            domainController.modifyShelvingUnitType(shelvingUnitPosition, setTemperatureController.getTemperature());
            updateShelvingUnit();
        } catch (Exception e) {
            toastLabelController.setErrorMsg("Error: " + e.getMessage(), 10000); // 10 seconds
        }
    }

    /**
     * Handles the cancel temperature action.
     *
     * @param mouseEvent
     */
    @FXML
    private void handleCancelTemperature(MouseEvent mouseEvent) {
        // Set temperature as it was before
        setTemperatureController.setTemperature(domainController.getShelvingUnit(shelvingUnitPosition).getTemperature());
    }

    /**
     * Prepares the go back action.
     * Saves the current state of the shelving unit.
     */
    private void prepareGoBack() {
        products = new ArrayList<>();
        for(int i = 0; i < domainController.getShelvingUnit(shelvingUnitPosition).getProducts().size(); i++) {
            if(domainController.getShelvingUnit(shelvingUnitPosition).getProducts().get(i) != null) products.add(domainController.getShelvingUnit(shelvingUnitPosition).getProducts().get(i).getName());
            else products.add(null);
        }
        temperature = domainController.getShelvingUnit(shelvingUnitPosition).getTemperature();
    }

    /**
     * Handles the go back action.
     * Saves the current state of the shelving unit.
     */
    private void GoBackHandler() {
        domainController.emptyShelvingUnit(shelvingUnitPosition);
        domainController.modifyShelvingUnitType(shelvingUnitPosition, temperature);
        for(int i = 0; i < products.size(); i++) {
            if(products.get(i) != null) domainController.addProductToShelvingUnit(products.get(i), i, shelvingUnitPosition);
        }
        presentationController.goBackESU();
    }
}
