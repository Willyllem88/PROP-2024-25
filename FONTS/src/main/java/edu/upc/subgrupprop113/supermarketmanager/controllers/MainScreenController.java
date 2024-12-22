package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.controllers.components.ShelvingUnitController;
import edu.upc.subgrupprop113.supermarketmanager.controllers.components.TopBarController;
import edu.upc.subgrupprop113.supermarketmanager.factories.DomainControllerFactory;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the main screen of the supermarket manager application.
 * Handles user interaction, shelving unit navigation, and layout updates.
 */
public class MainScreenController {

    private final PresentationController presentationController;
    private final DomainController domainController = DomainControllerFactory.getInstance().getDomainController();
    private TopBarController topBarController;

    public VBox leftButtonContainer;
    public VBox rightButtonContainer;

    @FXML
    private HBox topBar;

    @FXML
    private HBox shelvingUnitContainer;

    @FXML
    private FontIcon leftButton;

    @FXML
    private FontIcon rightButton;

    private final List<Node> shelvingUnits = new ArrayList<>();
    private static final int NB_DISPLAYED_UNITS = 3;
    private int currentIndex;

    /**
     * Constructor for MainScreenController.
     *
     * @param presentationController the controller managing navigation and presentation logic.
     */
    public MainScreenController(PresentationController presentationController) {
        this.presentationController = presentationController;
        currentIndex = 0;
    }

    public MainScreenController(PresentationController presentationController, int index) {
        this.presentationController = presentationController;
        currentIndex = index;
    }

    /**
     * Initializes the main screen components and their bindings.
     */
    @FXML
    private void initialize() {
        topBarController = (TopBarController) topBar.getProperties().get("controller");
        if (!domainController.loggedAdmin()) {
            topBarController.showSaveButton(false);
            topBarController.showSaveAsButton(false);
        }
        topBarController.showDistributionSettings(domainController.loggedAdmin());
        topBarController.showGoBackButton(false);
        topBarController.showNewDistributionButton(false);
        topBarController.showImportButton(false);
        leftButton.iconSizeProperty().bind(Bindings.createIntegerBinding(
                () -> (int) ((leftButtonContainer.getHeight() * 0.8 + leftButtonContainer.getWidth() * 0.2) * 0.15),
                leftButtonContainer.heightProperty(),
                leftButtonContainer.widthProperty()
        ));
        rightButton.iconSizeProperty().bind(Bindings.createIntegerBinding(
                () -> (int) ((leftButtonContainer.getHeight() * 0.8 + leftButtonContainer.getWidth() * 0.2) * 0.15),
                rightButtonContainer.heightProperty(),
                rightButtonContainer.widthProperty()
        ));
        reloadShelvingUnits();
        topBarController.setOnImportHandler(_ -> reloadShelvingUnits());
    }

    /**
     * Reloads the shelving units and resets the current index to zero.
     */
    private void reloadShelvingUnits() {
        shelvingUnits.clear();
        loadShelvingUnits();
        updateVisibleUnits();
    }

    /**
     * Loads shelving unit components into the shelvingUnits list.
     */
    private void loadShelvingUnits() {
        shelvingUnits.clear();
        for (int i = 0; i < domainController.getShelvingUnits().size(); i++) {
            final int index = i;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/edu/upc/subgrupprop113/supermarketmanager/fxml/components/shelvingUnit.fxml"));
                loader.setController(new ShelvingUnitController(presentationController, index));

                HBox shelvingUnit = loader.load();
                shelvingUnits.add(shelvingUnit);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to load Shelving Unit Component", e);
            }
        }

        shelvingUnitContainer.getChildren().addAll(shelvingUnits);
    }

    /**
     * Updates the visible shelving units on the screen based on the current index.
     */
    private void updateVisibleUnits() {
        shelvingUnitContainer.getChildren().clear();
        int showingUnits = Math.min(NB_DISPLAYED_UNITS, shelvingUnits.size());

        for (int i = 0; i < showingUnits; i++) {
            int index = (currentIndex + i) % shelvingUnits.size();
            shelvingUnitContainer.getChildren().add(shelvingUnits.get(index));
        }
    }

    /**
     * Moves the shelving unit view to the right, updating the current index.
     */
    @FXML
    private void moveShelvingUnitsRight() {
        if (shelvingUnits.size() <= NB_DISPLAYED_UNITS) return;

        currentIndex = (currentIndex + 1) % shelvingUnits.size();

        updateVisibleUnits();
    }

    /**
     * Moves the shelving unit view to the left, updating the current index.
     */
    @FXML
    private void moveShelvingUnitsLeft() {
        if (shelvingUnits.size() <= NB_DISPLAYED_UNITS) return;

        currentIndex = (currentIndex - 1 + shelvingUnits.size()) % shelvingUnits.size();

        updateVisibleUnits();
    }
}
