package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.controllers.components.ShelvingUnitController;
import edu.upc.subgrupprop113.supermarketmanager.factories.DomainControllerFactory;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
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
    private ScrollPane scrollPane; // ScrollPane para permitir scroll.

    @FXML
    private Button leftButton; // Botón Izquierda definido en el FXML.

    @FXML
    private Button rightButton; // Botón Derecha definido en el FXML.

    private List<Node> shelvingUnits = new ArrayList<>();
    private int visibleUnits = 3; // Número de estanterías visibles a la vez.
    private int currentIndex = 0; // Índice del primer elemento visible.

    public MainScreenController(PresentationController presentationController) {
        this.presentationController = presentationController;
    }

    @FXML
    private void initialize() {
        loadShelvingUnits();
        setupNavigationButtons();
    }

    private void loadShelvingUnits() {
        for (int i = 0; i < domainController.getShelvingUnits().size(); i++) { // Ejemplo: cargar 20 estanterías.
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

        updateVisibleUnits();
    }

    private void updateVisibleUnits() {
        shelvingUnitContainer.getChildren().clear();
        for (int i = 0; i < visibleUnits; i++) {
            int index = (currentIndex + i) % shelvingUnits.size();
            shelvingUnitContainer.getChildren().add(shelvingUnits.get(index));
        }

        // Sincronizar desplazamiento del ScrollPane.
        double offset = currentIndex * (1.0 / shelvingUnits.size());
        scrollPane.setHvalue(offset);
    }

    private void setupNavigationButtons() {
        // Configurar botón Izquierda.
        leftButton.setOnAction(e -> {
            moveShelvingUnitsLeft();
        });

        // Configurar botón Derecha.
        rightButton.setOnAction(e -> {
            moveShelvingUnitsRight();
        });
    }

    // Método para mover las estanterías a la izquierda
    private void moveShelvingUnitsLeft() {
        animateShelvingMovement(true);
    }

    // Método para mover las estanterías a la derecha
    private void moveShelvingUnitsRight() {
        animateShelvingMovement(false);
    }

    // Animar el movimiento de las estanterías
    private void animateShelvingMovement(boolean moveLeft) {
        // Desplazar el contenedor de estanterías a la izquierda o derecha
        double movement = moveLeft ? -1 : 1;

        // Animación para mover el contenedor entero
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), shelvingUnitContainer);
        transition.setToX(shelvingUnitContainer.getTranslateX() + movement * 200); // Desplazar 200px
        transition.play();

        // Actualizar las estanterías después de la animación
        transition.setOnFinished(e -> {
            if (moveLeft) {
                currentIndex = (currentIndex + 1) % shelvingUnits.size(); // Desplazar hacia la derecha
            } else {
                currentIndex = (currentIndex - 1 + shelvingUnits.size()) % shelvingUnits.size(); // Desplazar hacia la izquierda
            }
            updateVisibleUnits(); // Recargar las estanterías visibles después de la animación
        });
    }
}