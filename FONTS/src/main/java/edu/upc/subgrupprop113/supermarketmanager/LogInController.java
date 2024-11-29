package edu.upc.subgrupprop113.supermarketmanager;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LogInController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private DomainController domainController = DomainController.getInstance(); // Instancia del controlador de dominio.

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            domainController.logIn(username, password);
            System.out.println("Logged in as " + username);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
            //errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    private ImageView powerOffImage;

    @FXML
    private void handlePowerOff() {
        System.out.println("Powering off...");
        System.exit(0);
    }
}