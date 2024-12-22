package edu.upc.subgrupprop113.supermarketmanager.controllers.components;

import edu.upc.subgrupprop113.supermarketmanager.controllers.PresentationController;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.feather.Feather;

import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * Controller class for managing a swappable shelving unit in the supermarket application.
 * Extends the functionality of {@link ShelvingUnitController} by adding toggle buttons for product selection.
 */
public class ShelvingUnitSwapController extends ShelvingUnitController {

    /** Callback function invoked when the state of a toggle button changes. */
    private BiConsumer<Integer, Boolean> onToggleButtonStateChanged;

    /** Height of the shelving unit for product indexing. */
    private final Integer height;

    /** Position of the shelving unit in the supermarket. */
    private final Integer pos;

    /**
     * Constructs a new SwapShelvingUnitController.
     *
     * @param presentationController the presentation controller for managing UI interactions
     * @param supermarketPosition the position of the shelving unit in the supermarket
     * @param pos the position of the shelving unit to compare against
     * @param height the height index used for toggle button management
     */
    public ShelvingUnitSwapController(PresentationController presentationController, int supermarketPosition, Integer pos, Integer height) {
        super(presentationController, supermarketPosition);
        this.pos = pos;
        this.height = height;
    }

    /**
     * Sets the callback function to handle toggle button state changes.
     *
     * @param onToggleButtonStateChanged the callback function accepting the product index and selection state
     */
    public void setOnToggleButtonStateChanged(BiConsumer<Integer, Boolean> onToggleButtonStateChanged) {
        this.onToggleButtonStateChanged = onToggleButtonStateChanged;
    }

    /**
     * Adjusts the layout of product images and adds toggle buttons for each product.
     */
    @Override
    protected void adjustProductImages() {
        super.adjustProductImages();
        addExtra();
    }

    /**
     * Adds toggle buttons to each product box for selection or deselection.
     */
    private void addExtra() {
        for (Integer i = 0; i < productContainer.getChildren().size(); i++) {
            VBox productBox = (VBox) productContainer.getChildren().get(i);
            ToggleButton toggleButton = new ToggleButton();
            FontIcon icon;

            if (pos == supermarketPosition) {
                if (!Objects.equals(height, i)) {
                    icon = new FontIcon(Feather.SQUARE);
                } else {
                    icon = new FontIcon(Feather.CHECK_SQUARE);
                    toggleButton.setSelected(true);
                }
            } else {
                icon = new FontIcon(Feather.SQUARE);
            }

            toggleButton.setGraphic(icon);
            toggleButton.setMinHeight(1);
            toggleButton.setMinWidth(1);
            toggleButton.setStyle("-fx-background-color: transparent;");

            int finalI = i;
            toggleButton.setOnAction(event -> {
                boolean isSelected = toggleButton.isSelected();
                icon.setIconCode(isSelected ? Feather.CHECK_SQUARE : Feather.SQUARE);
                if (onToggleButtonStateChanged != null) {
                    onToggleButtonStateChanged.accept(finalI, isSelected);
                }
            });

            toggleButton.setVisible(true);
            productBox.getChildren().add(toggleButton);
        }
        productContainer.requestLayout();
    }
}
