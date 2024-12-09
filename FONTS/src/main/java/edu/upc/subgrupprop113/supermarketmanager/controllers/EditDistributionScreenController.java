package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.controllers.components.PrimaryButtonController;
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
import org.kordamp.ikonli.feather.Feather;
import edu.upc.subgrupprop113.supermarketmanager.controllers.components.ErrorLabelController;
import edu.upc.subgrupprop113.supermarketmanager.controllers.components.PrimaryButtonController;

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
    }

    @FXML
    private void handleOrder() {
        System.out.println("Order");
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
        int showingUnits = Math.min(visibleUnits, shelvingUnits.size());

        // Agregar un ícono antes de la primera estantería visible
        FontIcon beforeIcon = new FontIcon(Feather.PLUS_CIRCLE);
        beforeIcon.getStyleClass().add("responsive-icon");
        beforeIcon.setIconSize(48);
        shelvingUnitContainer.getChildren().add(beforeIcon);

        for (int i = 0; i < showingUnits; i++) {
            int index = (currentIndex + i) % shelvingUnits.size();
            VBox shelvingUnitWithIcons = new VBox();

            // Agregar la unidad de estantería
            shelvingUnitWithIcons.getChildren().add(shelvingUnits.get(index));

            // Agregar los íconos debajo de la estantería
            HBox iconsContainer = new HBox();
            iconsContainer.getStyleClass().add("container-icons");
            FontIcon editIcon = new FontIcon(Feather.EDIT_2);
            editIcon.getStyleClass().add("responsive-icon");
            editIcon.setIconSize(24);

            FontIcon trashIcon = new FontIcon(Feather.TRASH_2);
            trashIcon.getStyleClass().add("responsive-icon");
            trashIcon.setIconSize(24);

            iconsContainer.getChildren().addAll(editIcon, trashIcon);
            shelvingUnitWithIcons.getChildren().add(iconsContainer);

            shelvingUnitContainer.getChildren().add(shelvingUnitWithIcons);

            // Añadir un ícono entre las estanterías excepto después de la última
            if (i < showingUnits - 1) {
                FontIcon plusIcon = new FontIcon(Feather.PLUS_CIRCLE);
                plusIcon.getStyleClass().add("responsive-icon");
                plusIcon.setIconSize(48);
                shelvingUnitContainer.getChildren().add(plusIcon);
            }
        }

        // Agregar un ícono después de la última estantería visible
        FontIcon afterIcon = new FontIcon(Feather.PLUS_CIRCLE);
        afterIcon.getStyleClass().add("responsive-icon");
        afterIcon.setIconSize(48);
        shelvingUnitContainer.getChildren().add(afterIcon);
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
}
