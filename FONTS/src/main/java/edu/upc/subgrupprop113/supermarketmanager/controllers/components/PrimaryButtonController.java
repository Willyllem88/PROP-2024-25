package edu.upc.subgrupprop113.supermarketmanager.controllers.components;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

public class PrimaryButtonController {

    @FXML
    private VBox root;

    @FXML
    private Button button;

    @FXML
    private Label label;

    private Consumer<Void> onClickHandler = _ -> System.out.println("Default Click Handler");

    @FXML
    public void initialize() {
        if (root != null) {
            root.getProperties().put("controller", this);
        }
        label.setText("Button"); // Default label
    }

    @FXML
    private void handleClick() {
        onClickHandler.accept(null); // Call the custom handler
    }

    // Set the button label text
    public void setLabelText(String text) {
        label.setText(text);
    }

    // Set the button click handler
    public void setOnClickHandler(Consumer<Void> handler) {
        this.onClickHandler = handler;
    }
}
