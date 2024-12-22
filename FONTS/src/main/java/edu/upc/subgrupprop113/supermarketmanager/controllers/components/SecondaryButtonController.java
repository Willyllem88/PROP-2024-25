package edu.upc.subgrupprop113.supermarketmanager.controllers.components;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

/**
 * Controller for the SecondaryButton component
 * <p>
 * This controller is used to handle the user interaction with the SecondaryButton component.
 * The component is a red secondary button with a label. The label and the click handler can be customized.
 * <p>
 */
public class SecondaryButtonController {

    @FXML
    private VBox root;

    @FXML
    private Label label;

    /**
     * On Click handler
     */
    private Consumer<Void> onClickHandler = _ -> System.out.println("Default Cancel Click Handler");

    /**
     * Initialize the component
     */
    @FXML
    public void initialize() {
        if (root != null) {
            root.getProperties().put("controller", this);
        }
        label.setText("Cancel"); // Default label
    }

    /**
     * Handle the button click
     */
    @FXML
    private void handleClick() {
        onClickHandler.accept(null); // Call the custom handler
    }

    /**
     * Set the button label text
     * @param text The text to set
     */
    public void setLabelText(String text) {
        label.setText(text);
    }

    /**
     * Set the button click handler
     * @param handler The handler to set
     */
    public void setOnClickHandler(Consumer<Void> handler) {
        this.onClickHandler = handler;
    }
}
