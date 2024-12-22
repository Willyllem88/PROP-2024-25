package edu.upc.subgrupprop113.supermarketmanager.controllers.components;

import edu.upc.subgrupprop113.supermarketmanager.Main;
import edu.upc.subgrupprop113.supermarketmanager.controllers.DomainController;
import edu.upc.subgrupprop113.supermarketmanager.controllers.PresentationController;
import edu.upc.subgrupprop113.supermarketmanager.factories.DomainControllerFactory;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.nio.file.Paths;
import java.util.function.Consumer;

/**
 * Controller for the top bar of the application.
 *
 * <p>Handles the actions of the top bar buttons, such as saving, importing, and navigating to different views.
 * Provides methods to set the visibility of the buttons and to set custom handlers for the actions.
 * </p>
 */
public class TopBarController {

    @FXML
    private HBox root;

    @FXML
    private VBox saveButton;

    @FXML
    private VBox saveAsButton;

    @FXML
    private VBox catalogButton;

    @FXML
    private VBox importButton;

    @FXML
    private VBox newDistributionButton;

    @FXML
    private VBox goBackButton;

    @FXML
    private VBox powerOffButton;

    @FXML
    private VBox superSettingsButton;

    @FXML
    public ToastLabelController toastLabelController;


    DomainController domainController = DomainControllerFactory.getInstance().getDomainController();

    private final PresentationController presentationController;

    private static final String IMPORT_TITLE = "Select File to Import the new Supermarket";
    private static final String SAVE_AS_TITLE = "Select File to Export the current Supermarket";
    private static final Integer TOAST_MILLISECONDS = 4500;

    private Consumer<Void> onSaveHandler = _ -> System.out.println("Default Save Handler");
    private Consumer<Void> onSaveAsHandler = _ -> System.out.println("Default Save As Handler");
    private Consumer<Void> onImportHandler = _ -> System.out.println("Default Import Handler");
    private Consumer<Void> onNewDistributionHandler = _ -> System.out.println("Default New Distribution Handler");
    private Consumer<Void> onGoBackHandler = _ -> System.out.println("Default Go Back Handler");

    public TopBarController(PresentationController presentationController) {
        this.presentationController = presentationController;
    }

    /**
     * Initializes the controller.
     *
     * <p>Stores a reference to this controller in the root node's properties.
     * Sets the default visibility of the buttons.</p>
     */
    @FXML
    public void initialize() {
        // Store a reference to this controller in the root node's properties
        if (root != null) {
            root.getProperties().put("controller", this);
        }

        // Default visibility
        saveButton.setVisible(true);
        saveAsButton.setVisible(true);
        catalogButton.setVisible(true);
        importButton.setVisible(true);
        newDistributionButton.setVisible(true);
        goBackButton.setVisible(true);
        powerOffButton.setVisible(true);
        superSettingsButton.setVisible(true);
    }

    @FXML
    private void showPowerOffMenu(MouseEvent event) {
        ContextMenu contextMenu = new ContextMenu();

        // Option to close the application
        MenuItem closeAppItem = new MenuItem("Close App");
        closeAppItem.setOnAction(_ -> handleCloseApp());

        // Option to log out (conditionally shown)
        if (domainController.isLogged()) {
            System.out.println("User is logged in.");
            MenuItem logoutItem = new MenuItem("Log Out");
            logoutItem.setOnAction(_ -> handleLogOut());
            contextMenu.getItems().add(logoutItem);
        }

        contextMenu.getItems().add(closeAppItem);

        // Show the menu near the clicked button
        contextMenu.show(powerOffButton, event.getScreenX(), event.getScreenY());
    }

    // Button Handlers
    /**
     * Handles the "Save" action.
     *
     * <p>Exports the current supermarket configuration using a default file path.
     * Invokes the custom save handler after successfully saving the configuration.</p>
     */
    @FXML
    private void handleSave() {
        try {
            domainController.exportSupermarketConfiguration(null);
            toastLabelController.setSuccessMsg("Exported Successful!", TOAST_MILLISECONDS);
            onSaveHandler.accept(null); // Invoke the custom handler
        }
        catch (Exception e) {
            toastLabelController.setErrorMsg(e.getMessage(), TOAST_MILLISECONDS);
        }
    }

    /**
     * Handles the "Save As" action.
     *
     * <p>Opens a file-saving dialog to allow the user to specify a file path.
     * If a valid path is provided, exports the current supermarket configuration to the selected file.
     * Invokes the custom save-as handler after successfully saving the configuration.
     * Displays an error message if the export fails.</p>
     */
    @FXML
    private void handleSaveAs() {
        String selectedFilePath = getExportJSONFile();
        if (selectedFilePath != null) {
            try {
                domainController.exportSupermarketConfiguration(selectedFilePath);
                toastLabelController.setSuccessMsg("Exported Successful!", TOAST_MILLISECONDS);
                onSaveAsHandler.accept(null); // Invoke the custom handler
            }
            catch (Exception e) {
                toastLabelController.setErrorMsg(e.getMessage(), TOAST_MILLISECONDS);
            }
        }
    }

    /**
     * Handles the "Import" action.
     *
     * <p>Opens a file selection dialog to allow the user to select a JSON file for importing a supermarket configuration.
     * If the domain has unsaved changes, displays a warning message and prevents the import action.
     * If a valid file is selected, attempts to import the configuration.
     * Invokes the custom import handler after successfully importing the configuration.
     * Displays an error message if the import fails.</p>
     */
    @FXML
    private void handleImport() {
        if (domainController.hasChangesMade()) {
            toastLabelController.setErrorMsg("There are unsaved changes!\nPlease save them.", TOAST_MILLISECONDS);
            return;
        }

        String selectedFilePath = getImportJSONFile();

        // If a file is selected, process its path
        if (selectedFilePath != null) {
            try {
                importDistribution(selectedFilePath);
            }
            catch (Exception e) {
                if (e.getMessage().equals("The supermarket distribution must be empty.")) {
                    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationAlert.setTitle("Import Confirmation");
                    confirmationAlert.setHeaderText("Warning: This action is irreversible!");
                    confirmationAlert.setContentText("You are about to import a new distribution.\n\n" +
                            "Please note:\n" +
                            "- Any unsaved changes will be permanently lost.\n" +
                            "- This action cannot be undone.\n\n" +
                            "Are you sure you want to proceed?");


                    ButtonType result = confirmationAlert.showAndWait().orElse(ButtonType.CANCEL);

                    if (result == ButtonType.OK) {
                        domainController.eraseSupermarketDistribution();
                        importDistribution(selectedFilePath);
                    }
                }
            }
        }
    }

    private void importDistribution(String selectedFilePath) {
        domainController.importSupermarketConfiguration(selectedFilePath);
        toastLabelController.setSuccessMsg("Import Successful!", TOAST_MILLISECONDS);
        onImportHandler.accept(null);
    }

    /**
     * Displays a success message in the toast label.
     *
     * @param text the message to display
     * @param time the duration of the message in milliseconds
     */
    public void toastSuccess(String text, Integer time) {
        toastLabelController.setSuccessMsg(text, time);
    }

    /**
     * Displays an error message in the toast label.
     *
     * @param text the message to display
     * @param time the duration of the message in milliseconds
     */
    public void toastError(String text, Integer time) {
        toastLabelController.setErrorMsg(text, time);
    }

    /**
     * Opens the catalog view.
     */
    @FXML
    private void handleCatalog() {
        if (presentationController != null) {
            presentationController.openCatalog();
        }
    }

    /**
     * Handles the "Go Back" action.
     *
     * <p>Invokes the custom go-back handler.</p>
     */
    @FXML
    private void handleGoBack() {
        onGoBackHandler.accept(null); // Invoke the custom handler
    }

    @FXML
    private void handleNewDistribution() {
        onNewDistributionHandler.accept(null);
    }

    /**
     * Close the application.
     */
    /**
     * Displays a confirmation dialog with a title, header, and content message. Returns the user's response.
     * The dialog contains "Yes" and "No" buttons, with "No" as the default option.
     *
     * @param title
     * @param header
     * @param content
     * @return {@code true} if the user confirms the action, {@code false} otherwise
     */
    private Boolean askForConfirmation(String title, String header, String content) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle(title);
        confirmationAlert.setHeaderText(header);
        confirmationAlert.setContentText(content);

        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        confirmationAlert.getButtonTypes().setAll(yesButton, noButton);

        ButtonType result = confirmationAlert.showAndWait().orElse(noButton);

        return result == yesButton;
    }

    private void handleCloseApp() {
        if (domainController.hasChangesMade()) {
            String title = "Close Application Confirmation";
            String header = "Warning: There are unsaved changes!";
            String content = "You have unsaved changes in the current supermarket distribution.\n\n" +
                    "Please note:\n" +
                    "- Any unsaved changes will be permanently lost.\n" +
                    "- This action cannot be undone.\n\n" +
                    "Are you sure you want to close the application?";

            if (askForConfirmation(title, header, content)) {
                System.exit(0);
            }
        }
        else {
            System.exit(0);
        }
    }

    /**
     * Logs out the current user.
     */
    private void handleLogOut() {
        if (domainController.hasChangesMade()) {
            String title = "Log Out Confirmation";
            String header = "Warning: There are unsaved changes!";
            String content = "You have unsaved changes in the current supermarket distribution.\n\n" +
                    "Please note:\n" +
                    "- Any unsaved changes will be permanently lost.\n" +
                    "- This action cannot be undone.\n\n" +
                    "Are you sure you want to log out?";

            if (askForConfirmation(title, header, content)) {
                domainController.logOut();
                presentationController.logOut();
            }
        }
        else {
            domainController.logOut();
            presentationController.logOut();
        }
    }

    /**
     * Opens the supermarket settings view.
     */
    @FXML
    private void handleSupermarketSettings() {
        presentationController.supermarketSettings();
    }

    /**
     * Sets the visibility of the "Go Back" button.
     *
     * @param visible {@code true} to show the button, {@code false} to hide it
     */
    public void showSaveButton(boolean visible) {
        saveButton.setVisible(visible);
    }

    /**
     * Sets the visibility of the "Save As" button.
     *
     * @param visible {@code true} to show the button, {@code false} to hide it
     */
    public void showSaveAsButton(boolean visible) {
        saveAsButton.setVisible(visible);
    }

    /**
     * Sets the visibility of the "Catalog" button.
     *
     * @param visible {@code true} to show the button, {@code false} to hide it
     */
    public void showCatalogButton(boolean visible) { catalogButton.setVisible(visible); }

    /**
     * Sets the visibility of the "Import" button.
     *
     * @param visible {@code true} to show the button, {@code false} to hide it
     */
    public void showImportButton(boolean visible) {
        importButton.setVisible(visible);
    }

    /**
     * Sets the visibility of the "New Distribution" button.
     *
     * @param visible {@code true} to show the button, {@code false} to hide it
     */
    public void showNewDistributionButton(boolean visible) {
        newDistributionButton.setVisible(visible);
    }

    /**
     * Sets the visibility of the "Power Off" button.
     *
     * @param visible {@code true} to show the button, {@code false} to hide it
     */
    public void showDistributionSettings(boolean visible) {
        superSettingsButton.setVisible(visible);
    }

    /**
     * Sets the visibility of the "Power Off" button.
     *
     * @param visible {@code true} to show the button, {@code false} to hide it
     */
    public void showGoBackButton(boolean visible) {
        goBackButton.setVisible(visible);
    }

    /**
     * Sets the behavior of the "New Distribution" button.
     *
     * @param handler the function to be invoked when the button is clicked
     */
    public void setOnNewDistributionHandler(Consumer<Void> handler) {
        this.onNewDistributionHandler = handler;
    }

    /**
     * Sets the behavior of the "Import" button.
     *
     * @param handler the function to be invoked when the button is clicked
     */
    public void setOnImportHandler(Consumer<Void> handler) {
        this.onImportHandler = handler;
    }

    /**
     * Sets the behavior of the "Go Back" button.
     *
     * @param handler the function to be invoked when the button is clicked
     */
    public void setOnGoBackHandler(Consumer<Void> handler) {
        this.onGoBackHandler = handler;
    }

    /**
     * Opens a file selection dialog for importing a JSON file.
     *
     * @return the absolute path of the selected JSON file, or {@code null} if no file was selected
     */
    private String getImportJSONFile() {
        return presentationController.showSelectDialog(TopBarController.IMPORT_TITLE, getDefaultDirectoryConfigurationPath(), "JSON Files", "*.json");
    }

    /**
     * Opens a file saving dialog for exporting a JSON file.
     *
     * @return the absolute path of the file to be saved, or {@code null} if no file was selected
     */
    private String getExportJSONFile() {
        return presentationController.showSaveDialog(TopBarController.SAVE_AS_TITLE, getDefaultDirectoryConfigurationPath(), "JSON Files", "*.json");
    }

    /**
     * Retrieves the default directory path for configuration files.
     *
     * <p>Attempts to locate a directory named "dataExamples" within the application's resources.
     * If the directory cannot be located, {@code null} is returned.</p>
     *
     * @return the absolute path of the default configuration directory, or {@code null} if the directory cannot be found
     */
    private String getDefaultDirectoryConfigurationPath() {
        // TODO: Create a directory for saving supermarkets
        try {
            return Paths.get(Main.class.getResource("dataExamples").toURI()).toString();
        } catch (Exception e) {
            return null;
        }
    }

}
