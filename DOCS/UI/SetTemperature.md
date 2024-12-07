# Using the `SetTemperatureController`

The `SetTemperatureController` provides a `ChoiceBox` that allows users to select a temperature setting for a product. The available options are `"AMBIENT"`, `"REFRIGERATED"`, and `"FROZEN"`. You can include this component in your FXML files and access the selected value via the controller.

## Steps to Use `SetTemperatureController`

### 1. Include `SetTemperature` in Your FXML File

To use the `SetTemperatureController` in your FXML, include the following line where you want the temperature selection component to appear:

```xml
<fx:include source="components/setTemperature.fxml" fx:id="setTemperature" />
```

This will include the `SetTemperature` component defined in `SetTemperature.fxml` into your layout, and the `fx:id="setTemperature"` will allow you to reference it in your controller.

### 2. Access and Use the `SetTemperatureController` in Your Controller

In the controller of the FXML file where you included the `SetTemperature`, you need to inject the `SetTemperatureController` and call its method to get the selected temperature value.

#### Example: LogInController.java (or any other controller where you want to access the temperature)

```java
package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.controllers.components.SetTemperatureController;
import javafx.fxml.FXML;

public class LogInController {  // Or another controller

    @FXML
    private SetTemperatureController setTemperatureController;  // Injecting the SetTemperatureController

    /**
     * Example method to get the selected temperature
     */
    @FXML
    public void handleTemperatureSelection() {
        String selectedTemperature = setTemperatureController.getTemperature();
        System.out.println("Selected temperature: " + selectedTemperature);
        // Further logic based on the selected temperature
    }
}
```

### 3. Get the Selected Temperature

The `SetTemperatureController` provides a method to retrieve the selected temperature:

- `getTemperature()`: Returns the selected temperature. The options are `"AMBIENT"`, `"REFRIGERATED"`, and `"FROZEN"`. It returns the following values:
  - `"AMBIENT"` for ambient temperature.
  - `"REFRIGERATED"` for refrigerated temperature (FRIDGE in the `ChoiceBox`).
  - `"FROZEN"` for frozen temperature (FREEZER in the `ChoiceBox`).

---

### Summary

- **Include the `SetTemperature` component** in your FXML using `<fx:include source="components/SetTemperature.fxml" fx:id="setTemperature" />`.
- **Inject and use the `SetTemperatureController`** in your controller to get the selected temperature.
- **Get the selected temperature** with the `getTemperature()` method, which returns `"AMBIENT"`, `"REFRIGERATED"`, or `"FROZEN"`.

This pattern allows you to integrate temperature selection in your JavaFX application and access the user's selection from the controller.
