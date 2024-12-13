package edu.upc.subgrupprop113.supermarketmanager.services;

import edu.upc.subgrupprop113.supermarketmanager.models.Product;
import edu.upc.subgrupprop113.supermarketmanager.models.ShelvingUnit;
import javafx.util.Pair;
import java.util.ArrayList;
import java.util.List;

import static edu.upc.subgrupprop113.supermarketmanager.utils.HelperFunctions.*;

/**
 * Class that implements the sorting algorithm by using a greedy approach.
 * */
public class GreedyBacktracking implements OrderingStrategy {

    private int shelfHeight;
    private double bestScore;
    private double highestSimilarity;
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
        this.bestScore = Double.POSITIVE_INFINITY;
        this.highestSimilarity = 0.0;
        this.optimalDistribution = deepCopyShelves((ArrayList<ShelvingUnit>) initialShelves, true);

        int currentShelfIndex = 0;
        int currentHeight = this.shelfHeight - 1;

        List<Product> remainingProducts = new ArrayList<>(products);

        while (currentShelfIndex < initialShelves.size() * shelfHeight) {
            ShelvingUnit shelf = initialShelves.get(currentShelfIndex % initialShelves.size());
            for (Product startingProduct : products) {
                if (isShelfCompatible(shelf, startingProduct)) {

                    remainingProducts.remove(startingProduct);
                    initialShelves.get(currentShelfIndex % initialShelves.size()).addProduct(startingProduct, currentHeight);

                    int nextIndex = calculateNextShelfIndex(currentShelfIndex, initialShelves.size(), this.shelfHeight);
                    recursivelyPlaceProducts(nextIndex, remainingProducts, (ArrayList<ShelvingUnit>) initialShelves, startingProduct, 0, 0);

                    initialShelves.get(currentShelfIndex % initialShelves.size()).removeProduct(currentHeight);
                    remainingProducts.add(startingProduct);
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
    private void recursivelyPlaceProducts(int currentShelfIndex, List<Product> remainingProducts, ArrayList<ShelvingUnit> shelves, Product previousProduct, double currentScore, double currentSimilarity) {
        if (currentScore >= this.bestScore && currentSimilarity <= this.highestSimilarity) {
            // If the current score is greater than the best score, stop recursion
            return;
        }

        // No more remaining products or all shelves are visited
        if (remainingProducts.isEmpty() || currentShelfIndex >= shelves.size() * this.shelfHeight) {
            if (currentScore < this.bestScore && currentSimilarity > this.highestSimilarity) {
                this.optimalDistribution = deepCopyShelves(shelves, false);
                this.bestScore = currentScore;
                this.highestSimilarity = currentSimilarity;
            }
            return;
        }

        ShelvingUnit currentShelf = shelves.get(currentShelfIndex % shelves.size());
        int height = this.shelfHeight - 1 - (currentShelfIndex / shelves.size());
        if (height < 0) {
            // If height is out of bounds, stop recursion
            return;
        }

        Pair<Product, Double> bestProductPair = findBestProductToPlace(currentShelfIndex, remainingProducts, shelves, previousProduct);
        Product bestProduct = bestProductPair.getKey();
        double bestSimilarity = bestProductPair.getValue();
        double bestInvertedSimilarity = 1 - bestSimilarity;

        int nextIndex = calculateNextShelfIndex(currentShelfIndex, shelves.size(), this.shelfHeight);

        if (bestProduct != null) {

            if (isLastPosition(currentShelfIndex, shelves.size(), this.shelfHeight)) {
                Product startingProduct = shelves.getFirst().getProduct(this.shelfHeight - 1);
                double lastSimilarity = calculateSimilarity(startingProduct, bestProduct);
                bestSimilarity += lastSimilarity;
                bestInvertedSimilarity += 1 - lastSimilarity;
            }

            // Place the best product found
            currentShelf.addProduct(bestProduct, height);
            remainingProducts.remove(bestProduct);

            // Recur to place the remaining products
            recursivelyPlaceProducts(nextIndex, remainingProducts, shelves, bestProduct, currentScore + bestInvertedSimilarity, currentSimilarity + bestSimilarity);
        } else {
            // No compatible product found for this shelf and height
            // Proceed to the next index
            recursivelyPlaceProducts(nextIndex, remainingProducts, shelves, null, currentScore, currentSimilarity);
        }
    }

    /**
     * Finds the next best product to place based on similarity and compatibility with the current shelf.
     * @param currentShelfIndex The index of the current shelf.
     * @param remainingProducts The products that need to be placed.
     * @param shelves The current state of the shelves.
     * @return A Pair containing the best product and its similarity score.
     */
    private Pair<Product, Double> findBestProductToPlace(int currentShelfIndex, List<Product> remainingProducts, ArrayList<ShelvingUnit> shelves, Product previousProduct) {
        ShelvingUnit currentShelf = shelves.get(currentShelfIndex % shelves.size());
        Product bestProduct = null;
        double bestSimilarity = 0;

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
