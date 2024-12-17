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

        recursivelyPlaceProducts(0, new ArrayList<>(products), (ArrayList<ShelvingUnit>) initialShelves, null, 0, 0);

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
        if (shouldPruneBranch(currentScore, currentSimilarity)) return;

        if (isSolutionComplete(currentShelfIndex, remainingProducts, shelves, this.shelfHeight)) {
            updateBestSolutionIfNecessary(currentScore, currentSimilarity, shelves);
            return;
        }

        // Keep exploring the search space
        ShelvingUnit currentShelf = getCurrentShelf(shelves, currentShelfIndex);
        int height = getShelfHeight(currentShelfIndex, shelves.size(), this.shelfHeight);

        if (height < 0) return;

        int nextIndex = calculateNextShelfIndex(currentShelfIndex, shelves.size(), this.shelfHeight);

        boolean placedProduct = false;

        for (Product candidate : new ArrayList<>(remainingProducts)) {
            if (isShelfCompatible(currentShelf, candidate)) {
                placedProduct = true;
                handlePlacementAndRecurse(candidate, currentShelf, height, previousProduct, currentShelfIndex, nextIndex, shelves, remainingProducts, currentScore, currentSimilarity);
            }
        }

        if (!placedProduct) {
            // If no product was placed, continue recursion removing the current product if exists
            currentShelf.removeProduct(height);
            recursivelyPlaceProducts(nextIndex, remainingProducts, shelves, null, currentScore, currentSimilarity);
        }
    }

    /**
     * Determines if the current branch should be pruned based on the current score and similarity.
     * @param currentScore The current accumulated inverted similarity score for the placement.
     * @param currentSimilarity The current accumulated similarity score for the placement.
     * @return True if the branch should be pruned, false otherwise.
     */
    private boolean shouldPruneBranch(double currentScore, double currentSimilarity) {
        return currentScore >= this.bestScore && currentSimilarity <= this.highestSimilarity;
    }

    /**
     * Updates the best solution found so far if the current solution is better.
     * @param shelves The current state of the shelves.
     * @param currentScore The current accumulated inverted similarity score for the placement.
     * @param currentSimilarity The current accumulated similarity score for the placement.
     */
    private void updateBestSolutionIfNecessary(double currentScore, double currentSimilarity, List<ShelvingUnit> shelves) {
        if (currentScore < this.bestScore && currentSimilarity > this.highestSimilarity) {
            this.optimalDistribution = deepCopyShelves((ArrayList<ShelvingUnit>) shelves, false);
            this.bestScore = currentScore;
            this.highestSimilarity = currentSimilarity;
        }
    }

    /**
     * Handles the placement of a product and recurses to the next index.
     * @param candidate The product to be placed.
     * @param currentShelf The current shelf being filled.
     * @param height The height at which the product should be placed.
     * @param previousProduct The product that was placed before the current one.
     * @param currentShelfIndex The index of the current shelf being filled.
     * @param nextIndex The index of the next shelf to be filled.
     * @param shelves The current state of the shelves.
     * @param remainingProducts The list of products that still need to be placed.
     * @param currentScore The current accumulated inverted similarity score for the placement.
     * @param currentSimilarity The current accumulated similarity score for the placement.
     */
    private void handlePlacementAndRecurse(Product candidate, ShelvingUnit currentShelf, int height, Product previousProduct, int currentShelfIndex, int nextIndex, ArrayList<ShelvingUnit> shelves, List<Product> remainingProducts, double currentScore, double currentSimilarity) {
        double similarity = calculateSimilarity(previousProduct, candidate);
        double invertedSimilarity = 1 - similarity;

        if (isLastPosition(currentShelfIndex, shelves.size(), this.shelfHeight)) {
            Product startingProduct = shelves.getFirst().getProduct(this.shelfHeight - 1);
            double lastSimilarity = calculateSimilarity(startingProduct, candidate);
            similarity += lastSimilarity;
            invertedSimilarity += 1 - lastSimilarity;
        }

        currentShelf.addProduct(candidate, height);
        remainingProducts.remove(candidate);

        recursivelyPlaceProducts(nextIndex, remainingProducts, shelves, candidate, currentScore + invertedSimilarity, currentSimilarity + similarity);

        currentShelf.removeProduct(height);
        remainingProducts.add(candidate);
    }

}

