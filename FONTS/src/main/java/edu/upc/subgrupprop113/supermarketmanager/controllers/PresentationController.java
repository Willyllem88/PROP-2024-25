package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.Main;
import edu.upc.subgrupprop113.supermarketmanager.controllers.components.TopBarController;
import edu.upc.subgrupprop113.supermarketmanager.factories.DomainControllerFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Objects;

public class PresentationController {

    private final Stage primaryStage;

    private static final String LOG_IN_VIEW = "fxml/logIn.fxml";
    private static final String SHELVING_UNIT_CONFIG_VIEW = "fxml/shelvingUnitConfig.fxml";
    private static final String MAIN_SCREEN_VIEW = "fxml/mainScreen.fxml";

    private final DomainController domainController = DomainControllerFactory.getInstance().getDomainController();

    public PresentationController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void start() {
        double currentWidth = primaryStage.getWidth();
        double currentHeight = primaryStage.getHeight();

        loadView(LOG_IN_VIEW, currentWidth, currentHeight);
    }

    public void logInSuccessful() {
        double currentWidth = primaryStage.getWidth();
        double currentHeight = primaryStage.getHeight();

        loadView(MAIN_SCREEN_VIEW, currentWidth, currentHeight);
    }

    public void logOut() {
        double currentWidth = primaryStage.getWidth();
        double currentHeight = primaryStage.getHeight();

        loadView(LOG_IN_VIEW, currentWidth, currentHeight);
    }

    public void shelvingUnitDeleted() {
        double currentWidth = primaryStage.getWidth();
        double currentHeight = primaryStage.getHeight();

        loadView(MAIN_SCREEN_VIEW, currentWidth, currentHeight);
    }

    private void loadView(String resource, double previousWidth, double previousHeight) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(resource));
            // Configura la fábrica de controladores
            loader.setControllerFactory(controllerClass -> {
                if (controllerClass == LogInController.class) {
                    return new LogInController(this);
                }
                if (controllerClass == TopBarController.class) {
                    return new TopBarController(this);
                }
                if (controllerClass == MainScreenController.class) {
                    return new MainScreenController(this);
                }
                if (controllerClass == ShelvingUnitConfigController.class) {
                    return new ShelvingUnitConfigController(this);
                }
                /*
                MORE CONTROLLERS HERE
                 */
                // Fallback: instantiate other controllers (like TopBarController)
                try {
                    return controllerClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new IllegalArgumentException("Unable to create controller: " + controllerClass.getName(), e);
                }
            });

            Parent root = loader.load();
            Scene scene = new Scene(root);

            primaryStage.setWidth(previousWidth);
            primaryStage.setHeight(previousHeight);

            double screenWidth = primaryStage.getWidth();
            double screenHeight = primaryStage.getHeight();

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
