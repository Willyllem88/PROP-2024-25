package edu.upc.subgrupprop113.supermarketmanager;

import javafx.util.Pair;
import java.util.ArrayList;
import java.util.List;

import static edu.upc.subgrupprop113.supermarketmanager.HelperFunctions.*;

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

                    int nextIndex = calculateNextShelfIndex(currentShelfIndex, initialShelves.size(), this.shelfHeight);
                    recursivelyPlaceProducts(nextIndex, remainingProducts, currentShelves, 0);
                }
            }
            currentShelfIndex = calculateNextShelfIndex(currentShelfIndex, initialShelves.size(), this.shelfHeight);
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

            int nextIndex = calculateNextShelfIndex(currentShelfIndex, shelves.size(), this.shelfHeight);

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

        int previousShelfIndex = calculatePreviousShelfIndex(currentShelfIndex, shelves.size(), this.shelfHeight);
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
}
