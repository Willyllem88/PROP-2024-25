package edu.upc.subgrupprop113.supermarketmanager.controllers.components;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
     * Sets an error message on the associated label and displays it for a specified duration.
     * <p>After the specified duration, the error message is cleared automatically by scheduling a task
     * to hide the label using a {@code ScheduledExecutorService}.</p>
     *
     * @param errorMsg the error message to display. This is set as the text of the error label.
     * @param milliseconds the duration in milliseconds for which the error message will be visible.
     *                      Must be greater than 0.
     * @throws IllegalArgumentException if the specified duration is less than or equal to 0.
     *
     *
     */
    public void setErrorMsg(String errorMsg, int milliseconds) throws IllegalArgumentException {
        if (milliseconds <= 0) throw new IllegalArgumentException("Delay must be greater than 0");

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        setErrorMsg(errorMsg);
        scheduler.schedule(() -> Platform.runLater(this::clearErrorMsg), milliseconds, TimeUnit.MILLISECONDS);
        scheduler.shutdown();
    }




    /**
     * Clear the error message
     */
    public void clearErrorMsg() {
        errorLabel.setVisible(false);
        errorLabel.setText("");
    }
}
