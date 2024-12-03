package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.factories.DomainControllerFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;

public class CatalogController {

    @FXML
    private HBox mainContent;

    @FXML
    private VBox leftSide;

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
        populateSearchResults(List.of("Product 1", "Product 2", "Product 3", "Product 4", "Product 5", "Product 6", "Product 7", "Product 8", "Product 9", "Product 10"));
    }

    public void populateSearchResults(List<String> products) {
        searchResults.getChildren().clear();
        for (String product : products) {
            HBox resultItem = new HBox();
            resultItem.getStyleClass().add("search-result-item");

            FontIcon icon = new FontIcon("fth-file");
            icon.setIconSize(20);
            icon.getStyleClass().add("result-icon");

            Label label = new Label(product);
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

        System.out.println("Clicked on product: " + productName);
        // Display product details in the left-side panel or handle as needed
    }
}
