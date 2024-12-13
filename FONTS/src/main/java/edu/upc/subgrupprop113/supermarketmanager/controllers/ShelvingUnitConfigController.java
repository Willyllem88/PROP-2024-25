package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.Main;
import edu.upc.subgrupprop113.supermarketmanager.controllers.components.*;
import edu.upc.subgrupprop113.supermarketmanager.factories.DomainControllerFactory;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ShelvingUnitConfigController {

    @FXML
    private MenuButton menuButton;

    @FXML
    private HBox topBar;

    @FXML
    private HBox shelvingUnitContainer;

    @FXML
    private VBox emptySU;

    @FXML
    private VBox eraseSU;

    @FXML
    private ToastLabelController toastLabelController;

    @FXML
    private SetTemperatureController setTemperatureController;

    //TODO: must not be hardcoded
    private int shelvingUnitPosition = 0;

    private final DomainController domainController = DomainControllerFactory.getInstance().getDomainController();

    private TopBarController topBarController;

    private PresentationController presentationController;

    public ShelvingUnitConfigController(PresentationController presentationController) {
        this.presentationController = presentationController;
    }

    @FXML
    private void initialize() {
        topBarController = (TopBarController) topBar.getProperties().get("controller");

        if (topBarController != null)  {
            topBarController.setOnGoBackHandler(_ -> System.out.println("Custom Go Back Handler"));
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

    private void handleModifySUType() {
        try {
            domainController.modifyShelvingUnitType(shelvingUnitPosition, setTemperatureController.getTemperature());
            updateShelvingUnit();
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

            loader.setController(new ShelvingUnitEditController(presentationController, supermarketPosition));

            HBox shelvingUnit = loader.load();
            shelvingUnitContainer.getChildren().add(shelvingUnit);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load Shelving Unit Component", e);
        }
    }
}
