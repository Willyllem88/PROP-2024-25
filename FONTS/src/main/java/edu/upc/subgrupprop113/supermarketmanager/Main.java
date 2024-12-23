package edu.upc.subgrupprop113.supermarketmanager;

import edu.upc.subgrupprop113.supermarketmanager.controllers.PresentationController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    /**
     * Start method to initialize the application
     * @param primaryStage The primary stage of the application
     * @throws Exception If an error occurs during the initialization
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        PresentationController presentationController = new PresentationController(primaryStage);
        presentationController.start();
    }

    /**
     * Main method to launch the application
     * @param args The arguments passed to the application
     */
    public static void main(String[] args) {
        launch(args);
    }
}
