package edu.upc.subgrupprop113.supermarketmanager;

import javafx.util.Pair;
import java.util.ArrayList;
import java.util.List;

public class BruteForce implements OrderingStrategy {

    private int shelfHeight;
    private double highestScore;
    private List<ShelvingUnit> optimalDistribution;

    @Override
    public ArrayList<ShelvingUnit> orderSupermarket(List<ShelvingUnit> initialShelves, List<Product> products) {
        if (initialShelves == null || initialShelves.isEmpty()) {
            throw new IllegalArgumentException("Initial shelves cannot be null or empty.");
        }
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Products cannot be null or empty.");
        }

        this.shelfHeight = initialShelves.getFirst().getHeight();
        this.highestScore = -1;
        this.optimalDistribution = deepCopyShelves((ArrayList<ShelvingUnit>) initialShelves, true);

        // Try to place each product as the starting product and recursively find the best distribution
        for (Product startingProduct : products) {
            if (isShelfCompatible(initialShelves.getFirst(), startingProduct)) {
                List<Product> remainingProducts = new ArrayList<>(products);
                remainingProducts.remove(startingProduct);

                ArrayList<ShelvingUnit> currentShelves = deepCopyShelves((ArrayList<ShelvingUnit>) initialShelves, true);
                currentShelves.getFirst().addProduct(startingProduct, this.shelfHeight - 1);

                recursivelyPlaceProducts(1, remainingProducts, currentShelves, 0);
            }
        }

        return (ArrayList<ShelvingUnit>) this.optimalDistribution;
    }

    /**
     * Recursively places all products on shelves using a backtracking approach.
     * @param currentShelfIndex The index of the current shelf being filled.
     * @param remainingProducts The list of products that still need to be placed.
     * @param shelves The current state of the shelves.
     * @param currentScore The current accumulated similarity score for the placement.
     */
    private void recursivelyPlaceProducts(int currentShelfIndex, List<Product> remainingProducts, ArrayList<ShelvingUnit> shelves, double currentScore) {
        if (remainingProducts.isEmpty()) { // TODO: There can be more products than shelves * shelfHeight
            if (currentScore > highestScore) {
                this.optimalDistribution = deepCopyShelves(shelves, false);
                this.highestScore = currentScore;
            }
        } else {
            ShelvingUnit currentShelf = shelves.get(currentShelfIndex % shelves.size());
            int height = this.shelfHeight - 1 - (currentShelfIndex / shelves.size());

            Pair<Product, Double> bestProductPair = findBestProductToPlace(currentShelfIndex, remainingProducts, shelves, height);
            Product bestProduct = bestProductPair.getKey();
            double bestSimilarity = bestProductPair.getValue();

            if (bestProduct != null) {
                // Place the best product found
                currentShelf.addProduct(bestProduct, height);
                remainingProducts.remove(bestProduct);

                // Recur to place the remaining products
                int nextIndex = calculateNextShelfIndex(currentShelfIndex, height, shelves.size());
                recursivelyPlaceProducts(nextIndex, remainingProducts, shelves, currentScore + bestSimilarity);

                // Backtrack: undo placement
                currentShelf.removeProduct(height);
                remainingProducts.add(bestProduct);
            }
        }
    }

    /**
     * Finds the next best product to place based on similarity and compatibility with the current shelf.
     * @param currentShelfIndex The index of the current shelf.
     * @param remainingProducts The products that need to be placed.
     * @param shelves The current state of the shelves.
     * @param height The height position on the current shelf.
     * @return A Pair containing the best product and its similarity score.
     */
    private Pair<Product, Double> findBestProductToPlace(int currentShelfIndex, List<Product> remainingProducts, ArrayList<ShelvingUnit> shelves, int height) {
        ShelvingUnit currentShelf = shelves.get(currentShelfIndex % shelves.size());
        Product bestProduct = null;
        double bestSimilarity = 0;

        int previousShelfIndex = calculatePreviousShelfIndex(currentShelfIndex, height, shelves.size());
        ShelvingUnit previousShelf = shelves.get(previousShelfIndex % shelves.size());
        int previousHeight = this.shelfHeight - 1 - (previousShelfIndex / shelves.size());

        if (previousHeight < 0 || previousHeight >= this.shelfHeight) {
            throw new IndexOutOfBoundsException("Height out of bounds: " + previousHeight);
        }

        Product previousProduct = previousShelf.getProduct(previousHeight);

        for (Product candidate : remainingProducts) {
            if (isShelfCompatible(currentShelf, candidate)) {
                double similarity = calculateSimilarity(previousProduct, candidate);
                if (similarity > bestSimilarity) {
                    bestSimilarity = similarity;
                    bestProduct = candidate;
                }
            }
        }

        return new Pair<>(bestProduct, bestSimilarity);
    }

    /**
     * Calculates the next index to place a product, moving across shelves and heights.
     * @param currentShelfIndex The current index being filled.
     * @param height The current height on the shelf.
     * @param numShelves The total number of shelves.
     * @return The next shelf index to access.
     */
    private int calculateNextShelfIndex(int currentShelfIndex, int height, int numShelves) {
        int direction = (height % 2 == 0) ? 1 : -1;
        int nextIndex = currentShelfIndex + direction;

        if ((currentShelfIndex % numShelves == 0 && direction == -1) || (currentShelfIndex % numShelves == numShelves - 1 && direction == 1)) {
            nextIndex = currentShelfIndex + numShelves;
        }

        if (nextIndex < 0 || nextIndex >= numShelves * shelfHeight) {
            throw new IndexOutOfBoundsException("Next index out of bounds: " + nextIndex);
        }

        return nextIndex;
    }

    /**
     * Calculates the previous index for backtracking purposes.
     * @param currentShelfIndex The current index being backtracked from.
     * @param height The current height on the shelf.
     * @param numShelves The total number of shelves.
     * @return The previous shelf index to access.
     */
    private int calculatePreviousShelfIndex(int currentShelfIndex, int height, int numShelves) {
        int direction = (height % 2 == 0) ? -1 : 1;
        int previousIndex = currentShelfIndex + direction;

        if ((currentShelfIndex % numShelves == 0 && direction == -1) || (currentShelfIndex % numShelves == numShelves - 1 && direction == 1)) {
            previousIndex = currentShelfIndex - numShelves;
        }

        if (previousIndex < 0 || previousIndex >= numShelves * shelfHeight) {
            throw new IndexOutOfBoundsException("Previous index out of bounds: " + previousIndex);
        }

        return previousIndex;
    }

    /**
     * Checks if a product can be placed on a given shelf based on temperature compatibility.
     * @param shelf The shelf to be checked.
     * @param product The product to be placed.
     * @return True if the product is compatible, false otherwise.
     */
    private Boolean isShelfCompatible(ShelvingUnit shelf, Product product) {
        return shelf.getTemperature() == product.getTemperature();
    }

    /**
     * Calculates the similarity between two products.
     * @param productA The first product.
     * @param productB The second product.
     * @return The similarity score between the two products.
     */
    private double calculateSimilarity(Product productA, Product productB) {
        return productA.getRelatedValue(productB);
    }

    /**
     * Creates a deep copy of the list of shelving units.
     * @param originalShelves The original list of shelves.
     * @param empty Whether the shelves should be emptied during the copying process.
     * @return A deep copy of the shelving units.
     */
    private ArrayList<ShelvingUnit> deepCopyShelves(ArrayList<ShelvingUnit> originalShelves, boolean empty) {
        ArrayList<ShelvingUnit> copiedShelves = new ArrayList<>();
        for (ShelvingUnit shelf : originalShelves) {
            ShelvingUnit copy = new ShelvingUnit(shelf); // Using the copy constructor
            if (empty) {
                copy.emptyShelvingUnit(); // Ensure the copy has no products
            }
            copiedShelves.add(copy);
        }
        return copiedShelves;
    }
}
