package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.Main;
import edu.upc.subgrupprop113.supermarketmanager.controllers.components.TopBarController;
import edu.upc.subgrupprop113.supermarketmanager.dtos.ProductDto;
import edu.upc.subgrupprop113.supermarketmanager.factories.DomainControllerFactory;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;
import java.util.Objects;

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
    private Label placeholderMessage;

    @FXML
    private ScrollPane productDetailsScrollPane;

    @FXML
    private VBox productDetailsContainer;

    @FXML
    private ImageView productImage;

    @FXML
    private Label productName;

    @FXML
    private TextField productNameTextField;

    @FXML
    private FontIcon confirmNameIcon;

    @FXML
    private FontIcon cancelNameIcon;

    @FXML
    private Label productPrice;

    @FXML
    private Label productTemperature;

    @FXML
    private FlowPane productKeywords;

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
    private Label searchBarPlaceholder;

    @FXML
    private ScrollPane searchResultsPane;

    @FXML
    private VBox searchResults;

    private HBox selectedItem;
    private Label selectedLabel;

    @FXML
    private Button addButton;

    private PresentationController presentationController;

    public CatalogController(PresentationController presentationController) {
        this.presentationController = presentationController;
    }

    private final DomainController domainController = DomainControllerFactory.getInstance().getDomainController();

    private List<ProductDto> products;

    @FXML
    public void initialize() {
        TopBarController topBarController = (TopBarController) topBar.getProperties().get("controller");
        topBarController.setPresentationController(this.presentationController);

        topBarController.showNewDistributionButton(false);
        placeholderMessage.setVisible(true);
        productDetailsScrollPane.setVisible(false);

        // DEBUG: Create some products
        domainController.createProduct(new ProductDto("Orange", 1.0f, "REFRIGERATED", "images/orange.png", List.of("Fruit", "Vitamin C", "Round", "Healthy", "Juice", "Miau", "Guau", "Barça", "Girona"), null));
        domainController.createProduct(new ProductDto("Apple", 1.5f, "REFRIGERATED", "images/orange.png", List.of("Fruit", "Vitamin C"), null));
        domainController.createProduct(new ProductDto("Banana", 0.5f, "REFRIGERATED", "images/orange.png", List.of("Fruit", "Vitamin C"), null));
        domainController.createProduct(new ProductDto("Milk", 2.0f, "REFRIGERATED", "images/orange.png", List.of("Dairy", "Protein"), null));
        domainController.createProduct(new ProductDto("Bread", 1.0f, "AMBIENT", "images/orange.png", List.of("Grain", "Carbs"), null));
        domainController.createProduct(new ProductDto("Eggs", 3.0f, "REFRIGERATED", "images/orange.png", List.of("Protein", "Dairy"), null));
        domainController.createProduct(new ProductDto("Chicken", 5.0f, "REFRIGERATED", "images/orange.png", List.of("Protein", "Meat"), null));
        domainController.createProduct(new ProductDto("Beef", 7.0f, "REFRIGERATED", "images/orange.png", List.of("Protein", "Meat"), null));
        domainController.createProduct(new ProductDto("Pork", 6.0f, "REFRIGERATED", "images/orange.png", List.of("Protein", "Meat"), null));
        domainController.createProduct(new ProductDto("Fish", 4.0f, "REFRIGERATED", "images/orange.png", List.of("Protein", "Seafood"), null));
        domainController.createProduct(new ProductDto("Shrimp", 8.0f, "REFRIGERATED", "images/orange.png", List.of("Protein", "Seafood"), null));
        domainController.createProduct(new ProductDto("Salmon", 10.0f, "REFRIGERATED", "images/orange.png", List.of("Protein", "Seafood"), null));
        domainController.createProduct(new ProductDto("Tuna", 9.0f, "REFRIGERATED", "images/orange.png", List.of("Protein", "Seafood"), null));
        domainController.createProduct(new ProductDto("Pasta", 2.0f, "AMBIENT", "images/orange.png", List.of("Grain", "Carbs"), null));
        domainController.createProduct(new ProductDto("Rice", 1.5f, "AMBIENT", "images/orange.png", List.of("Grain", "Carbs"), null));

        this.products = domainController.getProducts().stream()
                .sorted((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()))
                .toList();
        populateSearchResults(products);

        // Automatically focus on the search bar
        searchBar.requestFocus();
        // Add listener to search bar
        searchBar.textProperty().addListener((_, _, newValue) -> handleSearch(newValue));
    }

    private void populateSearchResults(List<ProductDto> products) {
        searchResults.getChildren().clear();
        for (ProductDto product : products) {
            HBox resultItem = new HBox();
            resultItem.getStyleClass().add("search-result-item");

            resultItem.setSpacing(10);

            // Create an ImageView for the product image
            ImageView productImage = new ImageView();
            productImage.setFitWidth(30); // Set the preferred width
            productImage.setFitHeight(30); // Set the preferred height
            productImage.setPreserveRatio(true); // Preserve aspect ratio
            productImage.setImage(new Image(Objects.requireNonNull(Main.class.getResource(product.getImgPath())).toExternalForm()));

            Label label = new Label(product.getName());
            label.getStyleClass().add("result-label");

            resultItem.getChildren().addAll(productImage, label);
            resultItem.setOnMouseClicked(this::handleResultClick);

            searchResults.getChildren().add(resultItem);
        }
    }

    private void handleSearch(String query) {

        // Filter products based on query
        List<ProductDto> filteredProducts = this.products.stream()
                .filter(product ->
                        product.getName().toLowerCase().contains(query.toLowerCase()) ||
                                product.getKeywords().stream().anyMatch(keyword -> keyword.toLowerCase().contains(query.toLowerCase()))
                )
                .toList();

        // Update search results
        populateSearchResults(filteredProducts);
    }

    @FXML
    private void handleAddProduct() {
        // TODO: Implement add product logic
        System.out.println("Add Product button clicked");
    }

    @FXML
    private void handleResultClick(MouseEvent mouseEvent) {
        HBox clickedItem = (HBox) mouseEvent.getSource();
        // Remove the selected class from the previously selected item
        if (selectedItem != null) {
            selectedItem.getStyleClass().remove("selected");
        }
        // Add the selected class to the newly clicked item
        clickedItem.getStyleClass().add("selected");
        selectedItem = clickedItem;

        Label label = (Label) clickedItem.getChildren().get(1); // Assuming the label is the second child
        String product = label.getText();

        // Find the product DTO by name (assuming names are unique)
        ProductDto selectedProduct = domainController.getProduct(product);

        if (selectedProduct != null) {
            // Update left panel
            placeholderMessage.setVisible(false);
            productDetailsScrollPane.setVisible(true);

            productName.setText("Name: " + selectedProduct.getName());
            productPrice.setText("Price: $" + selectedProduct.getPrice());
            productTemperature.setText("Temperature: " + selectedProduct.getTemperature());

            // Placeholder for the image
            productImage.setImage(new Image(Objects.requireNonNull(Main.class.getResource(selectedProduct.getImgPath())).toExternalForm())); // Replace with selectedProduct.getImagePath()

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
        // Switch to editing mode
        productName.setVisible(false);
        productNameTextField.setText(productName.getText().replace("Name: ", ""));
        productNameTextField.setVisible(true);
        confirmNameIcon.setVisible(true);
        cancelNameIcon.setVisible(true);
        editNameIcon.setVisible(false);
    }

    // TODO: Fix view problem with name
    @FXML
    private void handleConfirmName() {
        // Confirm the edit
        String newName = productNameTextField.getText().trim();
        if (!newName.isEmpty()) {
            // Update the selected product's name
            ProductDto selectedProduct = domainController.getProduct(productName.getText().replace("Name: ", ""));
            if (selectedProduct != null) {
                selectedProduct.setName(newName);
                domainController.createProduct(selectedProduct);
                domainController.removeProduct(productName.getText().replace("Name: ", ""));
                productName.setText("Name: " + newName);
                products = domainController.getProducts().stream()
                        .sorted((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()))
                        .toList();
                populateSearchResults(products);
            }
        }
        // Switch back to view mode
        switchToViewMode();
    }

    @FXML
    private void handleEditPrice() {
        // TODO: Implement edit price logic
        System.out.println("Edit Price button clicked");
    }

    @FXML
    private void handleEditTemperature() {
        // TODO: Implement edit temperature logic
        System.out.println("Edit Temperature button clicked");
    }

    @FXML
    private void handleEditKeywords() {
        // TODO: Implement edit keywords logic
        System.out.println("Edit Keywords button clicked");
    }

    @FXML
    private void handleCancelEdit() {
        switchToViewMode();
    }

    private void switchToViewMode() {
        // Restore the view mode
        productName.setVisible(true);
        productNameTextField.setVisible(false);
        confirmNameIcon.setVisible(false);
        cancelNameIcon.setVisible(false);
        editNameIcon.setVisible(true);
    }
}
