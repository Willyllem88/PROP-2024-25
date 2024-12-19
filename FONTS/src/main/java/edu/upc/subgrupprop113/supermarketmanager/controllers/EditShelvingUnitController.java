package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.Main;
import edu.upc.subgrupprop113.supermarketmanager.controllers.components.*;
import edu.upc.subgrupprop113.supermarketmanager.factories.DomainControllerFactory;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

    private void handleConfirm() {
        presentationController.goBackESU();
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

    @FXML
    private void prepareGoBack() {
        products = new ArrayList<>();
        for(int i = 0; i < domainController.getShelvingUnit(shelvingUnitPosition).getProducts().size(); i++) {
            if(domainController.getShelvingUnit(shelvingUnitPosition).getProducts().get(i) != null) products.add(domainController.getShelvingUnit(shelvingUnitPosition).getProducts().get(i).getName());
            else products.add(null);
        }
        temperature = domainController.getShelvingUnit(shelvingUnitPosition).getTemperature();
    }

    private void GoBackHandler() {
        domainController.emptyShelvingUnit(shelvingUnitPosition);
        domainController.modifyShelvingUnitType(shelvingUnitPosition, temperature);
        for(int i = 0; i < products.size(); i++) {
            if(products.get(i) != null) domainController.addProductToShelvingUnit(products.get(i), i, shelvingUnitPosition);
        }
        presentationController.goBackESU();
    }
}
