# Using the `ErrorLabelController`

The `ErrorLabelController` is a reusable component that displays error messages in your JavaFX application. You can include it in your FXML files to show error messages dynamically in your views.

## Steps to Use `ErrorLabelController`

### 1. Include `ErrorLabel` in Your FXML File

To use the `ErrorLabelController` in your FXML, include the following line where you want the error message to appear:

```xml
<fx:include source="components/errorLabel.fxml" fx:id="errorLabel" />
```

This line will include the `ErrorLabel` defined in `ErrorLabel.fxml` into your layout. The `fx:id="errorLabel"` will allow you to reference this component in your controller.

### 2. Access and Use the ErrorLabel in Your Controller

In the controller of the FXML file where you included the `ErrorLabel`, you need to inject the `ErrorLabelController` and call its methods to set or clear error messages.

```java
@FXML
private ErrorLabelController errorLabelController;
```

### 3. Error Handling Methods

The `ErrorLabelController` provides two methods to control the error message:

- `setErrorMsg(String errorMsg)`: Displays the error message on the screen.
- `setErrorMsg(String errorMsg, int milliseconds)`: Displays the error message on the screen for the specified time.
- `clearErrorMsg()`: Clears the error message.

### 4. Example Usage

LogInController.java has an example of how to use the `ErrorLabelController` to display an error message when the user enters an incorrect username or password. Visit the [LogInController.java] file to see the implementation.

---

### Summary

- **Include the `ErrorLabel`** in your FXML using `<fx:include source="components/ErrorLabel.fxml" fx:id="errorLabel" />`.
- **Inject and use `ErrorLabelController`** in your controller to display and clear error messages.

This pattern allows you to centralize error handling and reuse the error message display component across different views in your JavaFX application.
