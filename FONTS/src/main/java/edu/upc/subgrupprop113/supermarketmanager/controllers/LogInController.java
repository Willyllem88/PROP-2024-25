package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.controllers.components.ToastLabelController;
import edu.upc.subgrupprop113.supermarketmanager.controllers.components.PrimaryButtonController;
import edu.upc.subgrupprop113.supermarketmanager.controllers.components.SetTemperatureController;
import edu.upc.subgrupprop113.supermarketmanager.controllers.components.TopBarController;
import edu.upc.subgrupprop113.supermarketmanager.factories.DomainControllerFactory;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LogInController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private HBox topBar;

    @FXML
    private VBox primaryButton;

    @FXML
    private ToastLabelController toastLabelController;

    @FXML
    private SetTemperatureController setTemperatureController;

    private final DomainController domainController = DomainControllerFactory.getInstance().getDomainController(); // Instancia del controlador de dominio.
    private PresentationController presentationController;

    public LogInController(PresentationController presentationController) {
        this.presentationController = presentationController;
    }

    /**
     * Initializes the controller.
     */
    @FXML
    private void initialize() {
        TopBarController topBarController = (TopBarController) topBar.getProperties().get("controller");
        PrimaryButtonController primaryButtonController = (PrimaryButtonController) primaryButton.getProperties().get("controller");

        if (topBarController != null) {
            // Default visibility
            topBarController.showGoBackButton(false);
            topBarController.showNewDistributionButton(false);
            topBarController.showSaveButton(false);
            topBarController.showImportButton(false);
            topBarController.showSaveAsButton(false);
            topBarController.showDistributionSettings(false);
            topBarController.showCatalogButton(false);
        }

        if (primaryButtonController != null) {
            primaryButtonController.setLabelText("Log In");
            primaryButtonController.setOnClickHandler(_ -> handleLogin());
        }

        restrictTextField(usernameField, "[a-zA-Z0-9]*");
        restrictTextField(passwordField, "[a-zA-Z0-9]*");
    }

    /**
     * Handles the login button click event.
     */
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
            toastLabelController.setErrorMsg(e.getMessage(), 4500);
        }
    }

    /**
     * Restricts the text field to the specified regular expression.
     *
     * @param textField the text field to restrict.
     * @param regex     the regular expression to use for validation.
     */
    private void restrictTextField(TextField textField, String regex) {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            // Allow only numbers and a single decimal point
            if (newText.matches(regex)) {
                return change;
            }
            return null; // Reject the change
        });
        textField.setTextFormatter(formatter);
    }

}