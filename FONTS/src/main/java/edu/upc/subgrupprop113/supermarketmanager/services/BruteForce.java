package edu.upc.subgrupprop113.supermarketmanager.services;

import edu.upc.subgrupprop113.supermarketmanager.models.Product;
import edu.upc.subgrupprop113.supermarketmanager.models.ShelvingUnit;

import java.util.ArrayList;
import java.util.List;

import static edu.upc.subgrupprop113.supermarketmanager.utils.HelperFunctions.*;

/**
 * Represents the brute force algorithm to order the supermarket shelves.
 */
public class BruteForce implements OrderingStrategy {

    private int shelfHeight;
    private double bestScore;
    private double highestSimilarity;
    private List<ShelvingUnit> optimalDistribution;

    /**
     * Orders the supermarket shelves using a brute force algorithm.
     *
     * @param initialShelves List of initial shelves.
     * @param products List of products to be placed in the shelves.
     * @return List of shelves with the products placed in the optimal way.
     */
    @Override
    public ArrayList<ShelvingUnit> orderSupermarket(List<ShelvingUnit> initialShelves, List<Product> products) {
        initializeState(initialShelves);

        recursivelyPlaceProducts(
                0,
                new ArrayList<>(products),
                (ArrayList<ShelvingUnit>) initialShelves,
                null,
                0.0,
                0.0
        );

        return (ArrayList<ShelvingUnit>) this.optimalDistribution;
    }

    /**
     * Initializes the state variables for the algorithm.
     *
     * @param initialShelves The initial state of the shelves.
     */
    private void initializeState(List<ShelvingUnit> initialShelves) {
        this.shelfHeight = initialShelves.getFirst().getHeight();
        this.bestScore = Double.POSITIVE_INFINITY;
        this.highestSimilarity = 0.0;
        this.optimalDistribution = deepCopyShelves((ArrayList<ShelvingUnit>) initialShelves, true);
    }

    /**
     * Recursive function that tries all possible combinations of products in the shelves.
     *
     * @param currentShelfIndex Index of the current shelf.
     * @param remainingProducts List of products that have not been placed yet.
     * @param shelves Current state of the shelves.
     * @param previousProduct Product that was placed in the previous iteration.
     * @param currentScore Sum of inverted similarities accumulated until now.
     * @param currentSimilarity Sum of similarities accumulated until now.
     */
    private void recursivelyPlaceProducts(int currentShelfIndex, List<Product> remainingProducts, ArrayList<ShelvingUnit> shelves, Product previousProduct, double currentScore, double currentSimilarity) {
        if (isSolutionComplete(currentShelfIndex, remainingProducts, shelves, shelfHeight)) {
            updateBestSolutionIfNecessary(currentScore, currentSimilarity, shelves);
            return;
        }

        if (shouldPruneBranch(currentScore, currentSimilarity)) {
            return;
        }

        int nextShelfIndex = calculateNextShelfIndex(currentShelfIndex, shelves.size(), shelfHeight);
        int height = getShelfHeight(currentShelfIndex, shelves.size(), shelfHeight);
        ShelvingUnit currentShelf = getCurrentShelf(shelves, currentShelfIndex);

        tryPlacingProducts(
                remainingProducts,
                shelves,
                previousProduct,
                currentScore,
                currentSimilarity,
                nextShelfIndex,
                height,
                currentShelf
        );

        tryLeavingPositionEmpty(
                currentShelfIndex,
                remainingProducts,
                shelves,
                currentScore,
                currentSimilarity,
                nextShelfIndex
        );
    }

    /**
     * Tries placing all products that are compatible on the current shelf.
     *
     * @param remainingProducts List of products that have not been placed yet.
     * @param shelves Current state of the shelves.
     * @param previousProduct Product that was placed in the previous iteration.
     * @param currentScore Sum of inverted similarities accumulated until now.
     * @param currentSimilarity Sum of similarities accumulated until now.
     * @param nextShelfIndex Index of the next shelf.
     * @param height Height of the current shelf.
     * @param currentShelf Current shelf.
     */
    private void tryPlacingProducts(List<Product> remainingProducts, ArrayList<ShelvingUnit> shelves, Product previousProduct, double currentScore, double currentSimilarity, int nextShelfIndex, int height, ShelvingUnit currentShelf) {
        for (int i = 0; i < remainingProducts.size(); i++) {
            Product candidateProduct = remainingProducts.get(i);

            if (!isShelfCompatible(currentShelf, candidateProduct)) {
                continue;
            }

            double similarity = calculateSimilarity(previousProduct, candidateProduct);
            double updatedScore = currentScore + (1.0 - similarity);
            double updatedSimilarity = currentSimilarity + similarity;

            // Place product and recurse
            placeProductAndRecurse(
                    currentShelf,
                    height,
                    candidateProduct,
                    remainingProducts,
                    i,
                    shelves,
                    nextShelfIndex,
                    updatedScore,
                    updatedSimilarity
            );
        }
    }

    /**
     * Places a product on the shelf, recurses, and performs backtracking.
     *
     * @param currentShelf Current shelf.
     * @param height Height of the current shelf.
     * @param candidateProduct Product to be placed.
     * @param remainingProducts List of products that have not been placed yet.
     * @param productIndex Index of the product to be placed.
     * @param shelves Current state of the shelves.
     * @param nextShelfIndex Index of the next shelf.
     * @param updatedScore Updated score after placing the product.
     * @param updatedSimilarity Updated similarity after placing the product.
     */
    private void placeProductAndRecurse(ShelvingUnit currentShelf, int height, Product candidateProduct, List<Product> remainingProducts, int productIndex, ArrayList<ShelvingUnit> shelves, int nextShelfIndex, double updatedScore, double updatedSimilarity) {
        // Place product
        currentShelf.addProduct(candidateProduct, height);
        remainingProducts.remove(productIndex);

        // Recursive call
        recursivelyPlaceProducts(
                nextShelfIndex,
                remainingProducts,
                shelves,
                candidateProduct,
                updatedScore,
                updatedSimilarity
        );

        // Backtrack
        currentShelf.removeProduct(height);
        remainingProducts.add(productIndex, candidateProduct);
    }

    /**
     * Tries leaving the current position empty if allowed.
     *
     * @param currentShelfIndex Index of the current shelf.
     * @param remainingProducts List of products that have not been placed yet.
     * @param shelves Current state of the shelves.
     * @param currentScore Sum of inverted similarities accumulated until now.
     * @param currentSimilarity Sum of similarities accumulated until now.
     * @param nextShelfIndex Index of the next shelf.
     */
    private void tryLeavingPositionEmpty(int currentShelfIndex, List<Product> remainingProducts, ArrayList<ShelvingUnit> shelves, double currentScore, double currentSimilarity, int nextShelfIndex) {
        int totalPositions = shelves.size() * shelfHeight;
        int positionsLeft = totalPositions - currentShelfIndex;
        int productsLeft = remainingProducts.size();

        if (positionsLeft > productsLeft) {
            recursivelyPlaceProducts(
                    nextShelfIndex,
                    remainingProducts,
                    shelves,
                    null,
                    currentScore,
                    currentSimilarity
            );
        }
    }

    /**
     * Determines if the current branch should be pruned.
     *
     * @param currentScore Sum of inverted similarities accumulated until now.
     * @param currentSimilarity Sum of similarities accumulated until now.
     */
    private boolean shouldPruneBranch(double currentScore, double currentSimilarity) {
        return (currentScore >= this.bestScore) && (currentSimilarity <= this.highestSimilarity);
    }

    /**
     * Updates the best solution found so far if the current one is better.
     *
     * @param currentScore Sum of inverted similarities accumulated until now.
     * @param currentSimilarity Sum of similarities accumulated until now.
     * @param shelves Current state of the shelves.
     */
    private void updateBestSolutionIfNecessary(double currentScore, double currentSimilarity, List<ShelvingUnit> shelves) {
        if (currentScore < this.bestScore && currentSimilarity > this.highestSimilarity) {
            this.optimalDistribution = deepCopyShelves((ArrayList<ShelvingUnit>) shelves, false);
            this.bestScore = currentScore;
            this.highestSimilarity = currentSimilarity;
        }
    }
}
