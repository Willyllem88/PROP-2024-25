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
                /*if (controllerClass == ShelvingUnitConfigController.class) {
                    return new ShelvingUnitConfigController(this);
                }
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

    public String showFileChooserDialog(String title, String initialPath) {
        return showFileChooserDialog(title, initialPath, "All Files", "*");
    }

    public String showFileChooserDialog(String title, String initialPath, String formatMessage, String extension) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);


        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(formatMessage, extension)
        );

        File initialDirectory = new File(initialPath);
        // Verify that the directory exists and is a folder
        if (initialDirectory.exists() && initialDirectory.isDirectory()) {
            fileChooser.setInitialDirectory(initialDirectory);
        }
        else {
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        }

        // Show the open file dialog
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) return null;
        return selectedFile.getAbsolutePath();
    }

}
