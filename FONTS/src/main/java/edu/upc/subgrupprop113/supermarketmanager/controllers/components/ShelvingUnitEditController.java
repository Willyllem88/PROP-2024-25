package edu.upc.subgrupprop113.supermarketmanager.controllers.components;

import edu.upc.subgrupprop113.supermarketmanager.controllers.PresentationController;
import javafx.fxml.FXML;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;


public class ShelvingUnitEditController extends ShelvingUnitController {

    @FXML
    VBox editButtonsVB;

    public ShelvingUnitEditController(PresentationController presentationController, int supermarketPosition) {
        super(presentationController, supermarketPosition);
    }

    @Override
    protected void initView() {
        super.initView();

        // Clear
        editButtonsVB.getChildren().clear();
        editButtonsVB.setVisible(true);
        editButtonsVB.setManaged(true);

        // Fixed size of 70: temperature img (50 px) + 20 px of spacing
        addFixedSpacer(editButtonsVB, 70);

        // Add a region to the VBox to make the buttons appear at the bottom
        addFlexibleSpacer(editButtonsVB);

        // Add the buttons
        for (int i = 0; i < super.shelvingUnitDto.getProducts().size(); i++) {
            final int index = i;
            if (super.shelvingUnitDto.getProducts().get(i) != null) {
                // Add a minus icon
                FontIcon minusIcon = createFontIcon("fth-trash-2", 50);
                minusIcon.setOnMouseClicked(event -> eliminateProductHandler(index));
                editButtonsVB.getChildren().add(minusIcon);
            } else {
                // Add a plus icon
                FontIcon plusIcon = createFontIcon("fth-plus-circle", 50);
                plusIcon.setOnMouseClicked(event -> addProductHandler(index));
                editButtonsVB.getChildren().add(plusIcon);
            }

            // Add a spacer between icons
            addFlexibleSpacer(editButtonsVB);

            // Add an extra spacer if not the last product
            if (i < super.shelvingUnitDto.getProducts().size() - 1) {
                addFlexibleSpacer(editButtonsVB);
            }
        }

        // Add a spacer at the bottom
        addFixedSpacer(editButtonsVB, 10);
    }

    private FontIcon createFontIcon(String iconName, int iconSize) {
        FontIcon icon = new FontIcon(iconName);
        icon.setIconSize(iconSize);
        if (iconName.equals("fth-trash-2")) {
            icon.getStyleClass().add("delete-icon");
        } else {
            icon.getStyleClass().add("add-icon");
        }
        return icon;
    }

    private void addFixedSpacer(VBox vbox, double height) {
        Region spacer = new Region();
        spacer.setMinHeight(height);
        spacer.setMaxHeight(height);
        VBox.setVgrow(spacer, Priority.ALWAYS);
        vbox.getChildren().add(spacer);
    }

    private void addFlexibleSpacer(VBox vbox) {
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        vbox.getChildren().add(spacer);
    }

    private void eliminateProductHandler(int height) {
        // Eliminate the product
        domainController.removeProductFromShelvingUnit(height, supermarketPosition);

        // Update the view
        this.updateView();
    }

    public void addProductHandler(int height) {

        // Update the view
        this.updateView();
    }
}
