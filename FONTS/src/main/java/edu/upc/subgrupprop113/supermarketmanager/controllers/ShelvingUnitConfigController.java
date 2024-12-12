package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.controllers.components.ShelvingUnitController;
import edu.upc.subgrupprop113.supermarketmanager.controllers.components.TopBarController;
import edu.upc.subgrupprop113.supermarketmanager.factories.DomainControllerFactory;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class ShelvingUnitConfigController {

    @FXML
    private MenuButton menuButton;

    @FXML
    private HBox topBar;

    @FXML
    private HBox shelvingUnitContainer;

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

        loadSingleShelvingUnitEdit(1);

    }

    private void loadSingleShelvingUnitEdit(int supermarketPosition) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/edu/upc/subgrupprop113/supermarketmanager/fxml/components/shelvingUnit.fxml"));
            loader.setControllerFactory(controllerClass -> {
                if (controllerClass == ShelvingUnitController.class) {
                    return new ShelvingUnitController(presentationController, 1); // Load only the shelving unit at index 1
                }
                throw new IllegalArgumentException("Unexpected controller: " + controllerClass);
            });

            HBox shelvingUnit = loader.load();
            shelvingUnitContainer.getChildren().add(shelvingUnit);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load Shelving Unit Component", e);
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        // Lógica de logout
        System.out.println("Logging out...");
    }

    @FXML
    private void handleCloseApp(ActionEvent event) {
        // Lógica para cerrar la aplicación
        System.out.println("Closing application...");
    }
}
