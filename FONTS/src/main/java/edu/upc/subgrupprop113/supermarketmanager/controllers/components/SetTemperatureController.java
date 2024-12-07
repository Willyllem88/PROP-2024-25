package edu.upc.subgrupprop113.supermarketmanager.controllers.components;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class SetTemperatureController {
    @FXML
    private ChoiceBox<String> temperatureChoiceBox;

    /**
     * Returns the temperature selected by the user
     * @return the temperature selected by the user. Can be "AMBIENT", "REFRIGERATED" or "FROZEN".
     */
    public String getTemperature() {
        String temperature = temperatureChoiceBox.getValue();

        if (temperature.equals("AMBIENT")) return "AMBIENT";
        else if (temperature.equals("FRIDGE")) return "REFRIGERATED";
        else return "FROZEN";
    }

    /**
     * Sets the temperature of the product
     * @param temperature the temperature of the product. Can be "AMBIENT", "REFRIGERATED" or "FROZEN".
     */
    public void setTemperature(String temperature) {
        if (temperature.equals("AMBIENT")) temperatureChoiceBox.setValue("AMBIENT");
        else if (temperature.equals("REFRIGERATED")) temperatureChoiceBox.setValue("FRIDGE");
        else temperatureChoiceBox.setValue("FREEZER");
    }
}
