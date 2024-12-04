package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.controllers.components.ShelvingUnitControllerFake;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import edu.upc.subgrupprop113.supermarketmanager.controllers.components.TopBarController;
import edu.upc.subgrupprop113.supermarketmanager.factories.DomainControllerFactory;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class MainScreenController {

    private final PresentationController presentationController;
    private final DomainController domainController = DomainControllerFactory.getInstance().getDomainController();

    @FXML
    private HBox topBar;

    @FXML
    private HBox shelvingUnitContainer; // Contenedor para los ShelvingUnits.

    private TopBarController topBarController;

    public MainScreenController(PresentationController presentationController) {
        this.presentationController = presentationController;
    }

    @FXML
    private void initialize() {
        // Configurar TopBar
        topBarController = (TopBarController) topBar.getProperties().get("controller");
        if (topBarController != null) {
            topBarController.showGoBackButton(false);
            topBarController.setOnGoBackHandler(_ -> System.out.println("Custom Go Back Handler"));
        }

        // Cargar dinámicamente ShelvingUnits
        loadShelvingUnits();
    }

    private void loadShelvingUnits() {
        for (int i = 0; i < 3; i++) { // Ejemplo: cargar 5 unidades
            addShelvingUnitComponent(i);
        }
    }

    private void addShelvingUnitComponent(int supermarketPosition) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/edu/upc/subgrupprop113/supermarketmanager/fxml/components/shelvingUnit.fxml"));
            loader.setControllerFactory(controllerClass -> {
                if (controllerClass == ShelvingUnitControllerFake.class) {
                    return new ShelvingUnitControllerFake(presentationController, supermarketPosition);
                }
                throw new IllegalArgumentException("Unexpected controller: " + controllerClass);
            });

            VBox shelvingUnit = loader.load(); // Cargar el componente
            shelvingUnitContainer.getChildren().add(shelvingUnit); // Agregarlo al contenedor
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load Shelving Unit Component", e);
        }
    }
}
