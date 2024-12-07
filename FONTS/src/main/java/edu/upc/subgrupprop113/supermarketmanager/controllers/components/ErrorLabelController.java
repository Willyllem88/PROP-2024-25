package edu.upc.subgrupprop113.supermarketmanager.controllers.components;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ErrorLabelController extends Label {
    @FXML
    private VBox root;

    @FXML
    private Label errorLabel;

    @FXML
    public void initialize() {
        errorLabel.setVisible(false);
    }

    /**
     * Set the error message to be displayed
     * @param errorMsg The error message to be displayed
     */
    public void setErrorMsg(String errorMsg) {
        errorLabel.setText(errorMsg);
        errorLabel.setVisible(true);
    }

    /**
     * Clear the error message
     */
    public void clearErrorMsg() {
        errorLabel.setVisible(false);
        errorLabel.setText("");
    }
}
