package edu.upc.subgrupprop113.supermarketmanager.controllers.components;

import edu.upc.subgrupprop113.supermarketmanager.controllers.DomainController;
import edu.upc.subgrupprop113.supermarketmanager.controllers.PresentationController;
import edu.upc.subgrupprop113.supermarketmanager.factories.DomainControllerFactory;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.io.InputStream;
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

    @FXML
    private void showImportMenu(MouseEvent event) {
        if (domainController.hasChangesMade()) {
            errorLabelController.setErrorMsg("There are unsaved changes!\nPlease save them.", 4500);
            return;
        }

        String title = "Select File to Import the new Supermarket";
        //TODO: fer utils per gestionar paths
        String initialPath;
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("nix") || os.contains("nux") || os.contains("aix"))
            initialPath = "FONTS/src/main/resources/edu/upc/subgrupprop113/supermarketmanager/dataExamples";
        else
            initialPath = "FONTS\\src\\main\\resources\\edu\\upc\\subgrupprop113\\supermarketmanager\\dataExamples";

        String selectedFilePath = presentationController.showFileChooserDialog(title, initialPath, "JSON Files", "*.json");

        // If a file is selected, print its path
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

       /* if (hasProducts(clickedIndex)) {
            // Crear el cuadro de diálogo de confirmación
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmación de eliminación");
            confirmationAlert.setHeaderText("¿Estás seguro de que deseas eliminar esta unidad de estantería?");
            confirmationAlert.setContentText("Esta acción no se puede deshacer.");

            // Mostrar el diálogo y esperar la respuesta
            ButtonType result = confirmationAlert.showAndWait().orElse(ButtonType.CANCEL);

            if (result == ButtonType.OK) {
                // Crear un nuevo Stage para el popup
                Stage popupStage = new Stage();
                popupStage.initModality(Modality.APPLICATION_MODAL); // Bloquea la interacción con la ventana principal
                popupStage.setTitle("New Distribution Settings");

                // Crear las imágenes para cada temperatura
                Image frozenImage = new Image(getClass().getResource("/edu/upc/subgrupprop113/supermarketmanager/assets/temperatureIcons/FROZEN.png").toExternalForm());
                Image refrigeratedImage = new Image(getClass().getResource("/edu/upc/subgrupprop113/supermarketmanager/assets/temperatureIcons/REFRIGERATED.png").toExternalForm());
                Image ambientImage = new Image(getClass().getResource("/edu/upc/subgrupprop113/supermarketmanager/assets/temperatureIcons/AMBIENT.png").toExternalForm());

                ImageView frozenIcon = new ImageView(frozenImage);
                ImageView refrigeratedIcon = new ImageView(refrigeratedImage);
                ImageView ambientIcon = new ImageView(ambientImage);

                // Ajustar tamaños de los íconos
                frozenIcon.setFitWidth(30);
                frozenIcon.setFitHeight(30);
                refrigeratedIcon.setFitWidth(30);
                refrigeratedIcon.setFitHeight(30);
                ambientIcon.setFitWidth(30);
                ambientIcon.setFitHeight(30);

                // Crear Spinners para cada tipo de temperatura
                Spinner<Integer> frozenSpinner = new Spinner<>(0, 100, 0); // Rango: -30 a 0, valor inicial: -18
                Spinner<Integer> refrigeratedSpinner = new Spinner<>(0, 100, 0); // Rango: 0 a 10, valor inicial: 4
                Spinner<Integer> ambientSpinner = new Spinner<>(0, 100, 0); // Rango: 10 a 25, valor inicial: 20

                // Crear Spinner para la altura de las estanterías
                Spinner<Integer> heightSpinner = new Spinner<>(1, 10, 1); // Rango: 1 a 5, valor inicial: 3

                // Crear etiquetas descriptivas
                Label heightLabel = new Label("Height of shelving units:");

                // Crear un botón para confirmar los valores
                Button setButton = new Button("SET");
                setButton.setOnAction(e -> {
                    int frozenValue = frozenSpinner.getValue();
                    int refrigeratedValue = refrigeratedSpinner.getValue();
                    int ambientValue = ambientSpinner.getValue();
                    int heightValue = heightSpinner.getValue();

                    // Aquí puedes procesar los valores seleccionados
                    System.out.println("Frozen: " + frozenValue);
                    System.out.println("Refrigerated: " + refrigeratedValue);
                    System.out.println("Ambient: " + ambientValue);
                    System.out.println("Height: " + heightValue);

                    popupStage.close(); // Cerrar el popup
                });

                // Crear layouts para organizar los elementos
                HBox frozenBox = new HBox(10, frozenIcon, frozenSpinner);
                HBox refrigeratedBox = new HBox(10, refrigeratedIcon, refrigeratedSpinner);
                HBox ambientBox = new HBox(10, ambientIcon, ambientSpinner);
                HBox heightBox = new HBox(10, heightLabel, heightSpinner);

                frozenBox.setAlignment(Pos.CENTER);
                refrigeratedBox.setAlignment(Pos.CENTER);
                ambientBox.setAlignment(Pos.CENTER);
                heightBox.setAlignment(Pos.CENTER);

                VBox mainLayout = new VBox(15, frozenBox, refrigeratedBox, ambientBox, heightBox, setButton);
                mainLayout.setPadding(new Insets(20));
                mainLayout.setAlignment(Pos.CENTER);

                // Crear la escena y mostrar el popup
                Scene scene = new Scene(mainLayout);
                popupStage.setScene(scene);
                popupStage.showAndWait(); // Mostrar y esperar a que se cierre
            } else {
                // Si el usuario cancela, no hacer nada
                System.out.println("Eliminación cancelada.");
            }
        } else {
            // Crear un nuevo Stage para el popup
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL); // Bloquea la interacción con la ventana principal
            popupStage.setTitle("New Distribution Settings");

            // Crear las imágenes para cada temperatura
            Image frozenImage = new Image(getClass().getResource("/edu/upc/subgrupprop113/supermarketmanager/assets/temperatureIcons/FROZEN.png").toExternalForm());
            Image refrigeratedImage = new Image(getClass().getResource("/edu/upc/subgrupprop113/supermarketmanager/assets/temperatureIcons/REFRIGERATED.png").toExternalForm());
            Image ambientImage = new Image(getClass().getResource("/edu/upc/subgrupprop113/supermarketmanager/assets/temperatureIcons/AMBIENT.png").toExternalForm());

            ImageView frozenIcon = new ImageView(frozenImage);
            ImageView refrigeratedIcon = new ImageView(refrigeratedImage);
            ImageView ambientIcon = new ImageView(ambientImage);

            // Ajustar tamaños de los íconos
            frozenIcon.setFitWidth(30);
            frozenIcon.setFitHeight(30);
            refrigeratedIcon.setFitWidth(30);
            refrigeratedIcon.setFitHeight(30);
            ambientIcon.setFitWidth(30);
            ambientIcon.setFitHeight(30);

            // Crear Spinners para cada tipo de temperatura
            Spinner<Integer> frozenSpinner = new Spinner<>(0, 100, 0); // Rango: -30 a 0, valor inicial: -18
            Spinner<Integer> refrigeratedSpinner = new Spinner<>(0, 100, 0); // Rango: 0 a 10, valor inicial: 4
            Spinner<Integer> ambientSpinner = new Spinner<>(0, 100, 0); // Rango: 10 a 25, valor inicial: 20

            // Crear Spinner para la altura de las estanterías
            Spinner<Integer> heightSpinner = new Spinner<>(1, 10, 1); // Rango: 1 a 5, valor inicial: 3

            // Crear etiquetas descriptivas
            Label heightLabel = new Label("Height of shelving units:");

            // Crear un botón para confirmar los valores
            Button setButton = new Button("SET");
            setButton.setOnAction(e -> {
                int frozenValue = frozenSpinner.getValue();
                int refrigeratedValue = refrigeratedSpinner.getValue();
                int ambientValue = ambientSpinner.getValue();
                int heightValue = heightSpinner.getValue();

                // Aquí puedes procesar los valores seleccionados
                System.out.println("Frozen: " + frozenValue);
                System.out.println("Refrigerated: " + refrigeratedValue);
                System.out.println("Ambient: " + ambientValue);
                System.out.println("Height: " + heightValue);

                popupStage.close(); // Cerrar el popup
            });

            // Crear layouts para organizar los elementos
            HBox frozenBox = new HBox(10, frozenIcon, frozenSpinner);
            HBox refrigeratedBox = new HBox(10, refrigeratedIcon, refrigeratedSpinner);
            HBox ambientBox = new HBox(10, ambientIcon, ambientSpinner);
            HBox heightBox = new HBox(10, heightLabel, heightSpinner);

            frozenBox.setAlignment(Pos.CENTER);
            refrigeratedBox.setAlignment(Pos.CENTER);
            ambientBox.setAlignment(Pos.CENTER);
            heightBox.setAlignment(Pos.CENTER);

            VBox mainLayout = new VBox(15, frozenBox, refrigeratedBox, ambientBox, heightBox, setButton);
            mainLayout.setPadding(new Insets(20));
            mainLayout.setAlignment(Pos.CENTER);

            // Crear la escena y mostrar el popup
            Scene scene = new Scene(mainLayout);
            popupStage.setScene(scene);
            popupStage.showAndWait(); // Mostrar y esperar a que se cierre
        }*/
        System.out.println("Default New Distribution Handler");
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

    public void setOnImportHandler(Consumer<Void> handler) {this.onImportHandler = handler;}

    public void setOnGoBackHandler(Consumer<Void> handler) {
        this.onGoBackHandler = handler;
    }

}
