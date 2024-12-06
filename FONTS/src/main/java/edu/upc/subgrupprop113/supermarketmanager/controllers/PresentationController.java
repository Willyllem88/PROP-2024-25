package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class PresentationController {

    private Stage primaryStage;

    private Scene currentScene;

    private static final String LOG_IN_VIEW = "fxml/logIn.fxml";
    private static final String SHELVING_UNIT_CONFIG_VIEW = "fxml/shelvingUnitConfig.fxml";
    private static final String CATALOG_VIEW = "fxml/catalog.fxml";

    public PresentationController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void start() {
        loadView(LOG_IN_VIEW);
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public void logInSuccessful() {
        // TODO: Navigate to the main view
    }

    public void navigateToCatalog() {
        loadView(CATALOG_VIEW);
    }

    public void logOut() {
        loadView(LOG_IN_VIEW);
    }

    private void loadView(String resource) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(resource));
            // Configura la fábrica de controladores
            loader.setControllerFactory(controllerClass -> {
                if (controllerClass == LogInController.class) {
                    return new LogInController(this);
                }
                /*if (controllerClass == ShelvingUnitConfigController.class) {
                    return new ShelvingUnitConfigController(this);
                }
                MORE CONTROLLERS HERE
                 */
                if (controllerClass == CatalogController.class) {
                    return new CatalogController(this);
                }
                // Fallback: instantiate other controllers (like TopBarController)
                try {
                    return controllerClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new IllegalArgumentException("Unable to create controller: " + controllerClass.getName(), e);
                }
            });

            Parent root = loader.load();
            currentScene = new Scene(root);
            currentScene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("css/global.css")).toExternalForm());
            primaryStage.setScene(currentScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
