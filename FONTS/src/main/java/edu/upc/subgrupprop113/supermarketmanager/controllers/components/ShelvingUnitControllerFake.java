package edu.upc.subgrupprop113.supermarketmanager.controllers.components;

import edu.upc.subgrupprop113.supermarketmanager.controllers.DomainController;
import edu.upc.subgrupprop113.supermarketmanager.controllers.PresentationController;
import edu.upc.subgrupprop113.supermarketmanager.dtos.ShelvingUnitDto;
import edu.upc.subgrupprop113.supermarketmanager.dtos.ProductDto;
import edu.upc.subgrupprop113.supermarketmanager.factories.DomainControllerFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;

public class ShelvingUnitControllerFake {
    @FXML
    private VBox root;

    @FXML
    private ImageView shelvingTypeImage;

    @FXML
    private VBox productContainer;

    @FXML
    private Button buttonOne;

    @FXML
    private Button buttonTwo;

    private PresentationController presentationController;
    private final DomainController domainController;

    private int supermarketPosition;
    private ShelvingUnitDto shelvingUnitInfo;

    public ShelvingUnitControllerFake(PresentationController presentationController, int supermarketPosition) {
        this.presentationController = presentationController;
        this.domainController = DomainControllerFactory.getInstance().getDomainController();
    }

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            Scene scene = this.presentationController.getCurrentScene();
            scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("css/shelvingUnit.css")).toExternalForm());
        });
        if (root != null) {
            root.getProperties().put("controller", this);
        }

        productContainer.heightProperty().addListener((observable, oldHeight, newHeight) -> {
            adjustProductImages();
        });

        this.shelvingUnitInfo = new ShelvingUnitDto(0, "AMBIENT",
                Arrays.asList(
                        new ProductDto("bread", 10.0f, "AMBIENT", "bread", Collections.emptyList()),
                        new ProductDto("bread", 10.0f, "AMBIENT", "bread", Collections.emptyList()),
                        new ProductDto("bread", 10.0f, "AMBIENT", "bread", Collections.emptyList()),
                        new ProductDto("bread", 10.0f, "AMBIENT", "bread", Collections.emptyList()),
                        new ProductDto("bread", 10.0f, "AMBIENT", "bread", Collections.emptyList()),
                        new ProductDto("bread", 10.0f, "AMBIENT", "bread", Collections.emptyList())
                )
        );
        initView();
    }

    public void setSupermarketPosition(int supermarketPosition) throws IllegalArgumentException {
        this.supermarketPosition = supermarketPosition;
        this.shelvingUnitInfo = new ShelvingUnitDto(0, "AMBIENT",
                Arrays.asList(
                        new ProductDto("bread", 10.0f, "AMBIENT", "bread", Collections.emptyList()),
                        new ProductDto("bread", 10.0f, "AMBIENT", "bread", Collections.emptyList()),
                        new ProductDto("bread", 10.0f, "AMBIENT", "bread", Collections.emptyList()),
                        new ProductDto("bread", 10.0f, "AMBIENT", "bread", Collections.emptyList()),
                        new ProductDto("bread", 10.0f, "AMBIENT", "bread", Collections.emptyList()),
                        new ProductDto("bread", 10.0f, "AMBIENT", "bread", Collections.emptyList())
                )
        );
        initView();
    }

    private void initView() {
        if (shelvingUnitInfo == null) {
            return;
        }

        loadShelvingTypeImage();
        adjustProductImages();
    }

    private void loadShelvingTypeImage() {
        String type = this.shelvingUnitInfo.getTemperature();
        InputStream imageStream = getClass().getResourceAsStream("/edu/upc/subgrupprop113/supermarketmanager/assets/temperatureIcons/" + type + ".png");
        if (imageStream != null) {
            shelvingTypeImage.setImage(new Image(imageStream));
        } else {
            shelvingTypeImage.setImage(new Image("/edu/upc/subgrupprop113/supermarketmanager/assets/defaultImage.png"));
        }
    }

    private void adjustProductImages() {
        int numProducts = this.shelvingUnitInfo.getProducts().size();
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
            productBox.setStyle("-fx-border-color: black; -fx-alignment: center;");
            String product_name = this.shelvingUnitInfo.getProducts().get(i).getName();
            ImageView productImageView = new ImageView();
            InputStream imageStream = getClass().getResourceAsStream("/edu/upc/subgrupprop113/supermarketmanager/assets/productIcons/" + product_name + ".png");
            if (imageStream != null) {
                productImageView.setImage(new Image(imageStream));
            } else {
                productImageView.setImage(new Image("/edu/upc/subgrupprop113/supermarketmanager/assets/defaultImage.png"));
            }
            productImageView.setPreserveRatio(true);
            productImageView.setFitHeight(productHeight * 0.8);
            productImageView.setFitWidth(100);

            Label productLabel = new Label(shelvingUnitInfo.getProducts().get(i).getName());

            productBox.getChildren().addAll(productImageView, productLabel);
            productContainer.getChildren().add(productBox);
        }
    }
}
