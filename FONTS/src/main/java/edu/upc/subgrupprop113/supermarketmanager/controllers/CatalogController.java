package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.controllers.components.TopBarController;
import edu.upc.subgrupprop113.supermarketmanager.dtos.ProductDto;
import edu.upc.subgrupprop113.supermarketmanager.factories.DomainControllerFactory;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;

public class CatalogController {

    @FXML
    private HBox mainContent;

    @FXML
    private HBox topBar;

    @FXML
    private VBox leftSide;

    @FXML
    private Label title;

    @FXML
    private ImageView productImage;

    @FXML
    private Label productName;

    @FXML
    private Label productPrice;

    @FXML
    private Label productTemperature;

    @FXML
    private HBox productKeywords;

    @FXML
    private FontIcon editNameIcon;

    @FXML
    private FontIcon editPriceIcon;

    @FXML
    private FontIcon editTemperatureIcon;

    @FXML
    private FontIcon editKeywordsIcon;


    @FXML
    private VBox rightSide;

    @FXML
    private TextField searchBar;

    @FXML
    private ScrollPane searchResultsPane;

    @FXML
    private VBox searchResults;

    @FXML
    private Button addButton;

    private PresentationController presentationController;

    public CatalogController(PresentationController presentationController) {
        this.presentationController = presentationController;
    }

    private final DomainController domainController = DomainControllerFactory.getInstance().getDomainController();

    @FXML
    public void initialize() {
        TopBarController topBarController = (TopBarController) topBar.getProperties().get("controller");
        topBarController.setPresentationController(this.presentationController);
        // TODO: correct image path format
        domainController.createProduct(new ProductDto("Orange", 1.0f, "REFRIGERATED", "images/orange.png", List.of("Fruit", "Vitamin C"), null));
        domainController.createProduct(new ProductDto("Apple", 1.5f, "REFRIGERATED", "images/apple.png", List.of("Fruit", "Vitamin C"), null));
        domainController.createProduct(new ProductDto("Banana", 0.5f, "REFRIGERATED", "images/banana.png", List.of("Fruit", "Vitamin C"), null));
        domainController.createProduct(new ProductDto("Milk", 2.0f, "REFRIGERATED", "images/milk.png", List.of("Dairy", "Protein"), null));
        domainController.createProduct(new ProductDto("Bread", 1.0f, "AMBIENT", "images/bread.png", List.of("Grain", "Carbs"), null));
        domainController.createProduct(new ProductDto("Eggs", 3.0f, "REFRIGERATED", "images/eggs.png", List.of("Protein", "Dairy"), null));
        domainController.createProduct(new ProductDto("Chicken", 5.0f, "REFRIGERATED", "images/chicken.png", List.of("Protein", "Meat"), null));
        domainController.createProduct(new ProductDto("Beef", 7.0f, "REFRIGERATED", "images/beef.png", List.of("Protein", "Meat"), null));
        domainController.createProduct(new ProductDto("Pork", 6.0f, "REFRIGERATED", "images/pork.png", List.of("Protein", "Meat"), null));
        domainController.createProduct(new ProductDto("Fish", 4.0f, "REFRIGERATED", "images/fish.png", List.of("Protein", "Seafood"), null));
        domainController.createProduct(new ProductDto("Shrimp", 8.0f, "REFRIGERATED", "images/shrimp.png", List.of("Protein", "Seafood"), null));
        domainController.createProduct(new ProductDto("Salmon", 10.0f, "REFRIGERATED", "images/salmon.png", List.of("Protein", "Seafood"), null));
        domainController.createProduct(new ProductDto("Tuna", 9.0f, "REFRIGERATED", "images/tuna.png", List.of("Protein", "Seafood"), null));
        domainController.createProduct(new ProductDto("Pasta", 2.0f, "AMBIENT", "images/pasta.png", List.of("Grain", "Carbs"), null));
        domainController.createProduct(new ProductDto("Rice", 1.5f, "AMBIENT", "images/rice.png", List.of("Grain", "Carbs"), null));
        populateSearchResults(domainController.getProducts());
    }

    public void populateSearchResults(List<ProductDto> products) {
        searchResults.getChildren().clear();
        for (ProductDto product : products) {
            HBox resultItem = new HBox();
            resultItem.getStyleClass().add("search-result-item");

            FontIcon icon = new FontIcon("fth-file");
            icon.setIconSize(20);
            icon.getStyleClass().add("result-icon");

            Label label = new Label(product.getName());
            label.getStyleClass().add("result-label");

            resultItem.getChildren().addAll(icon, label);
            resultItem.setOnMouseClicked(this::handleResultClick);

            searchResults.getChildren().add(resultItem);
        }
    }

    private void handleSearch(String query) {
        // TODO: Implement search logic
        System.out.println("Searching for: " + query);
    }

    private void showProductDetails(String productName) {
        // TODO: Implement detail view logic
        System.out.println("Showing details for: " + productName);
    }

    @FXML
    private void handleAddProduct() {
        // TODO: Implement add product logic
        System.out.println("Add Product button clicked");
    }

    @FXML
    private void handleResultClick(MouseEvent mouseEvent) {
        HBox clickedItem = (HBox) mouseEvent.getSource();
        Label label = (Label) clickedItem.getChildren().get(1); // Assuming the label is the second child
        String productName = label.getText();

        // Find the product DTO by name (assuming names are unique)
        ProductDto selectedProduct = domainController.getProduct(productName);

        if (selectedProduct != null) {
            // Update left panel
            productName = selectedProduct.getName();
            productPrice.setText("Price: $" + selectedProduct.getPrice());
            productTemperature.setText("Temperature: " + selectedProduct.getTemperature());

            // Placeholder for the image
            productImage.setImage(new Image(selectedProduct.getImgPath())); // Replace with selectedProduct.getImagePath()

            // Update keywords
            productKeywords.getChildren().clear();
            for (String keyword : selectedProduct.getKeywords()) {
                Label keywordLabel = new Label(keyword);
                keywordLabel.getStyleClass().add("keyword-label");
                productKeywords.getChildren().add(keywordLabel);
            }
        }
    }

    @FXML
    private void handleEditName() {
        System.out.println("Edit Name button clicked");
    }

    @FXML
    private void handleEditPrice() {
        System.out.println("Edit Price button clicked");
    }

    @FXML
    private void handleEditTemperature() {
        System.out.println("Edit Temperature button clicked");
    }

    @FXML
    private void handleEditKeywords() {
        System.out.println("Edit Keywords button clicked");
    }

}
