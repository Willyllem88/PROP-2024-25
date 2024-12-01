package edu.upc.subgrupprop113.supermarketmanager.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.kordamp.ikonli.javafx.FontIcon;

public class LogInController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button powerOffButton; // Reference to the power-off button

    @FXML
    private Label errorLabel;

    private DomainController domainController = DomainController.getInstance(); // Instancia del controlador de dominio.

    private PresentationController presentationController;

    public LogInController(PresentationController presentationController) {
        this.presentationController = presentationController;
    }

    @FXML
    public void initialize() {
        // Add an Ikonli icon to the power-off button
        FontIcon powerOffIcon = new FontIcon("fth-power"); // Feather icon 'power'
        powerOffIcon.setIconSize(30);
        powerOffButton.setGraphic(powerOffIcon); // Set the icon as the button's graphic
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

    @FXML
    private void handlePowerOff() {
        System.out.println("Powering off...");
        System.exit(0);
    }
}