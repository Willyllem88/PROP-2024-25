package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.controllers.components.TopBarController;
import edu.upc.subgrupprop113.supermarketmanager.factories.DomainControllerFactory;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

public class MainScreenController {

    private PresentationController presentationController;
    private final DomainController domainController;

    @FXML
    private HBox topBar;

    private TopBarController topBarController = new TopBarController(); // Instancia del controlador de la barra superior.

    public MainScreenController(PresentationController presentationController) {
        this.presentationController = presentationController;
        this.domainController = DomainControllerFactory.getInstance().getDomainController();
    }

    @FXML
    private void initialize() {
        topBarController = (TopBarController) topBar.getProperties().get("controller");

        if (topBarController != null) {
            topBarController.showGoBackButton(false);
            topBarController.setOnGoBackHandler(_ -> System.out.println("Custom Go Back Handler"));
        }
    }
}
