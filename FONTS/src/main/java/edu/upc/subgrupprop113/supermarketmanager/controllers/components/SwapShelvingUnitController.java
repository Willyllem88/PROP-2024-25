package edu.upc.subgrupprop113.supermarketmanager.controllers.components;


import edu.upc.subgrupprop113.supermarketmanager.controllers.DomainController;
import edu.upc.subgrupprop113.supermarketmanager.controllers.PresentationController;
import edu.upc.subgrupprop113.supermarketmanager.dtos.ProductDto;
import edu.upc.subgrupprop113.supermarketmanager.dtos.ShelvingUnitDto;
import edu.upc.subgrupprop113.supermarketmanager.factories.DomainControllerFactory;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.feather.Feather;

import java.io.InputStream;
import java.util.Objects;
import java.util.function.BiConsumer; // Import necesario para el callback

public class SwapShelvingUnitController extends ShelvingUnitController {
    private BiConsumer<Integer, Boolean> onToggleButtonStateChanged;
    private Integer height;
    private Integer pos;

    public SwapShelvingUnitController(PresentationController presentationController, int supermarketPosition, Integer pos, Integer height) {
        super(presentationController, supermarketPosition);
        this.pos = pos;
        this.height = height;
    }

    public void setOnToggleButtonStateChanged(BiConsumer<Integer, Boolean> onToggleButtonStateChanged) {
        this.onToggleButtonStateChanged = onToggleButtonStateChanged;
    }

    @Override
    protected void adjustProductImages() {
        super.adjustProductImages();
        addExtra();
    }

    private void addExtra() {
        for(Integer i = 0; i < productContainer.getChildren().size(); i++) {
            VBox productBox = (VBox) productContainer.getChildren().get(i);
            ToggleButton toggleButton = new ToggleButton();
            FontIcon icon;
            if(pos == supermarketPosition) {
                if(height != i)  icon = new FontIcon(Feather.SQUARE);
                else  {
                    icon = new FontIcon(Feather.CHECK_SQUARE);
                    toggleButton.setSelected(true);
                }
            }
            else icon = new FontIcon(Feather.SQUARE);

            toggleButton.setGraphic(icon);
            toggleButton.setMinHeight(1);
            toggleButton.setMinWidth(1);
            toggleButton.setStyle("-fx-background-color: transparent;");


            int finalI = i; // Índice del producto
            toggleButton.setOnAction(event -> {
                boolean isSelected = toggleButton.isSelected();
                icon.setIconCode(isSelected ? Feather.CHECK_SQUARE : Feather.SQUARE);
                if (onToggleButtonStateChanged != null) {
                    onToggleButtonStateChanged.accept(finalI, isSelected);
                }
            });
            toggleButton.setStyle("-fx-background-color: transparent;");
            toggleButton.setVisible(true);
            productBox.getChildren().add(toggleButton);
        }
        productContainer.requestLayout();
    }

}
