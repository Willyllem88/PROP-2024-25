package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PresentationController {

    private Stage primaryStage;

    private static final String LOG_IN_VIEW = "fxml/logIn.fxml";
    private static final String SHELVING_UNIT_CONFIG_VIEW = "fxml/shelvingUnitConfig.fxml";

    public PresentationController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void start() {
        loadView(LOG_IN_VIEW);
    }

    public void logInSuccessful() {
        // TODO: Navigate to the main view
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
                else {
                    throw new IllegalArgumentException("Controlador desconocido: " + controllerClass);
                }
            });

            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
