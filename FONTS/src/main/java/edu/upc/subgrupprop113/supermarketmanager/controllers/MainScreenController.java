package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.controllers.components.ShelvingUnitController;
import edu.upc.subgrupprop113.supermarketmanager.controllers.components.TopBarController;
import edu.upc.subgrupprop113.supermarketmanager.factories.DomainControllerFactory;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;
import java.util.List;

public class MainScreenController {

    private final PresentationController presentationController;
    private final DomainController domainController = DomainControllerFactory.getInstance().getDomainController();
    private TopBarController topBarController;

    public VBox left_b;
    public VBox right_b;

    @FXML
    private HBox topBar;

    @FXML
    private HBox shelvingUnitContainer;

    @FXML
    private FontIcon leftButton;

    @FXML
    private FontIcon rightButton;

    private final List<Node> shelvingUnits = new ArrayList<>();
    private final int visibleUnits;
    private int currentIndex;
    private final int shelvingUnitWidth;

    public MainScreenController(PresentationController presentationController) {
        this.presentationController = presentationController;
        visibleUnits = 3;
        currentIndex = 0;
        shelvingUnitWidth = 200;
    }

    @FXML
    private void initialize() {
        topBarController = (TopBarController) topBar.getProperties().get("controller");
        leftButton.iconSizeProperty().bind(Bindings.createIntegerBinding(
                () -> (int) (((left_b.getHeight()*0.8 + left_b.getWidth()*0.2)) * 0.15),
                left_b.heightProperty(),
                left_b.widthProperty()
        ));
        rightButton.iconSizeProperty().bind(Bindings.createIntegerBinding(
                () -> (int) (((left_b.getHeight()*0.8 + left_b.getWidth()*0.2)) * 0.15),
                right_b.heightProperty(),
                right_b.widthProperty()
        ));
        reloadShelvingUnits();
        topBarController.setOnImportHandler(_ -> reloadShelvingUnits());
    }

    private void reloadShelvingUnits() {
        currentIndex = 0;
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

    private void updateVisibleUnits() {
        shelvingUnitContainer.getChildren().clear();

        for (int i = 0; i < visibleUnits; i++) {
            int index = (currentIndex + i) % shelvingUnits.size();
            //TODO: Donar un identificador als fills valis pq si no peta
            shelvingUnitContainer.getChildren().add(shelvingUnits.get(index));
        }
    }

    public void moveShelvingUnitsRight(MouseEvent mouseEvent) {
        if (shelvingUnits.size() <= visibleUnits) return;

        currentIndex = (currentIndex + 1) % shelvingUnits.size();

        updateVisibleUnits();
    }

    public void moveShelvingUnitsLeft(MouseEvent mouseEvent) {
        if (shelvingUnits.size() <= visibleUnits) return;

        currentIndex = (currentIndex - 1 + shelvingUnits.size()) % shelvingUnits.size();

        updateVisibleUnits();
    }
}
