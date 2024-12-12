package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.controllers.components.*;
import edu.upc.subgrupprop113.supermarketmanager.dtos.ProductDto;
import edu.upc.subgrupprop113.supermarketmanager.factories.DomainControllerFactory;
import edu.upc.subgrupprop113.supermarketmanager.models.ProductTemperature;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.feather.Feather;
import edu.upc.subgrupprop113.supermarketmanager.controllers.components.PrimaryButtonController;
import javafx.geometry.Point2D;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class EditDistributionScreenController {
    private final PresentationController presentationController;
    private final DomainController domainController = DomainControllerFactory.getInstance().getDomainController();
    private TopBarController topBarController;

    public VBox leftButtonContainer;
    public VBox rightButtonContainer;

    @FXML
    private VBox primaryButton1;
    @FXML
    private VBox primaryButton2;

    @FXML
    private HBox topBar;

    @FXML
    private HBox shelvingUnitContainer;

    @FXML
    private FontIcon leftButton;

    @FXML
    private FontIcon rightButton;
    private boolean order_clicked = false;
    private final List<Node> shelvingUnits = new ArrayList<>();
    private final int visibleUnits;
    private int currentIndex;
    private final int shelvingUnitWidth;

    public EditDistributionScreenController(PresentationController presentationController) {
        this.presentationController = presentationController;
        visibleUnits = 3;
        currentIndex = 0;
        shelvingUnitWidth = 200;
    }

    @FXML
    private void initialize() {
        PrimaryButtonController primaryButtonController1 = (PrimaryButtonController) primaryButton1.getProperties().get("controller");
        PrimaryButtonController primaryButtonController2 = (PrimaryButtonController) primaryButton2.getProperties().get("controller");
        topBarController = (TopBarController) topBar.getProperties().get("controller");
        leftButton.iconSizeProperty().bind(Bindings.createIntegerBinding(
                () -> (int) ((leftButtonContainer.getHeight()*0.8 + leftButtonContainer.getWidth()*0.2) * 0.15),
                leftButtonContainer.heightProperty(),
                leftButtonContainer.widthProperty()
        ));
        rightButton.iconSizeProperty().bind(Bindings.createIntegerBinding(
                () -> (int) ((leftButtonContainer.getHeight()*0.8 + leftButtonContainer.getWidth()*0.2) * 0.15),
                rightButtonContainer.heightProperty(),
                rightButtonContainer.widthProperty()
        ));
        reloadShelvingUnits();
        topBarController.setOnImportHandler(_ -> reloadShelvingUnits());
        if (primaryButtonController1 != null) {
            primaryButtonController1.setLabelText("Order");
            primaryButtonController1.setOnClickHandler(_ -> handleOrder());
        }
        if (primaryButtonController2 != null) {
            primaryButtonController2.setLabelText("Swap");
            primaryButtonController2.setOnClickHandler(_ -> handleSwap());
        }
        initializeMouseMovementDetection();
    }

    private ContextMenu contextMenu;

    @FXML
    private void handleOrder() {
            if (contextMenu != null && contextMenu.isShowing()) {
                contextMenu.hide();
            }
            else {
                // Crear un nuevo ContextMenu solo si no existe
                contextMenu = new ContextMenu();

                // Opción para cerrar la aplicación
                MenuItem backtrackingItem = new MenuItem("Backtracking");
                backtrackingItem.setOnAction(_ -> handleBacktracking());
                MenuItem approximationItem = new MenuItem("Approximation");
                approximationItem.setOnAction(_ -> handleApproximation());
                MenuItem greedyItem = new MenuItem("Greedy");
                greedyItem.setOnAction(_ -> handleGreedy());

                contextMenu.getItems().add(backtrackingItem);
                contextMenu.getItems().add(approximationItem);
                contextMenu.getItems().add(greedyItem);

                // Obtener las coordenadas de la esquina superior izquierda del botón en la pantalla
                Point2D screenPosition = primaryButton1.localToScreen(primaryButton1.getBoundsInLocal().getMinX(), primaryButton1.getBoundsInLocal().getMinY());

                // Obtener las coordenadas X e Y
                double x = screenPosition.getX();
                double y = screenPosition.getY();

                // Ajustar la posición Y para que el menú se muestre encima del botón
                double offsetY = -primaryButton1.getHeight()*1.5; // Desplazamos el menú hacia arriba 100 píxeles (ajustar según sea necesario)
                double adjustedY = y + offsetY;  // Sumar el desplazamiento a la posición Y

                // Mostrar el menú en la posición ajustada
                contextMenu.show(primaryButton1, x, adjustedY);
            }
    }

    private void handleBacktracking() {
        System.out.println("Backtracking");
        domainController.sortSupermarketProducts("BruteForce");
        reloadShelvingUnitsStatic();
    }

    private void handleApproximation() {
        System.out.println("Approximation");
        domainController.sortSupermarketProducts("Approximation");
        reloadShelvingUnitsStatic();
    }

    private void handleGreedy() {
        System.out.println("Greedy");
        domainController.sortSupermarketProducts("Greedy");
        reloadShelvingUnitsStatic();
    }

    @FXML
    private void handleSwap() {
        System.out.println("Swap");
    }

    private void reloadShelvingUnits() {
        currentIndex = 0;
        shelvingUnits.clear();
        loadShelvingUnits();
        updateVisibleUnits();
    }

    private void reloadShelvingUnitsStatic() {
        shelvingUnits.clear();
        loadShelvingUnits();
        updateVisibleUnits();
    }

    private void loadShelvingUnits() {
        for (int i = 0; i < domainController.getShelvingUnits().size(); i++) {
            final int index = i;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/edu/upc/subgrupprop113/supermarketmanager/fxml/components/shelvingUnit.fxml"));
                loader.setControllerFactory(controllerClass -> {
                    if (controllerClass == ShelvingUnitController.class) {
                        return new ShelvingUnitController(presentationController, index);
                    }
                    throw new IllegalArgumentException("Unexpected controller: " + controllerClass);
                });

                VBox shelvingUnit = loader.load();
                shelvingUnits.add(shelvingUnit);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to load Shelving Unit Component", e);
            }
        }

        shelvingUnitContainer.getChildren().addAll(shelvingUnits);
    }

    private void moveShelvingUnits(boolean moveRight) {
        if (shelvingUnits.size() <= visibleUnits) return;
        currentIndex = moveRight
                ? (currentIndex + 1) % shelvingUnits.size()
                : (currentIndex - 1 + shelvingUnits.size()) % shelvingUnits.size();

        updateVisibleUnits();
    }

    private final List<FontIcon> plusIcons = new ArrayList<>();

    private void addPlusIconWithProximityBehavior(HBox shelvingUnitContainer, int index) {
        // Crear el contenedor del ícono
        StackPane iconWrapper = new StackPane();
        iconWrapper.setMinSize(50, 50); // Tamaño mínimo del contenedor (ajustar según diseño)
        iconWrapper.setStyle("-fx-background-color: transparent;"); // Hacerlo transparente

        // Crear el ícono de "plus"
        FontIcon plusIcon = new FontIcon(Feather.PLUS_CIRCLE);
        plusIcon.getStyleClass().add("responsive-icon");
        plusIcon.iconSizeProperty().bind(Bindings.createIntegerBinding(
                () -> (int) ((shelvingUnitContainer.getHeight() * 0.2 + shelvingUnitContainer.getWidth() * 0.2) * 0.15),
                shelvingUnitContainer.heightProperty(),
                shelvingUnitContainer.widthProperty()
        ));
        plusIcon.setUserData(index);
        plusIcon.setVisible(false); // Inicialmente oculto

        // Agregar el ícono al contenedor
        iconWrapper.getChildren().add(plusIcon);
        shelvingUnitContainer.getChildren().add(iconWrapper);

        // Guardar la referencia para calcular proximidad después
        plusIcons.add(plusIcon);

        // Asignar acción al clic en el ícono
        plusIcon.setOnMouseClicked(event -> {
            Integer clickedIndex = (Integer) plusIcon.getUserData();
            handleAddIconClick(clickedIndex);
        });
    }

    // Detectar movimiento del mouse en el contenedor
    @FXML
    private void initializeMouseMovementDetection() {
        shelvingUnitContainer.setOnMouseMoved(event -> {
            // Obtener las coordenadas del mouse en la escena
            double mouseX = event.getSceneX();
            double mouseY = event.getSceneY();

            for (FontIcon plusIcon : plusIcons) {
                // Obtener la posición del ícono en la escena
                Node iconWrapper = (Node) plusIcon.getParent(); // Icon está dentro de un StackPane
                Point2D iconPositionInScene = iconWrapper.localToScene(iconWrapper.getBoundsInLocal().getWidth() / 2,
                        iconWrapper.getBoundsInLocal().getHeight() / 2);

                // Calcular la distancia entre el mouse y el ícono
                double distance = calculateDistance(mouseX, mouseY, iconPositionInScene.getX(), iconPositionInScene.getY());
                double screenWidth = Screen.getPrimary().getBounds().getWidth();
                double screenHeight = Screen.getPrimary().getBounds().getHeight();
                Stage stage = (Stage) shelvingUnitContainer.getScene().getWindow();

                double currentWidth = stage.getWidth();
                double currentHeight = stage.getHeight();
                // Mostrar u ocultar el ícono basado en la distancia
                plusIcon.setVisible(distance < (400 * ((currentHeight*currentWidth)/(screenHeight*screenWidth))) ); // Visible si está dentro de 50 píxeles
            }
        });
    }


    private double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }



    private void updateVisibleUnits() {
        shelvingUnitContainer.getChildren().clear();
        int showingUnits = Math.min(visibleUnits, shelvingUnits.size());

        addPlusIconWithProximityBehavior(shelvingUnitContainer, (currentIndex)%shelvingUnits.size());

        for (int i = 0; i < showingUnits; i++) {
            int index = (currentIndex + i) % shelvingUnits.size();
            VBox shelvingUnitWithIcons = new VBox();

            // Agregar la unidad de estantería
            shelvingUnitWithIcons.getChildren().add(shelvingUnits.get(index));

            // Agregar los íconos debajo de la estantería
            HBox iconsContainer = new HBox();
            iconsContainer.getStyleClass().add("container-icons");
            FontIcon editIcon = new FontIcon(Feather.EDIT_2);
            editIcon.getStyleClass().add("responsive-icon-1");
            editIcon.setIconSize(36);

            FontIcon trashIcon = new FontIcon(Feather.TRASH_2);
            trashIcon.getStyleClass().add("responsive-icon-2");
            trashIcon.setIconSize(36);
            trashIcon.setUserData(index);

            // Asignar el manejador de eventos para el clic en el trashIcon
            trashIcon.setOnMouseClicked(event -> {
                Integer clickedIndex = (Integer) trashIcon.getUserData();  // Obtener el índice asociado al icono
                handleTrashIconClick(clickedIndex);  // Llamar a la función con el índice
            });


            iconsContainer.getChildren().addAll(editIcon, trashIcon);
            shelvingUnitWithIcons.getChildren().add(iconsContainer);

            shelvingUnitContainer.getChildren().add(shelvingUnitWithIcons);

            // Añadir un ícono entre las estanterías excepto después de la última
            if (i < showingUnits - 1) {
                addPlusIconWithProximityBehavior(shelvingUnitContainer, (index + 1) % shelvingUnits.size());
            }
        }
        addPlusIconWithProximityBehavior(shelvingUnitContainer, (currentIndex + showingUnits) % shelvingUnits.size());
    }

    public void moveShelvingUnitsRight() {
        if (shelvingUnits.size() <= visibleUnits) return;

        currentIndex = (currentIndex + 1) % shelvingUnits.size();

        updateVisibleUnits();
    }

    public void moveShelvingUnitsLeft() {
        if (shelvingUnits.size() <= visibleUnits) return;

        currentIndex = (currentIndex - 1 + shelvingUnits.size()) % shelvingUnits.size();

        updateVisibleUnits();
    }

    public boolean hasProducts(Integer index) {
        for(ProductDto x : domainController.getShelvingUnit(index).getProducts()) {
            if(x != null) return true;
        }
        return false;
    }

    public void handleTrashIconClick(Integer clickedIndex) {
        if (hasProducts(clickedIndex)) {
            // Crear el cuadro de diálogo de confirmación
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmación de eliminación");
            confirmationAlert.setHeaderText("¿Estás seguro de que deseas eliminar esta unidad de estantería?");
            confirmationAlert.setContentText("Esta acción no se puede deshacer.");

            // Mostrar el diálogo y esperar la respuesta
            ButtonType result = confirmationAlert.showAndWait().orElse(ButtonType.CANCEL);

            if (result == ButtonType.OK) {
                // Si el usuario confirma, eliminar la unidad de estantería
                domainController.emptyShelvingUnit(clickedIndex);
                domainController.removeShelvingUnit(clickedIndex);
                reloadShelvingUnits(); // Recargar las unidades de estantería
            } else {
                // Si el usuario cancela, no hacer nada
                System.out.println("Eliminación cancelada.");
            }
        } else {
            // Aquí puedes manejar el caso en el que la unidad no tenga productos
            domainController.removeShelvingUnit(clickedIndex);
            reloadShelvingUnits();
            System.out.println("La unidad de estantería está vacía.");
        }
    }

    public void handleAddIconClick(Integer clickedIndex) {
        try {
            // Cargar el archivo FXML y el controlador asociado
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/upc/subgrupprop113/supermarketmanager/fxml/components/setTemperature.fxml"));
            Pane dialogContent = loader.load();

            // Obtener el controlador del componente
            SetTemperatureController setTemperatureController = loader.getController();

            // Crear el diálogo
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Establecer Temperatura");
            dialog.getDialogPane().setContent(dialogContent);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            // Mostrar el diálogo y esperar la respuesta del usuario
            dialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Obtener la temperatura seleccionada
                    String selectedTemperature = setTemperatureController.getTemperature();
                    System.out.println("Temperatura seleccionada: " + selectedTemperature);

                    // Aquí puedes realizar la lógica para configurar la temperatura
                    domainController.addShelvingUnit(clickedIndex, selectedTemperature);
                    reloadShelvingUnits(); // Recargar las unidades de estantería
                } else {
                    System.out.println("Operación cancelada.");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
