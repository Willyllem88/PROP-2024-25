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
    private ToastLabelController toastLabelController;

    @FXML
    private SetTemperatureController setTemperatureController;

    private ShelvingUnitEditionController shelvingUnitEditionController;

    private int shelvingUnitPosition;

    private TopBarController topBarController;

    private final DomainController domainController = DomainControllerFactory.getInstance().getDomainController();

    private PresentationController presentationController;

    public EditShelvingUnitController(PresentationController presentationController, int shelvingUnitPosition) {
        this.presentationController = presentationController;
        this.shelvingUnitPosition = shelvingUnitPosition;
    }

    @FXML
    private void initialize() {
        topBarController = (TopBarController) topBar.getProperties().get("controller");

        if (topBarController != null)  {
            topBarController.setOnGoBackHandler(_ -> GoBackHandler());
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

        updateShelvingUnit();

        // Set temperature
        String temperature = domainController.getShelvingUnit(shelvingUnitPosition).getTemperature();
        System.out.println("Temperature: " + temperature);
        setTemperatureController.setTemperature(temperature);
    }

    private void handleEmptySU() {
        domainController.emptyShelvingUnit(shelvingUnitPosition);
        loadSingleShelvingUnitEdit(shelvingUnitPosition);
    }

    private void handleEraseSU() {
        try {
            domainController.removeShelvingUnit(shelvingUnitPosition);
            presentationController.shelvingUnitDeleted();
        } catch (Exception e) {
            toastLabelController.setErrorMsg("Error: " + e.getMessage(), 10000); // 10 seconds
        }
    }

    private void updateShelvingUnit() {
        loadSingleShelvingUnitEdit(shelvingUnitPosition);
    }

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

    @FXML
    private void handleConfirmTemperature(MouseEvent mouseEvent) {
        try {
            domainController.modifyShelvingUnitType(shelvingUnitPosition, setTemperatureController.getTemperature());
            updateShelvingUnit();
        } catch (Exception e) {
            toastLabelController.setErrorMsg("Error: " + e.getMessage(), 10000); // 10 seconds
        }
    }

    @FXML
    private void handleCancelTemperature(MouseEvent mouseEvent) {
        // Set temperature as it was before
        setTemperatureController.setTemperature(domainController.getShelvingUnit(shelvingUnitPosition).getTemperature());
    }

    private void GoBackHandler() {
        presentationController.goBackESU();
    }
}
