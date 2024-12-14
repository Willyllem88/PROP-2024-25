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


public class ShelvingUnitController {
    @FXML
    protected HBox root;

    @FXML
    private ImageView shelvingTypeImage;

    @FXML
    private VBox productContainer;

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

    private void adjustProductImages() {
        int numProducts = this.shelvingUnitDto.getProducts().size();
        if (numProducts <= 0) {
            productContainer.getChildren().clear();
            return;
        }

        double containerHeight = productContainer.getHeight();
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
                productImageView.setFitHeight((productHeight - 50) * 0.8);
                productImageView.setFitWidth((productHeight - 50) * 0.8);
                Label productLabel = new Label(product_name);
                productLabel.getStyleClass().add("product-name");
                productBox.getChildren().addAll(productImageView, productLabel);
            }
            productContainer.getChildren().add(productBox);
        }
    }
}
