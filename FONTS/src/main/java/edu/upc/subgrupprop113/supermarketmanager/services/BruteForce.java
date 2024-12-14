package edu.upc.subgrupprop113.supermarketmanager.services;

import edu.upc.subgrupprop113.supermarketmanager.models.Product;
import edu.upc.subgrupprop113.supermarketmanager.models.ShelvingUnit;

import java.util.ArrayList;
import java.util.List;

import static edu.upc.subgrupprop113.supermarketmanager.utils.HelperFunctions.*;
/**
 * Class that implements the sorting algorithm by using backtracking.
 * */
public class BruteForce implements OrderingStrategy {

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

        List<Product> mutableProducts = new ArrayList<>(products);

        recursivelyPlaceProducts(0, mutableProducts, (ArrayList<ShelvingUnit>) initialShelves, null, 0, 0);

        return (ArrayList<ShelvingUnit>) this.optimalDistribution;
    }

    /**
     * Recursively places all products on shelves using a backtracking approach.
     * @param currentShelfIndex The index of the current shelf being filled.
     * @param remainingProducts The list of products that still need to be placed.
     * @param shelves The current state of the shelves.
     * @param currentScore The current accumulated inverted similarity score for the placement.
     * @param currentSimilarity The current accumulated similarity score for the placement.
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

        // Keep exploring the search space
        ShelvingUnit currentShelf = shelves.get(currentShelfIndex % shelves.size());
        int height = this.shelfHeight - 1 - (currentShelfIndex / shelves.size());

        if (height < 0) return;

        int nextIndex = calculateNextShelfIndex(currentShelfIndex, shelves.size(), this.shelfHeight);

        boolean placedProduct = false;

        for (Product candidate : new ArrayList<>(remainingProducts)) {
            if (isShelfCompatible(currentShelf, candidate)) {
                double similarity = calculateSimilarity(previousProduct, candidate); // We invert the similarity to prune solutions that are greater than the best score
                double invertedSimilarity = 1 - similarity;

                // If it is the last position, then compute similarity with product in shelfIndex = 0 to make it circular and add it to the currentScore
                if (isLastPosition(currentShelfIndex, shelves.size(), this.shelfHeight)) {
                    Product startingProduct = shelves.getFirst().getProduct(this.shelfHeight - 1);
                    double lastSimilarity = calculateSimilarity(startingProduct, candidate);
                    similarity += lastSimilarity;
                    invertedSimilarity += 1 - lastSimilarity;
                }

                // Place the product and continue recursion
                currentShelf.addProduct(candidate, height);
                remainingProducts.remove(candidate);

                recursivelyPlaceProducts(nextIndex, remainingProducts, shelves, candidate, currentScore + invertedSimilarity, currentSimilarity + similarity);

                // Backtrack: undo placement
                currentShelf.removeProduct(height);
                remainingProducts.add(candidate);

                placedProduct = true;
            }
        }

        if (!placedProduct) {
            // If no product was placed, continue recursion without placing anything
            recursivelyPlaceProducts(nextIndex, remainingProducts, shelves, null, currentScore, currentSimilarity);
        }
    }

}

