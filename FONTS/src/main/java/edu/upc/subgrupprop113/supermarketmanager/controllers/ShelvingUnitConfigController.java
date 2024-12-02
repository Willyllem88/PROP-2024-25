package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.controllers.components.TopBarController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

public class ShelvingUnitConfigController {

    @FXML
    private MenuButton menuButton;

    @FXML
    private HBox topBar;

    private final DomainController domainController = DomainController.getInstance();

    private TopBarController topBarController = new TopBarController();

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
