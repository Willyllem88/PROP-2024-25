package edu.upc.subgrupprop113.supermarketmanager.controllers.components;

import edu.upc.subgrupprop113.supermarketmanager.controllers.DomainController;
import edu.upc.subgrupprop113.supermarketmanager.controllers.PresentationController;
import edu.upc.subgrupprop113.supermarketmanager.dtos.ShelvingUnitDto;
import edu.upc.subgrupprop113.supermarketmanager.factories.DomainControllerFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.Objects;
import java.io.InputStream;
import javafx.scene.layout.Priority;
import javafx.stage.Screen;


public class ShelvingUnitController {
    @FXML
    protected HBox root;

    @FXML
    private ImageView shelvingTypeImage;

    @FXML
    protected VBox productContainer;

    private PresentationController presentationController;
    protected final DomainController domainController;

    protected int supermarketPosition;
    protected ShelvingUnitDto shelvingUnitDto;

    public ShelvingUnitController(PresentationController presentationController, int supermarketPosition) {
        this.presentationController = presentationController;
        this.domainController = DomainControllerFactory.getInstance().getDomainController();
        this.setSupermarketPosition(supermarketPosition);
    }

    @FXML
    private void initialize() {
        if (root != null) {
            root.getProperties().put("controller", this);
        }

        productContainer.heightProperty().addListener((observable, oldHeight, newHeight) -> {
            adjustProductImages();
        });

        productContainer.widthProperty().addListener((observable, oldWidth, newWidth) -> {
            adjustProductImages();
        });

        initView();
    }

    public int getSupermarketPosition() {
        return supermarketPosition;
    }

    public void setSupermarketPosition(int supermarketPosition) throws IllegalArgumentException {
        this.supermarketPosition = supermarketPosition;
        this.shelvingUnitDto = domainController.getShelvingUnit(supermarketPosition);
    }

    protected void initView() {
        if (shelvingUnitDto == null) {
            return;
        }

        loadShelvingTypeImage();

        adjustProductImages();
    }

    protected void updateView() {
        setSupermarketPosition(supermarketPosition);
        initView();
    }

    private void loadShelvingTypeImage() {
        String type = this.shelvingUnitDto.getTemperature();
        InputStream imageStream = getClass().getResourceAsStream("/edu/upc/subgrupprop113/supermarketmanager/assets/temperatureIcons/" + type + ".png");
        if (imageStream != null) {
            shelvingTypeImage.setImage(new Image(imageStream));
        } else {
            shelvingTypeImage.setImage(new Image("/edu/upc/subgrupprop113/supermarketmanager/assets/error-img.png"));
        }
    }

    protected void adjustProductImages() {
        int numProducts = this.shelvingUnitDto.getProducts().size();
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
            if(this.shelvingUnitDto.getProducts().get(i) != null) {
                String product_name = this.shelvingUnitDto.getProducts().get(i).getName().toUpperCase();
                String product_path = this.shelvingUnitDto.getProducts().get(i).getImgPath();

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
                productImageView.setFitWidth((Math.min(productHeight, containerWidth) - 50) * 0.8);
                Label productLabel = new Label(product_name);
                productLabel.getStyleClass().add("product-name");
                productLabel.setAlignment(javafx.geometry.Pos.CENTER); // Alineación del Label

                productLabel.setMaxWidth(containerWidth - 50);  // Ajuste el tamaño máximo
                //productLabel.setWrapText(true); // Si el texto es largo, se ajustará a varias líneas

                // Ajustar el tamaño de la fuente dinámicamente
                double fontSize = Math.min(containerWidth, containerHeight) / 20;  // Ajusta el tamaño de la fuente
                productLabel.setStyle("-fx-font-size: " + fontSize + "px;");

                productBox.getChildren().addAll(productImageView, productLabel);
            }
            productContainer.getChildren().add(productBox);
        }
    }
}
