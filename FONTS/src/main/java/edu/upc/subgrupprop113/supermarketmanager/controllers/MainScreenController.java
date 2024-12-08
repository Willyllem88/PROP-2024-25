package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.controllers.components.ShelvingUnitController;
import edu.upc.subgrupprop113.supermarketmanager.factories.DomainControllerFactory;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class MainScreenController {

    private final PresentationController presentationController;
    private final DomainController domainController = DomainControllerFactory.getInstance().getDomainController();

    @FXML
    private HBox shelvingUnitContainer; // Contenedor para los ShelvingUnits.

    @FXML
    private Button leftButton; // Botón Izquierda definido en el FXML.

    @FXML
    private Button rightButton; // Botón Derecha definido en el FXML.

    private final List<Node> shelvingUnits = new ArrayList<>();
    private final int visibleUnits = 3; // Número de estanterías visibles a la vez.
    private int currentIndex = 0; // Índice del primer elemento visible.
    private final int shelvingUnitWidth = 200; // Ancho de cada estantería en píxeles.

    public MainScreenController(PresentationController presentationController) {
        this.presentationController = presentationController;
    }

    @FXML
    private void initialize() {
        loadShelvingUnits();
        setupNavigationButtons();
        updateVisibleUnits(); // Inicializar las estanterías visibles.
    }

    private void loadShelvingUnits() {
        for (int i = 0; i < domainController.getShelvingUnits().size(); i++) {
            final int index = i; // Declarar como final o efectivamente final.
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/edu/upc/subgrupprop113/supermarketmanager/fxml/components/shelvingUnit.fxml"));
                loader.setControllerFactory(controllerClass -> {
                    if (controllerClass == ShelvingUnitController.class) {
                        return new ShelvingUnitController(presentationController, index);
                    }
                    throw new IllegalArgumentException("Unexpected controller: " + controllerClass);
                });

                VBox shelvingUnit = loader.load(); // Cargar el componente
                shelvingUnits.add(shelvingUnit);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to load Shelving Unit Component", e);
            }
        }

        // Agregar todas las estanterías al contenedor.
        shelvingUnitContainer.getChildren().addAll(shelvingUnits.subList(0, Math.min(visibleUnits, shelvingUnits.size())));
    }

    private void setupNavigationButtons() {
        // Configurar botón Izquierda.
        leftButton.setOnAction(e -> moveShelvingUnits(false));

        // Configurar botón Derecha.
        rightButton.setOnAction(e -> moveShelvingUnits(true));
    }

    private void moveShelvingUnits(boolean moveRight) {
        if (shelvingUnits.size() <= visibleUnits) return; // No hay suficiente para desplazarse.

        int direction = moveRight ? 1 : -1;

        // Determinar el índice de la nueva estantería a agregar
        int newIndex = moveRight
                ? (currentIndex + visibleUnits) % shelvingUnits.size()
                : (currentIndex - 1 + shelvingUnits.size()) % shelvingUnits.size();

        // Crear la nueva estantería y agregarla al contenedor en la posición correspondiente
        Node newShelvingUnit = shelvingUnits.get(newIndex);
        if (moveRight) {
            shelvingUnitContainer.getChildren().add(newShelvingUnit);
        } else {
            shelvingUnitContainer.getChildren().add(0, newShelvingUnit);
        }

        // Animar todas las estanterías visibles más la nueva
        List<Node> allUnits = new ArrayList<>(shelvingUnitContainer.getChildren());
        for (Node shelvingUnit : allUnits) {
            TranslateTransition transition = new TranslateTransition(Duration.millis(300), shelvingUnit);
            transition.setByX(-direction * shelvingUnitWidth);
            transition.setOnFinished(event -> {
                if (shelvingUnit == allUnits.get(allUnits.size() - 1)) {
                    // Actualizar el contenedor después de la animación
                    updateShelvingUnitContainer(moveRight);
                }
            });
            transition.play();
        }
    }

    private void updateShelvingUnitContainer(boolean moveRight) {
        // Actualizar el índice actual
        currentIndex = moveRight
                ? (currentIndex + 1) % shelvingUnits.size()
                : (currentIndex - 1 + shelvingUnits.size()) % shelvingUnits.size();

        // Eliminar la estantería que ha salido de la vista
        if (moveRight) {
            shelvingUnitContainer.getChildren().remove(0);
        } else {
            shelvingUnitContainer.getChildren().remove(shelvingUnitContainer.getChildren().size() - 1);
        }

        // Reiniciar la posición de TranslateX de las estanterías visibles
        for (Node shelvingUnit : shelvingUnitContainer.getChildren()) {
            shelvingUnit.setTranslateX(0);
        }
    }

    private void updateVisibleUnits() {
        shelvingUnitContainer.getChildren().clear();

        // Mostrar solo las estanterías visibles basadas en el índice actual.
        for (int i = 0; i < visibleUnits; i++) {
            int index = (currentIndex + i) % shelvingUnits.size();
            shelvingUnitContainer.getChildren().add(shelvingUnits.get(index));
        }
    }
}
