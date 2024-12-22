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
import javafx.scene.layout.Priority;

/**
 * Controller class for managing a shelving unit in the supermarket application.
 * Handles the display and adjustment of products within a shelving unit, including dynamic resizing and layout adjustments.
 */
public class ShelvingUnitController {

    /** Root container for the shelving unit component. */
    @FXML
    protected HBox root;

    /** ImageView for displaying the type of shelving unit. */
    @FXML
    private ImageView shelvingTypeImage;

    /** Container for displaying products in the shelving unit. */
    @FXML
    protected VBox productContainer;

    @FXML
    private Label shelvingUnitPosition;

    /** Controller for handling presentation logic. */
    private PresentationController presentationController;

    /** Controller for handling domain-specific logic. */
    protected final DomainController domainController;

    /** Position of the shelving unit in the supermarket. */
    protected int supermarketPosition;

    /** Data Transfer Object (DTO) containing information about the shelving unit. */
    protected ShelvingUnitDto shelvingUnitDto;

    /**
     * Constructs a new ShelvingUnitController.
     *
     * @param presentationController the presentation controller for managing UI interactions
     * @param supermarketPosition the position of the shelving unit in the supermarket
     */
    public ShelvingUnitController(PresentationController presentationController, int supermarketPosition) {
        this.presentationController = presentationController;
        this.domainController = DomainControllerFactory.getInstance().getDomainController();
        this.setSupermarketPosition(supermarketPosition);
    }

    /**
     * Initializes the shelving unit component after loading the FXML.
     */
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

    /**
     * Sets the supermarket position for this shelving unit and retrieves its corresponding data.
     *
     * @param supermarketPosition the position of the shelving unit in the supermarket
     * @throws IllegalArgumentException if the position is invalid
     */
    public void setSupermarketPosition(int supermarketPosition) throws IllegalArgumentException {
        this.supermarketPosition = supermarketPosition;
        this.shelvingUnitDto = domainController.getShelvingUnit(supermarketPosition);
    }

    /**
     * Initializes the view by setting up the shelving type image and adjusting product images.
     */
    protected void initView() {
        if (shelvingUnitDto == null) {
            return;
        }

        loadShelvingTypeImage();
        adjustProductImages();
        shelvingUnitPosition.setText(String.valueOf(supermarketPosition));
    }

    /**
     * Updates the view by re-fetching the data and reinitializing the display.
     */
    protected void updateView() {
        setSupermarketPosition(supermarketPosition);
        initView();
    }

    /**
     * Loads and sets the shelving type image based on the temperature of the shelving unit.
     */
    private void loadShelvingTypeImage() {
        shelvingTypeImage.setImage(new Image(domainController.getTemperatureIcon(shelvingUnitDto.getTemperature())));
    }

    /**
     * Adjusts the layout and dimensions of product images in the shelving unit based on container size.
     */
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
                String productName = this.shelvingUnitDto.getProducts().get(i).getName().toUpperCase();
                String productPath = this.shelvingUnitDto.getProducts().get(i).getImgPath();

                ImageView productImageView = new ImageView();
                try {
                    productImageView.setImage(new Image(productPath));
                }
                catch (Exception e) {
                    if (! (e instanceof IllegalArgumentException)) {
                        productImageView.setImage(new Image(domainController.getErrorImage()));
                    }
                }

                productBox.setVgrow(productImageView, Priority.ALWAYS);
                productImageView.setPreserveRatio(true);
                productImageView.setFitHeight((Math.min(productHeight, containerWidth) - 50) * 0.8);
                productImageView.setFitWidth((Math.min(productHeight, containerWidth) - 50) * 0.8);
                Label productLabel = new Label(productName);
                productLabel.getStyleClass().add("product-name");
                productLabel.setAlignment(javafx.geometry.Pos.CENTER);

                productLabel.setMaxWidth(containerWidth - 50);

                double fontSize = Math.min(containerWidth, containerHeight) / 20;
                productLabel.setStyle("-fx-font-size: " + fontSize + "px;");

                productBox.getChildren().addAll(productImageView, productLabel);
            }
            productContainer.getChildren().add(productBox);
        }
    }
}
