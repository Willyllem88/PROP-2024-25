# Documentation: Using the Primary and Secondary Button Components

This document provides a guide to integrating and using the **Primary Button** and **Secondary Button** components in your JavaFX application. These buttons are reusable UI elements designed for consistent styling and functionality across your app.

---

## Structure Overview

### **Files**

1. **Primary Button**

   - **`primaryButton.fxml`** in `src/main/resources/edu/upc/subgrupprop113/supermarketmanager/fxml/components`
     Contains the layout and styles of the Primary Button.
   - **`PrimaryButtonController.java`** in `src/main/java/edu/upc/subgrupprop113/supermarketmanager/controllers/components`
     The controller file handling the logic and interactivity of the Primary Button.

2. **Secondary Button**

   - **`secondaryButton.fxml`** in `src/main/resources/edu/upc/subgrupprop113/supermarketmanager/fxml/components`
     Contains the layout and styles of the Secondary Button.
   - **`SecondaryButtonController.java`** in `src/main/java/edu/upc/subgrupprop113/supermarketmanager/controllers/components`
     The controller file handling the logic and interactivity of the Secondary Button.

3. **CSS Styles**
   - **`PrimaryButton.css`**
   - **`SecondaryButton.css`**
   - Both located in `src/main/resources/edu/upc/subgrupprop113/supermarketmanager/css`
     Both provide the visual styling for their respective buttons and are loaded automatically.

---

## Usage Instructions

### **1. Including Buttons in Your View**

To include a button in your FXML view, use the `<fx:include>` tag. For example:

#### Including a Primary Button

```xml
<fx:include source="components/primaryButton.fxml" fx:id="primaryButton"/>
```

#### Including a Secondary Button

```xml
<fx:include source="components/secondaryButton.fxml" fx:id="secondaryButton"/>
```

---

### **2. Accessing the Button Controller**

The button controllers are automatically instantiated via FXML. To customize their functionality, you need to access them programmatically from your screen's controller.

#### Example: Accessing the Button Controllers

```java
public class YourScreenController {

    @FXML
    private VBox primaryButton;

    @FXML
    private VBox secondaryButton;

    @FXML
    public void initialize() {
        // Access the Primary Button Controller
        PrimaryButtonController primaryController = (PrimaryButtonController) primaryButton.getProperties().get("controller");
        if (primaryController != null) {
            primaryController.setLabelText("Confirm");
            primaryController.setOnClickHandler(_ -> System.out.println("Primary button clicked!"));
        }

        // Access the Secondary Button Controller
        SecondaryButtonController secondaryController = (SecondaryButtonController) secondaryButton.getProperties().get("controller");
        if (secondaryController != null) {
            secondaryController.setLabelText("Cancel");
            secondaryController.setOnClickHandler(_ -> System.out.println("Secondary button clicked!"));
        }
    }
}
```

---

### **3. Customizing Button Text**

You can dynamically set the text displayed on the button using the `setLabelText` method provided in both controllers.

#### Example: Setting Button Text

```java
primaryController.setLabelText("Submit");
secondaryController.setLabelText("Cancel");
```

---

### **4. Handling Button Actions**

By default, both buttons have predefined click handlers that log a default message to the console. You can override these handlers using the `setOnClickHandler` method.

#### Example: Setting Custom Button Actions

```java
primaryController.setOnClickHandler(_ -> {
    System.out.println("Primary button confirmed!");
    // Add custom logic here
});

secondaryController.setOnClickHandler(_ -> {
    System.out.println("Secondary button cancelled!");
    // Add custom logic here
});
```

---

## Summary

- **Button Inclusion:** Use `<fx:include>` to add Primary or Secondary Buttons to your screen and access their controllers via the `fx:id` property.
- **Controller Access:** Retrieve the button controllers from the parent container’s properties.
- **Custom Labels:** Set button text dynamically using the `setLabelText` method.
- **Custom Handlers:** Override default click behavior using the `setOnClickHandler` method.

---
