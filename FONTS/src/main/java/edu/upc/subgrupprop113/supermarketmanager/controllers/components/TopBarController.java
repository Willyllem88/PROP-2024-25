package edu.upc.subgrupprop113.supermarketmanager.controllers.components;

import edu.upc.subgrupprop113.supermarketmanager.controllers.DomainController;
import edu.upc.subgrupprop113.supermarketmanager.controllers.PresentationController;
import edu.upc.subgrupprop113.supermarketmanager.factories.DomainControllerFactory;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
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

    @FXML
    private void showImportMenu(MouseEvent event) {
        if (domainController.hasChangesMade()) {
            errorLabelController.setErrorMsg("There are unsaved changes!\nPlease save them.", 4500);
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File to Import the new Supermarket");

        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("JSON Files", "*.json"),
            new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        //TODO: fer utils per gestionar paths
        String filePath;
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("nix") || os.contains("nux") || os.contains("aix"))
            filePath = "FONTS/src/main/resources/edu/upc/subgrupprop113/supermarketmanager/dataExamples";
        else
            filePath = "FONTS\\src\\main\\resources\\edu\\upc\\subgrupprop113\\supermarketmanager\\dataExamples";

        File initialDirectory = new File(filePath);
        // Verify that the directory exists and is a folder
        if (initialDirectory.exists() && initialDirectory.isDirectory()) {
                fileChooser.setInitialDirectory(initialDirectory);
        }
        else {
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        }

        // Show the open file dialog
        File selectedFile = fileChooser.showOpenDialog(presentationController.getPrimaryStage());

        // If a file is selected, print its path
        if (selectedFile != null) {
            try {
                domainController.importSupermarketConfiguration(selectedFile.getAbsolutePath());
            }
            catch (Exception e) {
                errorLabelController.setErrorMsg(e.getMessage(), 4500);
            }
        }
    }

    // Button Handlers

    @FXML
    private void handleSave() {
        onSaveHandler.accept(null); // Invoke the custom handler
    }

    @FXML
    private void handleSaveAs() {
        onSaveAsHandler.accept(null); // Invoke the custom handler
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

    public void setOnGoBackHandler(Consumer<Void> handler) {
        this.onGoBackHandler = handler;
    }

}
