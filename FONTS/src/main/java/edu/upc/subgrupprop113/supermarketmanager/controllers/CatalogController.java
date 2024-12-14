package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.Main;
import edu.upc.subgrupprop113.supermarketmanager.controllers.components.EditKeywordsController;
import edu.upc.subgrupprop113.supermarketmanager.controllers.components.TopBarController;
import edu.upc.subgrupprop113.supermarketmanager.controllers.components.SetTemperatureController;
import edu.upc.subgrupprop113.supermarketmanager.dtos.ProductDto;
import edu.upc.subgrupprop113.supermarketmanager.factories.DomainControllerFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
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
    private Label productNameLabel;

    @FXML
    private Label productName;

    @FXML
    private TextField productNameTextField;

    @FXML
    private HBox editNameIconsBox;

    @FXML
    private FontIcon confirmNameIcon;

    @FXML
    private FontIcon cancelNameIcon;

    @FXML
    private Label productPriceLabel;

    @FXML
    private Label productPrice;

    @FXML
    private TextField productPriceTextField;

    @FXML
    private HBox editPriceIconsBox;

    @FXML
    private FontIcon confirmPriceIcon;

    @FXML
    private FontIcon cancelPriceIcon;

    @FXML
    private Label productTemperature;

    @FXML
    private StackPane setTemperatureWrapper;

    @FXML
    private HBox setTemperatureComponent;

    @FXML
    private SetTemperatureController setTemperatureComponentController;

    @FXML
    private HBox editTemperatureIconsBox;

    @FXML
    private FontIcon confirmTemperatureIcon;

    @FXML
    private FontIcon cancelTemperatureIcon;


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

    private final PresentationController presentationController;

    public CatalogController(PresentationController presentationController) {
        this.presentationController = presentationController;
    }

    private final DomainController domainController = DomainControllerFactory.getInstance().getDomainController();

    private List<ProductDto> searchResultProducts;

    @FXML
    private void initialize() {
        TopBarController topBarController = (TopBarController) topBar.getProperties().get("controller");
        topBarController.setPresentationController(this.presentationController);

        topBarController.showNewDistributionButton(false);
        placeholderMessage.setVisible(true);
        productDetailsScrollPane.setVisible(false);

        // Restrict the product names to alphanumeric characters
        restrictTextField(searchBar, "[a-zA-Z0-9\\s]*");
        restrictTextField(productNameTextField, "[a-zA-Z0-9\\s]*");
        // Restrict the price text field to numbers
        restrictTextField(productPriceTextField, "\\d*\\.?\\d{0,2}");

        // Trim leading spaces in text fields
        trimLeadingSpaces(productNameTextField);
        trimLeadingSpaces(searchBar);

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

        sortCatalogProducts();

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

    private void sortCatalogProducts() {
        this.searchResultProducts = domainController.getProducts().stream()
                .sorted((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()))
                .toList();
        populateSearchResults(searchResultProducts);
    }

    private void restrictTextField(TextField textField, String regex) {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            // Allow only numbers and a single decimal point
            if (newText.matches(regex)) {
                return change;
            }
            return null; // Reject the change
        });
        textField.setTextFormatter(formatter);
    }

    private void trimLeadingSpaces(TextField textField) {
        textField.textProperty().addListener((_, _, newValue) -> {
            if (newValue != null && newValue.startsWith(" ")) {
                textField.setText(newValue.stripLeading());
            }
        });
    }

    private void handleSearch(String query) {
        String trimmedQuery = query.trim();
        if (!trimmedQuery.isEmpty()) {
            List<ProductDto> filteredProducts = domainController.searchProduct(trimmedQuery);
            populateSearchResults(filteredProducts);
        }
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

            productName.setText(selectedProduct.getName());
            productPrice.setText(String.valueOf(selectedProduct.getPrice()));
            String temperature = selectedProduct.getTemperature();
            temperature = switch (temperature) {
                case "REFRIGERATED" -> "FRIDGE";
                case "FROZEN" -> "FREEZER";
                default -> "AMBIENT";
            };
            productTemperature.setText("Temperature: " + temperature);

            // Placeholder for the image
            productImage.setImage(new Image(Objects.requireNonNull(Main.class.getResource(selectedProduct.getImgPath())).toExternalForm())); // Replace with selectedProduct.getImagePath()

            // Update keywords
            updateProductKeywords(selectedProduct);
        }
    }

    @FXML
    private void handleEditName() {
        // Switch to editing mode
        switchToViewMode();
        productName.setVisible(false);

        productNameTextField.setText(productName.getText());
        productNameTextField.setVisible(true);
        productNameTextField.requestFocus();

        editNameIconsBox.setVisible(true);

        editNameIcon.setVisible(false);
    }

    @FXML
    private void handleConfirmName() {
        // Confirm the edit
        String newName = productNameTextField.getText().trim();
        if (!newName.isEmpty()) {
            if (newName.equals(productName.getText())) {
                handleCancelEdit();
                return;
            }
            // Update the selected product's name
            ProductDto selectedProduct = domainController.getProduct(productName.getText());
            if (selectedProduct != null) {
                selectedProduct.setName(newName);
                domainController.createProduct(selectedProduct);
                ProductDto updatedProduct = domainController.getProduct(newName);
                updateProductKeywords(updatedProduct);
                domainController.removeProduct(productName.getText());
                productName.setText(newName);

                sortCatalogProducts();

                // Switch back to view mode
                switchToViewMode();
            }
        }
    }

    @FXML
    private void handleEditPrice() {
        // Switch to editing mode for price
        switchToViewMode();
        productPrice.setVisible(false);

        productPriceTextField.setText(productPrice.getText());
        productPriceTextField.setVisible(true);
        productPriceTextField.requestFocus();

        editPriceIconsBox.setVisible(true);

        editPriceIcon.setVisible(false);
    }

    @FXML
    private void handleConfirmPrice() {
        // Confirm the edit for price
        String newPriceText = productPriceTextField.getText().trim();
        if (!newPriceText.isEmpty()) {
            float newPrice = Float.parseFloat(newPriceText);
            ProductDto selectedProduct = domainController.getProduct(productName.getText());
            if (selectedProduct != null) {
                selectedProduct.setPrice(newPrice);
                domainController.modifyProduct(selectedProduct);
                productPrice.setText(String.valueOf(newPrice));
            }
        }
        // Switch back to view mode
        switchToViewMode();
    }

    @FXML
    private void handleEditTemperature() {
        // Switch to editing mode for temperature
        switchToViewMode();
        productTemperature.setVisible(false);
        setTemperatureWrapper.setVisible(true);

        // Set the choice box to the current temperature
        String currentTemperature = productTemperature.getText().replace("Temperature: ", "");
        currentTemperature = switch (currentTemperature) {
            case "FRIDGE" -> "REFRIGERATED";
            case "FREEZER" -> "FROZEN";
            default -> "AMBIENT";
        };
        setTemperatureComponentController.setTemperature(currentTemperature);

        editTemperatureIconsBox.setVisible(true);
        editTemperatureIcon.setVisible(false);
    }

    @FXML
    private void handleConfirmTemperature() {
        // Confirm the temperature change
        String newTemperature = setTemperatureComponentController.getTemperature();
        ProductDto selectedProduct = domainController.getProduct(productName.getText());
        if (selectedProduct != null) {
            selectedProduct.setTemperature(newTemperature);
            domainController.modifyProduct(selectedProduct);
            switch (newTemperature) {
                case "REFRIGERATED" -> newTemperature = "FRIDGE";
                case "FROZEN" -> newTemperature = "FREEZER";
                default -> newTemperature = "AMBIENT";
            }
            productTemperature.setText("Temperature: " + newTemperature);
        }

        // Switch back to view mode
        switchToViewMode();
    }

    @FXML
    private void handleEditKeywords() {
        try {
            switchToViewMode();
            // Load the FXML for the dialog
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/components/editKeywords.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the current keywords
            EditKeywordsController controller = loader.getController();
            List<String> currentKeywords = domainController.getProduct(productName.getText()).getKeywords();
            controller.setKeywords(currentKeywords);

            // Create the dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Keywords");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

            // Check if the user saved the changes
            if (controller.isSaved()) {
                List<String> updatedKeywords = controller.getKeywords();

                // Update the product and the UI
                ProductDto selectedProduct = domainController.getProduct(productName.getText());
                if (selectedProduct != null) {
                    selectedProduct.setKeywords(updatedKeywords);
                    domainController.modifyProduct(selectedProduct);
                    updateProductKeywords(selectedProduct);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancelEdit() {
        switchToViewMode();
    }

    private void switchToViewMode() {
        // Restore the view mode
        productName.setVisible(true);
        productNameTextField.setVisible(false);
        editNameIconsBox.setVisible(false);
        editNameIcon.setVisible(true);

        productPrice.setVisible(true);
        productPriceTextField.setVisible(false);
        editPriceIconsBox.setVisible(false);
        editPriceIcon.setVisible(true);

        // Restore the view mode for temperature
        productTemperature.setVisible(true);
        setTemperatureWrapper.setVisible(false);
        editTemperatureIconsBox.setVisible(false);
        editTemperatureIcon.setVisible(true);
    }

    private void updateProductKeywords(ProductDto product) {
        productKeywords.getChildren().clear();
        for (String keyword : product.getKeywords()) {
            Label keywordLabel = new Label(keyword);
            keywordLabel.getStyleClass().add("keyword-label");
            productKeywords.getChildren().add(keywordLabel);
        }
    }
}
