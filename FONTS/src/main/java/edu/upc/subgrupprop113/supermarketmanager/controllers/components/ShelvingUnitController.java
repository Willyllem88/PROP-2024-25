package edu.upc.subgrupprop113.supermarketmanager.controllers.components;

import edu.upc.subgrupprop113.supermarketmanager.controllers.DomainController;
import edu.upc.subgrupprop113.supermarketmanager.controllers.PresentationController;
import edu.upc.subgrupprop113.supermarketmanager.dtos.ShelvingUnitDto;
import edu.upc.subgrupprop113.supermarketmanager.factories.DomainControllerFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class ShelvingUnitController {
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

    public ShelvingUnitController(PresentationController presentationController, int supermarketPosition) {
        this.presentationController = presentationController;
        this.domainController = DomainControllerFactory.getInstance().getDomainController();
        this.setSupermarketPosition(supermarketPosition);
    }

    @FXML
    public void initialize() {
        if (root != null) {
            root.getProperties().put("controller", this);
        }

        productContainer.heightProperty().addListener((observable, oldHeight, newHeight) -> {
            adjustProductImages();
        });

    }

    public int getSupermarketPosition() {
        return supermarketPosition;
    }

    public void setSupermarketPosition(int supermarketPosition) throws IllegalArgumentException {
        this.supermarketPosition = supermarketPosition;
        this.shelvingUnitInfo = domainController.getShelvingUnitDto(supermarketPosition);
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
        String OS = System.getProperty("os.name").toLowerCase();
        String filePath;
        if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"))
            filePath = "./src/main/resources/edu/upc/subgrupprop113/supermarketmanager/assets/temperatureIcons/";
        else
            filePath = ".\\src\\main\\resources\\edu\\upc\\subgrupprop113\\supermarketmanager\\assets\\temperatureIcons\\";
        shelvingTypeImage.setImage(new Image(filePath + type + ".png"));
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
            // Crear un contenedor para cada producto
            VBox productBox = new VBox();
            productBox.setSpacing(5);
            productBox.setStyle("-fx-border-color: black; -fx-alignment: center;");

            ImageView productImage = new ImageView(shelvingUnitInfo.getProducts().get(i).getImgPath());
            productImage.setPreserveRatio(true);
            productImage.setFitHeight(productHeight * 0.8);
            productImage.setFitWidth(100);

            Label productLabel = new Label(shelvingUnitInfo.getProducts().get(i).getName());

            productBox.getChildren().addAll(productImage, productLabel);

            productContainer.getChildren().add(productBox);
        }
    }
}
