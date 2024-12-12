# Documentation: Using the Top Bar Component

This document provides a guide to integrating and using the **Top Bar Component** in your JavaFX application. The Top Bar is a reusable UI element designed to provide common functionalities like **Save**, **Save As**, **New Distribution**, **Go Back**, and **Power Off**.

---

## Structure Overview

### **Files**

1. **`topBar.fxml`** in `src/main/resources/edu/upc/subgrupprop113/supermarketmanager/fxml/components`
   Contains the layout and styles of the Top Bar.

2. **`TopBarController.java`** in `src/main/java/edu/upc/subgrupprop113/supermarketmanager/controllers/components`
   The controller file handling the logic and interactivity of the Top Bar.

3. **`TopBar.css`** in **`src/main/resources/edu/upc/subgrupprop113/supermarketmanager/css/TopBar.css`**
   Provides the visual styling for the Top Bar and is loaded automatically by the fxml file.

---

## Usage Instructions

### **1. Including the Top Bar in Your View**

To include the Top Bar in your FXML view, use the `<fx:include>` tag within your layout's FXML file. For example:

```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.layout.*?>

<VBox xmlns="http://javafx.com/javafx/17.0.1" xmlns:fx="http://javafx.com/fxml/1">
    <!-- Include the Top Bar -->
    <top>
        <fx:include source="components/topBar.fxml" fx:id="topBar"/>
    </top>

    <!-- Your screen-specific content here -->
    <Label text="Main Content" />
</VBox>
```

This will embed the Top Bar at the top of your screen layout.

---

### **2. Accessing the Top Bar Controller**

The `TopBarController` is automatically instantiated via the FXML. To customize its functionality, you need to access it programmatically from your screen's controller.

#### Example: Accessing the Top Bar Controller

```java
public class YourScreenController {

    ...
    // Other fields and methods
    ...

    @FXML
    private HBox topBar;

    private TopBarController topBarController;

    @FXML
    public void initialize() {
        // Retrieve the Top Bar Controller from the HBox properties
        topBarController = (TopBarController) topBar.getProperties().get("controller");

        if (topBarController != null) {
            // Customize visibility
            topBarController.showGoBackButton(false);
            topBarController.showNewDistributionButton(false);
            topBarController.showSaveButton(false);
            topBarController.showSaveAsButton(false);

            // Set custom handlers
            topBarController.setOnGoBackHandler(_ -> System.out.println("Custom Go Back Action"));
        }
    }
}
```

---

### **3. Customizing Button Visibility**

You can control the visibility of specific buttons using the provided methods in `TopBarController`.

#### Example: Hiding Specific Buttons

```java
topBarController.showSaveButton(false); // Hides the Save button
topBarController.showNewDistributionButton(true); // Shows the New Distribution button
```

---

### **4. Handling Button Actions**

By default, the Top Bar buttons have predefined handlers. However, you can override these handlers to customize their behavior in your screen.

#### Example: Setting Custom Button Actions

```java
topBarController.setOnSaveHandler(_ -> {
    System.out.println("Save button clicked!");
    // Add custom save logic here
});

topBarController.setOnNewDistributionHandler(_ -> {
    System.out.println("New distribution created!");
    // Add custom new distribution logic here
});
```

---

### **5. Dynamic Power Off Menu**

The **Power Off** button displays a contextual menu with options to **Close the App** or **Log Out**. The **Log Out** option should be displayed only if the user is logged in.

**TODO**: Add state management for user login status.

---

### **6. File Handling methods**
The Top Bar provides functionalities for **Save**, **Save As**, and **Import** actions, each with specific behaviors:

#### **1. handleSave()**

- **Description**: Exports the current supermarket configuration without specifying a path.
- **Custom Handler**: Allows executing a custom handler through `onSaveHandler` after the save operation is completed.

---

#### **2. handleSaveAs()**

- **Description**: Displays a dialog to save the supermarket configuration to a user-defined location.
- **Custom Handler**: Executes a custom handler via `onSaveAsHandler` after the file is successfully saved.

---

#### **3. handleImport()**

- **Description**: Displays a dialog to import a supermarket configuration from a file.
- **Unsaved Changes Validation**: Checks if there are unsaved changes before proceeding with the import.
- **Custom Handler**: Executes a custom handler via `onImportHandler` once the file is imported.


## Summary

- **Top Bar Inclusion:** Use `<fx:include>` and `<fx:id>` to add the Top Bar to your screen and access its controller.
- **Controller Access:** Retrieve the Top Bar Controller from the HBox properties.
- **Custom Handlers:** Override default button actions using `setOn<Handler>` methods.
- **Dynamic Visibility:** Control button visibility based on your screen's requirements.
- **Power Off Menu:** When state management is set up it would dynamically manages options to **Log Out** or **Close App**.

---
