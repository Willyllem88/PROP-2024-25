package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.Main;
import edu.upc.subgrupprop113.supermarketmanager.controllers.components.TopBarController;
import edu.upc.subgrupprop113.supermarketmanager.dtos.ProductDto;
import edu.upc.subgrupprop113.supermarketmanager.dtos.ShelvingUnitDto;
import edu.upc.subgrupprop113.supermarketmanager.factories.DomainControllerFactory;
import static edu.upc.subgrupprop113.supermarketmanager.utils.AssetsImageHandler.getLogoIconPath;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Screen;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class PresentationController {

    private final Stage primaryStage;

    private static final String LOG_IN_VIEW = "fxml/logIn.fxml";
    private static final String EDIT_SHELVING_UNIT_VIEW = "fxml/editShelvingUnit.fxml";
    private static final String MAIN_SCREEN_VIEW = "fxml/mainScreen.fxml";
    private static final String EDIT_DISTRIBUTION_VIEW = "fxml/editDistribution.fxml";
    private static final String CATALOG_VIEW = "fxml/catalog.fxml";

    private final DomainController domainController = DomainControllerFactory.getInstance().getDomainController();

    public PresentationController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // -------------- Navigation methods -------------- //

    public void start() {
        primaryStage.setTitle("Supermarket Manager");
        primaryStage.getIcons().add(new Image(getLogoIconPath()));
        primaryStage.setMaximized(true);
        loadView(LOG_IN_VIEW);
    }

    public void logInSuccessful() {
        loadView(MAIN_SCREEN_VIEW);
    }

    public void logOut() {
        loadView(LOG_IN_VIEW);
    }

    public void supermarketSettings() {
        loadView(EDIT_DISTRIBUTION_VIEW);
    }

    public void goBackESU() {
        loadView(EDIT_DISTRIBUTION_VIEW);
    }

    public void goBackEditDistribution() {
        loadView(MAIN_SCREEN_VIEW);
    }

    public void shelvingUnitDeleted() {
        loadView(EDIT_DISTRIBUTION_VIEW);
    }

    public void shelvingUnitEdited(int position) {
        loadView(EDIT_SHELVING_UNIT_VIEW, position);
    }

    public void openCatalog() { loadView(CATALOG_VIEW); }

    public void showProductInShelvingUnits(ProductDto product) {
        List<ShelvingUnitDto> shelvingUnits = domainController.getShelvingUnits();
        for (int i = 0; i < shelvingUnits.size(); ++i) {
            ShelvingUnitDto shelvingUnit = shelvingUnits.get(i);
            List<ProductDto> products = shelvingUnit.getProducts();
            // Check if any products name matches the searched product
            for (ProductDto p : products) {
                if (p == null) continue;
                if (p.getName().equals(product.getName())) {
                    loadView(MAIN_SCREEN_VIEW, i);
                }
            }
        }
    }

    /**
     * Loads the view specified by the resource path.
     * This version of the method does not require an additional parameter for the controller.
     *
     * @param resource       the path to the FXML file to load.
     */
    private void loadView(String resource) {
        loadView(resource, -1);
    }

    /**
     * Loads the view specified by the resource path
     *
     * @param resource       the path to the FXML file to load.
     * @param param1Int      an integer parameter to pass to the controller. It can be used in:
     *                          - {@link EditShelvingUnitController} to set the position of the shelving unit to edit.
     */
    private void loadView(String resource, int param1Int) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(resource));
            // Set the controller factory to instantiate controllers with parameters
            loader.setControllerFactory(controllerClass -> {
                if (controllerClass == LogInController.class) {
                    return new LogInController(this);
                }
                if (controllerClass == TopBarController.class) {
                    return new TopBarController(this);
                }
                if (controllerClass == MainScreenController.class && param1Int == -1) {
                    return new MainScreenController(this);
                }
                if (controllerClass == MainScreenController.class) {
                    return new MainScreenController(this, param1Int);
                }
                if (controllerClass == EditDistributionController.class) {
                    return new EditDistributionController(this);
                }
                if (controllerClass == EditShelvingUnitController.class) {
                    return new EditShelvingUnitController(this, param1Int);
                }
                if (controllerClass == CatalogController.class) {
                    return new CatalogController(this);
                }
                /*MORE CONTROLLERS HERE
                 */
                // Fallback: instantiate other controllers (like TopBarController)
                try {
                    return controllerClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new IllegalArgumentException("Unable to create controller: " + controllerClass.getName(), e);
                }
            });

            double currentWidth = primaryStage.getWidth();
            double currentHeight = primaryStage.getHeight();

            Parent root = loader.load();
            Scene scene = new Scene(root);

            primaryStage.setWidth(currentWidth);
            primaryStage.setHeight(currentHeight);

            double screenWidth = Screen.getPrimary().getBounds().getWidth();
            double screenHeight = Screen.getPrimary().getBounds().getHeight();

            primaryStage.setMinWidth(screenWidth / 2);
            primaryStage.setMinHeight(screenHeight / 2);

            scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("css/global.css")).toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays a file selection dialog to the user and returns the selected file's absolute path.
     *
     * @param title                the title for the dialog window
     * @param defaultDirectoryPath the path to the default directory to open, if valid
     * @return the absolute path of the selected file, or {@code null} if no file was selected
     */
    public String showSelectDialog(String title, String defaultDirectoryPath) {
        return showSelectDialog(title, defaultDirectoryPath, "All Files", "*");
    }

    /**
     * Displays a file selection dialog to the user with specified filters and returns the selected file's absolute path.
     *
     * @param title                the title for the dialog window
     * @param defaultDirectoryPath the path to the default directory to open, if valid
     * @param formatMessage        the description of the file format (e.g., "Text Files")
     * @param extension            the file extension filter (e.g., "*.txt")
     * @return the absolute path of the selected file, or {@code null} if no file was selected
     */
    public String showSelectDialog(String title, String defaultDirectoryPath, String formatMessage, String extension) {
        FileChooser fileChooser = configFileChooser(title, defaultDirectoryPath, formatMessage, extension);

        // Show the open file dialog
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) return null;
        return selectedFile.getAbsolutePath();
    }

    /**
     * Displays a file saving dialog to the user and returns the selected file's absolute path.
     *
     * @param title                the title for the dialog window
     * @param defaultDirectoryPath the path to the default directory to open, if valid
     * @return the absolute path of the file to be saved, or {@code null} if no file was selected
     */
    public String showSaveDialog(String title, String defaultDirectoryPath) {
        return showSaveDialog(title, defaultDirectoryPath, "All Files", "*");
    }

    /**
     * Displays a file saving dialog to the user with specified filters and returns the selected file's absolute path.
     *
     * @param title                the title for the dialog window
     * @param defaultDirectoryPath the path to the default directory to open, if valid
     * @param formatMessage        the description of the file format (e.g., "Text Files")
     * @param extension            the file extension filter (e.g., "*.txt")
     * @return the absolute path of the file to be saved, or {@code null} if no file was selected
     */
    public String showSaveDialog(String title, String defaultDirectoryPath, String formatMessage, String extension) {
        FileChooser fileChooser = configFileChooser(title, defaultDirectoryPath, formatMessage, extension);

        // Show the save file dialog
        File selectedFile = fileChooser.showSaveDialog(primaryStage);
        if (selectedFile == null) return null;
        return selectedFile.getAbsolutePath();
    }

    /**
     * Configures a {@link FileChooser} with the specified parameters.
     *
     * @param title          the title to display on the file chooser dialog.
     * @param defaultDirectoryPath    the initial directory to open in the file chooser.
     *                       If null or invalid, the user's home directory will be used.
     * @param formatMessage  a description for the file extension filter.
     * @param extension      the file extension to filter (e.g., "*.txt").
     * @return a configured {@link FileChooser} instance.
     */
    private FileChooser configFileChooser(String title, String defaultDirectoryPath, String formatMessage, String extension) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);


        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(formatMessage, extension)
        );

        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        if (defaultDirectoryPath != null) {
            File initialDirectory = new File(defaultDirectoryPath);
            // Verify that the directory exists and is a folder
            if (initialDirectory.exists() && initialDirectory.isDirectory()) {
                fileChooser.setInitialDirectory(initialDirectory);
            }
        }

        return fileChooser;
    }
}
