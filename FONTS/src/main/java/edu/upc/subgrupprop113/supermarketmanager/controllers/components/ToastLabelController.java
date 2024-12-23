package edu.upc.subgrupprop113.supermarketmanager.controllers.components;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ToastLabelController extends Label {
    @FXML
    private VBox root;

    @FXML
    private Label labelText;

    @FXML
    private void initialize() {
        labelText.setVisible(false);
    }

    /**
     * Set the error message to be displayed
     * @param errorMsg The error message to be displayed
     */
    public void setErrorMsg(String errorMsg) {
        labelText.getStyleClass().clear();
        labelText.getStyleClass().add("toast-label");
        labelText.getStyleClass().add("error");
        setMsg(errorMsg);
    }

    /**
     * Set the success message to be displayed
     * @param successMsg The success message to be displayed
     */
    public void setSuccessMsg(String successMsg) {
        labelText.getStyleClass().clear();
        labelText.getStyleClass().add("toast-label");
        labelText.getStyleClass().add("success");
        setMsg(successMsg);
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
        setErrorMsg(errorMsg);
        if(milliseconds >= 0) scheduleClearMsg(milliseconds);
    }

    /**
     * Sets a success message on the associated label and displays it for a specified duration.
     * <p>After the specified duration, the success message is cleared automatically by scheduling a task
     * to hide the label using a {@code ScheduledExecutorService}.</p>
     *
     * @param successMsg the success message to display. This is set as the text of the success label.
     * @param milliseconds the duration in milliseconds for which the success message will be visible.
     *                      Must be greater than 0.
     * @throws IllegalArgumentException if the specified duration is less than or equal to 0.
     */
    public void setSuccessMsg(String successMsg, int milliseconds) throws IllegalArgumentException {
        setSuccessMsg(successMsg);
        if(milliseconds >= 0) scheduleClearMsg(milliseconds);
    }

    /**
     * Set the message to be displayed
     * @param msg The message to be displayed
     */
    public void setMsg(String msg) {
        labelText.setText(msg);
        labelText.setVisible(true);
    }

    /**
     * Clear the error message
     */
    public void clearMsg() {
        labelText.setVisible(false);
        labelText.setText("");
    }

    /**
     * Schedule a task to clear the message after a specified delay
     * @param milliseconds The delay in milliseconds
     */
    private void scheduleClearMsg(int milliseconds) {
        if (milliseconds <= 0) throw new IllegalArgumentException("Delay must be greater than 0");
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(() -> Platform.runLater(this::clearMsg), milliseconds, TimeUnit.MILLISECONDS);
        scheduler.shutdown();
    }
}
