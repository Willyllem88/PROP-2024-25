package edu.upc.subgrupprop113.supermarketmanager.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

public class ShelvingUnitConfigController {

    @FXML
    private MenuButton menuButton;

    @FXML
    private void initialize() {
        // Add an Ikonli icon to the menu button
        FontIcon menuIcon = new FontIcon("fth-power"); // Feather icon 'menu'
        menuIcon.setIconSize(30);
        menuButton.setGraphic(menuIcon); // Set the icon as the button's graphic
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        // Lógica de logout
        System.out.println("Logging out...");
    }

    @FXML
    private void handleCloseApp(ActionEvent event) {
        // Lógica para cerrar la aplicación
        System.out.println("Closing application...");
    }
}
