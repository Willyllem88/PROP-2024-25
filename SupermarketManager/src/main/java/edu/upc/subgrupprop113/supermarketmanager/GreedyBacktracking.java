package edu.upc.subgrupprop113.supermarketmanager;

import javafx.util.Pair;
import java.util.ArrayList;
import java.util.List;

public class GreedyBacktracking implements OrderingStrategy {

    private int shelfHeight;
    private double highestScore;
    private List<ShelvingUnit> optimalDistribution;

    /**
     * Orders the supermarket shelves using a greedy backtracking approach.
     * @param initialShelves The initial state of the supermarket shelves.
     * @param products The list of products to be placed on the shelves.
     * @return The ordered list of supermarket shelves.
     */
    @Override
    public ArrayList<ShelvingUnit> orderSupermarket(List<ShelvingUnit> initialShelves, List<Product> products) {
        this.shelfHeight = initialShelves.getFirst().getHeight();
        this.highestScore = -1;
        this.optimalDistribution = deepCopyShelves((ArrayList<ShelvingUnit>) initialShelves, true);

        int currentShelfIndex = 0;
        int currentHeight = this.shelfHeight - 1;

        while (currentShelfIndex < initialShelves.size() * shelfHeight) {
            ShelvingUnit shelf = initialShelves.get(currentShelfIndex % initialShelves.size());
            for (Product startingProduct : products) {
                if (isShelfCompatible(shelf, startingProduct)) {
                    List<Product> remainingProducts = new ArrayList<>(products);
                    remainingProducts.remove(startingProduct);

                    ArrayList<ShelvingUnit> currentShelves = deepCopyShelves((ArrayList<ShelvingUnit>) initialShelves, true);
                    currentShelves.get(currentShelfIndex % initialShelves.size()).addProduct(startingProduct, currentHeight);

                    int nextIndex = calculateNextShelfIndex(currentShelfIndex, initialShelves.size());
                    recursivelyPlaceProducts(nextIndex, remainingProducts, currentShelves, 0);
                }
            }
            currentShelfIndex = calculateNextShelfIndex(currentShelfIndex, initialShelves.size());
            currentHeight = this.shelfHeight - 1 - (currentShelfIndex / initialShelves.size());
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
        if (remainingProducts.isEmpty() || currentShelfIndex >= shelves.size() * shelfHeight) {
            if (currentScore > highestScore) {
                this.optimalDistribution = deepCopyShelves(shelves, false);
                this.highestScore = currentScore;
            }
        } else {
            ShelvingUnit currentShelf = shelves.get(currentShelfIndex % shelves.size());
            int height = this.shelfHeight - 1 - (currentShelfIndex / shelves.size());
            if (height < 0) {
                // If height is out of bounds, stop recursion
                return;
            }

            Pair<Product, Double> bestProductPair = findBestProductToPlace(currentShelfIndex, remainingProducts, shelves);
            Product bestProduct = bestProductPair.getKey();
            double bestSimilarity = bestProductPair.getValue();

            int nextIndex = calculateNextShelfIndex(currentShelfIndex, shelves.size());

            if (bestProduct != null) {
                // Place the best product found
                currentShelf.addProduct(bestProduct, height);
                remainingProducts.remove(bestProduct);

                // Recur to place the remaining products
                recursivelyPlaceProducts(nextIndex, remainingProducts, shelves, currentScore + bestSimilarity);
            } else {
                // No compatible product found for this shelf and height
                // Proceed to the next index
                recursivelyPlaceProducts(nextIndex, remainingProducts, shelves, currentScore);
            }
        }
    }

    /**
     * Finds the next best product to place based on similarity and compatibility with the current shelf.
     * @param currentShelfIndex The index of the current shelf.
     * @param remainingProducts The products that need to be placed.
     * @param shelves The current state of the shelves.
     * @return A Pair containing the best product and its similarity score.
     */
    private Pair<Product, Double> findBestProductToPlace(int currentShelfIndex, List<Product> remainingProducts, ArrayList<ShelvingUnit> shelves) {
        ShelvingUnit currentShelf = shelves.get(currentShelfIndex % shelves.size());
        Product bestProduct = null;
        double bestSimilarity = 0;

        int previousShelfIndex = calculatePreviousShelfIndex(currentShelfIndex, shelves.size());
        ShelvingUnit previousShelf = shelves.get(previousShelfIndex % shelves.size());
        int previousHeight = this.shelfHeight - 1 - (previousShelfIndex / shelves.size());

        if (previousHeight < 0 || previousHeight >= this.shelfHeight) {
//            throw new IndexOutOfBoundsException("Height out of bounds: " + previousHeight);
            return new Pair<>(bestProduct, bestSimilarity);
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
     * @param numShelves The total number of shelves.
     * @return The next shelf index to access.
     */
    private int calculateNextShelfIndex(int currentShelfIndex, int numShelves) {
        int height = this.shelfHeight - 1 - (currentShelfIndex / numShelves);
        int direction = (height % 2 == 0) ? 1 : -1;
        int nextIndex = currentShelfIndex + direction;

        if ((currentShelfIndex % numShelves == 0 && direction == -1) || (currentShelfIndex % numShelves == numShelves - 1 && direction == 1)) {
            nextIndex = currentShelfIndex + numShelves;
        }

        return nextIndex;
    }

    /**
     * Calculates the previous index for backtracking purposes.
     * @param currentShelfIndex The current index being backtracked from.
     * @param numShelves The total number of shelves.
     * @return The previous shelf index to access.
     */
    private int calculatePreviousShelfIndex(int currentShelfIndex, int numShelves) {
        int height = this.shelfHeight - 1 - (currentShelfIndex / numShelves);
        int direction = (height % 2 == 0) ? -1 : 1;
        int previousIndex = currentShelfIndex + direction;

        if ((currentShelfIndex % numShelves == 0 && direction == -1) || (currentShelfIndex % numShelves == numShelves - 1 && direction == 1)) {
            previousIndex = currentShelfIndex - numShelves;
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
        if (productA == null || productB == null) {
            return 0;
        }
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
