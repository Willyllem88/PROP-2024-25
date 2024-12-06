package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.controllers.components.TopBarController;
import edu.upc.subgrupprop113.supermarketmanager.factories.DomainControllerFactory;
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

    private final DomainController domainController = DomainControllerFactory.getInstance().getDomainController(); // Instancia del controlador de dominio.

    @FXML
    private HBox topBar;

    private PresentationController presentationController;

    public LogInController(PresentationController presentationController) {
        this.presentationController = presentationController;
    }

    @FXML
    public void initialize() {

        TopBarController topBarController = (TopBarController) topBar.getProperties().get("controller");
        topBarController.setPresentationController(this.presentationController);

        topBarController.showGoBackButton(false);
        topBarController.showNewDistributionButton(false);
        topBarController.showSaveButton(false);
        topBarController.showSaveAsButton(false);
        topBarController.showCatalogButton(true);

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