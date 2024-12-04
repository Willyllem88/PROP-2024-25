package edu.upc.subgrupprop113.supermarketmanager.controllers.components;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class SetTemperatureController {
    @FXML
    private ChoiceBox<String> temperatureChoiceBox;

    public void getTemperature() {
        String temperature = temperatureChoiceBox.getValue();
        System.out.println(temperature);
    }
}
