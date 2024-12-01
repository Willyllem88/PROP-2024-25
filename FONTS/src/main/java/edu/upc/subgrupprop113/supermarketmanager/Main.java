package edu.upc.subgrupprop113.supermarketmanager;

import edu.upc.subgrupprop113.supermarketmanager.controllers.PresentationController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        PresentationController presentationController = new PresentationController(primaryStage);
        presentationController.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
