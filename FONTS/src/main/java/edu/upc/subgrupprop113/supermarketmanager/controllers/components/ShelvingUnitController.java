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
        // Store a reference to this controller in the root node's properties
        if (root != null) {
            root.getProperties().put("controller", this);
        }

        // Ajustar las imágenes dinámicamente cuando cambie la altura del contenedor
        productContainer.heightProperty().addListener((observable, oldHeight, newHeight) -> {
            adjustProductImages(shelvingUnitInfo != null ? shelvingUnitInfo.getProducts().size() : 0);
        });

        // Configurar los botones
        configureButtons();
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

        // Configurar la imagen del tipo de estantería
        loadShelvingTypeImage(shelvingUnitInfo.getTypeImagePath());

        // Ajustar las imágenes de los productos
        adjustProductImages(shelvingUnitInfo.getProducts().size());
    }

    private void loadShelvingTypeImage(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            shelvingTypeImage.setImage(new Image(imagePath));
        } else {
            shelvingTypeImage.setImage(new Image("path/to/default/shelving/type/image.png"));
        }
    }

    private void adjustProductImages(int numProducts) {
        if (numProducts <= 0) {
            productContainer.getChildren().clear();
            return;
        }

        // Altura disponible para cada producto
        double containerHeight = productContainer.getHeight();
        double productHeight = containerHeight / numProducts;

        // Limpiar productos anteriores
        productContainer.getChildren().clear();

        for (int i = 0; i < numProducts; i++) {
            // Crear un contenedor para cada producto
            VBox productBox = new VBox();
            productBox.setSpacing(5);
            productBox.setStyle("-fx-border-color: black; -fx-alignment: center;");

            // Crear la imagen
            ImageView productImage = new ImageView(shelvingUnitInfo.getProducts().get(i).getImagePath());
            productImage.setPreserveRatio(true);
            productImage.setFitHeight(productHeight * 0.8); // Ajustar altura proporcional
            productImage.setFitWidth(100); // Ancho fijo

            // Crear la etiqueta para el nombre del producto
            Label productLabel = new Label(shelvingUnitInfo.getProducts().get(i).getName());

            // Añadir la imagen y la etiqueta al contenedor del producto
            productBox.getChildren().addAll(productImage, productLabel);

            // Añadir el producto al contenedor principal
            productContainer.getChildren().add(productBox);
        }
    }

    private void configureButtons() {
        buttonOne.setText("Button 1"); // Cambia el texto si es necesario
        buttonTwo.setText("Button 2");

        buttonOne.setOnAction(event -> handleButtonOne());
        buttonTwo.setOnAction(event -> handleButtonTwo());
    }

    private void handleButtonOne() {
        System.out.println("Botón 1 presionado");
        // Lógica específica para el primer botón
    }

    private void handleButtonTwo() {
        System.out.println("Botón 2 presionado");
        // Lógica específica para el segundo botón
    }
}
