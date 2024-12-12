package edu.upc.subgrupprop113.supermarketmanager.controllers.components;

import edu.upc.subgrupprop113.supermarketmanager.Main;
import edu.upc.subgrupprop113.supermarketmanager.controllers.DomainController;
import edu.upc.subgrupprop113.supermarketmanager.controllers.PresentationController;
import edu.upc.subgrupprop113.supermarketmanager.dtos.ProductDto;
import edu.upc.subgrupprop113.supermarketmanager.factories.DomainControllerFactory;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.function.Consumer;

public class TopBarController {

    @FXML
    private HBox root;

    @FXML
    private VBox saveButton;

    @FXML
    private VBox saveAsButton;

    @FXML
    private VBox importButton;

    @FXML
    private VBox newDistributionButton;

    @FXML
    private VBox goBackButton;

    @FXML
    private VBox powerOffButton;

    @FXML
    private ErrorLabelController errorLabelController;


    DomainController domainController = DomainControllerFactory.getInstance().getDomainController();

    private final PresentationController presentationController;

    private static final String IMPORT_TITLE = "Select File to Import the new Supermarket";
    private static final String SAVE_AS_TITLE = "Select File to Export the current Supermarket";

    public TopBarController(PresentationController presentationController) {
        this.presentationController = presentationController;
    }

    @FXML
    public void initialize() {
        // Store a reference to this controller in the root node's properties
        if (root != null) {
            root.getProperties().put("controller", this);
        }

        // Default visibility
        saveButton.setVisible(true);
        saveAsButton.setVisible(true);
        importButton.setVisible(true);
        newDistributionButton.setVisible(true);
        goBackButton.setVisible(true);
        powerOffButton.setVisible(true);
    }

    private boolean isLoggedIn = true; // TODO: Integrate with a state manager.

    private Consumer<Void> onSaveHandler = _ -> System.out.println("Default Save Handler");
    private Consumer<Void> onSaveAsHandler = _ -> System.out.println("Default Save As Handler");
    private Consumer<Void> onImportHandler = _ -> System.out.println("Default Import Handler");
    private Consumer<Void> onNewDistributionHandler = _ -> System.out.println("Default New Distribution Handler");
    private Consumer<Void> onGoBackHandler = _ -> System.out.println("Default Go Back Handler");

    @FXML
    private void showPowerOffMenu(MouseEvent event) {
        ContextMenu contextMenu = new ContextMenu();

        // Option to close the application
        MenuItem closeAppItem = new MenuItem("Close App");
        closeAppItem.setOnAction(_ -> handleCloseApp());

        // Option to log out (conditionally shown)
        if (isLoggedIn) {
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
        domainController.modifyProduct(new ProductDto("Eggs", 999.99f, "AMBIENT", "PATATA", new ArrayList<>(), new ArrayList<>()));
        domainController.exportSupermarketConfiguration(null);
        onSaveHandler.accept(null); // Invoke the custom handler
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
        domainController.modifyProduct(new ProductDto("Eggs", 0.99f, "AMBIENT", "aaaa", new ArrayList<>(), new ArrayList<>()));
        String selectedFilePath = getExportJSONFile();
        if (selectedFilePath != null) {
            try {
                domainController.exportSupermarketConfiguration(selectedFilePath);
                onSaveAsHandler.accept(null); // Invoke the custom handler
            }
            catch (Exception e) {
                errorLabelController.setErrorMsg(e.getMessage(), 4500);
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
            errorLabelController.setErrorMsg("There are unsaved changes!\nPlease save them.", 4500);
            return;
        }

        String selectedFilePath = getImportJSONFile();

        // If a file is selected, process its path
        if (selectedFilePath != null) {
            try {
                domainController.importSupermarketConfiguration(selectedFilePath);
                onImportHandler.accept(null);
            }
            catch (Exception e) {
                errorLabelController.setErrorMsg(e.getMessage(), 4500);
            }
        }
    }

    @FXML
    private void handleNewDistribution() {
        onNewDistributionHandler.accept(null); // Invoke the custom handler
    }

    @FXML
    private void handleGoBack() {
        onGoBackHandler.accept(null); // Invoke the custom handler
    }

    private void handleCloseApp() {
        System.out.println("Closing application...");
        System.exit(0);
    }

    private void handleLogOut() {
        System.out.println("Logging out...");
        domainController.logOut();
        presentationController.logOut();
        isLoggedIn = false;
    }

    // Methods to control button visibility
    public void showSaveButton(boolean visible) {
        saveButton.setVisible(visible);
    }

    public void showSaveAsButton(boolean visible) {
        saveAsButton.setVisible(visible);
    }

    public void showImportButton(boolean visible) {
        importButton.setVisible(visible);
    }

    public void showNewDistributionButton(boolean visible) {
        newDistributionButton.setVisible(visible);
    }

    public void showGoBackButton(boolean visible) {
        goBackButton.setVisible(visible);
    }

    // Methods to set custom handlers
    public void setOnSaveHandler(Consumer<Void> handler) {
        this.onSaveHandler = handler;
    }

    public void setOnSaveAsHandler(Consumer<Void> handler) {
        this.onSaveAsHandler = handler;
    }

    public void setOnNewDistributionHandler(Consumer<Void> handler) {
        this.onNewDistributionHandler = handler;
    }

    public void setOnImportHandler(Consumer<Void> handler) {
        this.onImportHandler = handler;
    }

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
