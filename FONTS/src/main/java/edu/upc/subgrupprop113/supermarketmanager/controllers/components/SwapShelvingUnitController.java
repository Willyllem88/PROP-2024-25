package edu.upc.subgrupprop113.supermarketmanager.controllers.components;


import edu.upc.subgrupprop113.supermarketmanager.controllers.DomainController;
import edu.upc.subgrupprop113.supermarketmanager.controllers.PresentationController;
import edu.upc.subgrupprop113.supermarketmanager.dtos.ProductDto;
import edu.upc.subgrupprop113.supermarketmanager.dtos.ShelvingUnitDto;
import edu.upc.subgrupprop113.supermarketmanager.factories.DomainControllerFactory;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
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

    public SwapShelvingUnitController(PresentationController presentationController, int supermarketPosition) {
        super(presentationController, supermarketPosition);
    }

    public void setOnToggleButtonStateChanged(BiConsumer<Integer, Boolean> onToggleButtonStateChanged) {
        this.onToggleButtonStateChanged = onToggleButtonStateChanged;
    }

    @Override
    public void adjustProductImages() {
        int numProducts = this.shelvingUnitInfo.getProducts().size();
        if (numProducts <= 0) {
            productContainer.getChildren().clear();
            return;
        }

        double containerHeight = productContainer.getHeight();
        double containerWidth = productContainer.getWidth();
        double productHeight = containerHeight / numProducts;

        productContainer.getChildren().clear();

        for (int i = 0; i < numProducts; i++) {
            VBox productBox = new VBox();
            productBox.setSpacing(5);
            productBox.getStyleClass().add("product-box");

            productBox.setMaxHeight(productHeight);
            productBox.setMinHeight(10);
            productBox.setPrefHeight(productHeight);
            if (this.shelvingUnitInfo.getProducts().get(i) != null) {
                String product_name = this.shelvingUnitInfo.getProducts().get(i).getName().toUpperCase();
                String product_path = this.shelvingUnitInfo.getProducts().get(i).getImgPath();

                ImageView productImageView = new ImageView();
                InputStream imageStream = getClass().getResourceAsStream("/edu/upc/subgrupprop113/supermarketmanager/assets/productIcons/" + product_path + ".jpg");
                if (imageStream != null) {
                    productImageView.setImage(new Image(imageStream));
                } else {
                    productImageView.setImage(new Image (Objects.requireNonNull(getClass().getResourceAsStream("/edu/upc/subgrupprop113/supermarketmanager/assets/error-img.png"))));
                }
                productBox.setVgrow(productImageView, Priority.ALWAYS);
                productImageView.setPreserveRatio(true);
                productImageView.setFitHeight((Math.min(productHeight, containerWidth) - 50) * 0.8);
                productImageView.setFitWidth((Math.min(productHeight, containerWidth) - 50) * 0.8);// Ajustar el tamaño del texto en el Label
                Label productLabel = new Label(product_name);
                productLabel.getStyleClass().add("product-name");
                productLabel.setAlignment(javafx.geometry.Pos.CENTER); // Alineación del Label

                productLabel.setMaxWidth(containerWidth - 50);  // Ajuste el tamaño máximo
                //productLabel.setWrapText(true); // Si el texto es largo, se ajustará a varias líneas

                // Ajustar el tamaño de la fuente dinámicamente
                double fontSize = Math.min(containerWidth, containerHeight) / 20;  // Ajusta el tamaño de la fuente
                productLabel.setStyle("-fx-font-size: " + fontSize + "px;");
                productBox.getChildren().addAll(productImageView, productLabel);

                ToggleButton toggleButton = new ToggleButton();
                FontIcon icon = new FontIcon(Feather.SQUARE);
                toggleButton.setGraphic(icon);
                toggleButton.setMinHeight(1);
                toggleButton.setMinWidth(1);


                int finalI = i; // Índice del producto
                toggleButton.setOnAction(event -> {
                    boolean isSelected = toggleButton.isSelected();
                    icon.setIconCode(isSelected ? Feather.CHECK_SQUARE : Feather.SQUARE);
                    if (onToggleButtonStateChanged != null) {
                        onToggleButtonStateChanged.accept(finalI, isSelected); // Llama al callback
                    }
                });

                productBox.getChildren().add(toggleButton);
            }
            else {
                ToggleButton toggleButton = new ToggleButton();
                FontIcon icon = new FontIcon(Feather.SQUARE);
                toggleButton.setGraphic(icon);
                toggleButton.setMinHeight(1);
                toggleButton.setMinWidth(1);


                int finalI = i; // Índice del producto
                toggleButton.setOnAction(event -> {
                    boolean isSelected = toggleButton.isSelected();
                    icon.setIconCode(isSelected ? Feather.CHECK_SQUARE : Feather.SQUARE);
                    if (onToggleButtonStateChanged != null) {
                        onToggleButtonStateChanged.accept(finalI, isSelected); // Llama al callback
                    }
                });

                productBox.getChildren().add(toggleButton);
            }
            productContainer.getChildren().add(productBox);
        }
    }
}
