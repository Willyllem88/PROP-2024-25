package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Screen;

import java.util.Objects;

public class PresentationController {

    private Stage primaryStage;

    private Scene currentScene;

    private static final String LOG_IN_VIEW = "fxml/logIn.fxml";
    private static final String SHELVING_UNIT_CONFIG_VIEW = "fxml/shelvingUnitConfig.fxml";
    private static final String CATALOG_VIEW = "fxml/catalog.fxml";
    private static final String MAIN_SCREEN_VIEW = "fxml/mainScreen.fxml";

    public PresentationController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void start() {
        loadView(MAIN_SCREEN_VIEW);
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

    private void loadView(String resource) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(resource));
            // Configura la fábrica de controladores
            loader.setControllerFactory(controllerClass -> {
                if (controllerClass == LogInController.class) {
                    return new LogInController(this);
                }
                else if (controllerClass == MainScreenController.class) {
                    return new MainScreenController(this);
                }
                /*if (controllerClass == ShelvingUnitConfigController.class) {
                    return new ShelvingUnitConfigController(this);
                }
                MORE CONTROLLERS HERE
                 */
                /*if (controllerClass == CatalogController.class) {
                    return new CatalogController(this);
                }*/
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
            // Obtener las dimensiones de la pantalla
            double screenWidth = Screen.getPrimary().getBounds().getWidth();
            double screenHeight = Screen.getPrimary().getBounds().getHeight();

            // Establecer el tamaño de la ventana en la mitad del ancho y alto de la pantalla
            primaryStage.setWidth((screenWidth / 2)*1.5);
            primaryStage.setHeight((screenHeight / 2)*1.5);
            primaryStage.setMinWidth((screenWidth / 2));
            primaryStage.setMinHeight((screenHeight / 2));

            // Centrar la ventana en la pantalla
            primaryStage.setX((screenWidth - primaryStage.getWidth()) / 2);
            primaryStage.setY((screenHeight - primaryStage.getHeight()) / 2);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
