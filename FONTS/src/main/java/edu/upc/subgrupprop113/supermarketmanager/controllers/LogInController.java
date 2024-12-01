package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.controllers.components.TopBarController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class LogInController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private final DomainController domainController = DomainController.getInstance(); // Instancia del controlador de dominio.

    @FXML
    private HBox topBar;

    private TopBarController topBarController = new TopBarController(); // Instancia del controlador de la barra superior.

    private PresentationController presentationController;

    public LogInController(PresentationController presentationController) {
        this.presentationController = presentationController;
    }

    @FXML
    public void initialize() {

        topBarController = (TopBarController) topBar.getProperties().get("controller");

        if (topBarController != null) {
            // Default visibility
            topBarController.showGoBackButton(false);
            topBarController.showNewDistributionButton(false);
            topBarController.showSaveButton(false);
            topBarController.showSaveAsButton(false);

            topBarController.setOnGoBackHandler(_ -> System.out.println("Custom Go Back Handler"));
        }
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            domainController.logIn(username, password);
            System.out.println("Logged in as " + username);
            presentationController.logInSuccessful();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
        }
    }

}