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
        this.shelfHeight = initialShelves.getFirst().getHeight();
        this.bestScore = Double.POSITIVE_INFINITY;
        this.highestSimilarity = 0.0;
        this.optimalDistribution = deepCopyShelves((ArrayList<ShelvingUnit>) initialShelves, true);

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
     * Recursive function that tries all possible combinations of products in the shelves.
     *
     * @param currentShelfIndex Index of the current shelf.
     * @param remainingProducts List of products that have not been placed yet.
     * @param shelves Current state of the shelves.
     * @param previousProduct Product that was placed in the previous iteration.
     * @param currentScore Sum of inverted similarities accumulated until now.
     * @param currentSimilarity Sum of similarities accumulated until now.
     */
    private void recursivelyPlaceProducts(
            int currentShelfIndex,
            List<Product> remainingProducts,
            ArrayList<ShelvingUnit> shelves,
            Product previousProduct,
            double currentScore,
            double currentSimilarity)
    {
        if (isSolutionComplete(currentShelfIndex, remainingProducts, shelves, shelfHeight)) {
            updateBestSolutionIfNecessary(currentScore, currentSimilarity, shelves);
            return;
        }

        if (shouldPruneBranch(currentScore, currentSimilarity)) {
            return;
        }

        int totalPositions = shelves.size() * shelfHeight;
        int positionsLeft = totalPositions - currentShelfIndex;       // Huecos que quedan por rellenar
        int productsLeft  = remainingProducts.size();                 // Productos que quedan
        int nextShelfIndex = calculateNextShelfIndex(currentShelfIndex, shelves.size(), shelfHeight);
        int height = getShelfHeight(currentShelfIndex, shelves.size(), shelfHeight);
        ShelvingUnit currentShelf = getCurrentShelf(shelves, currentShelfIndex);

        if (productsLeft > 0) {
            for (int i = 0; i < productsLeft; i++) {
                Product candidateProduct = remainingProducts.get(i);

                if (!isShelfCompatible(currentShelf, candidateProduct)) {
                    continue;
                }

                double oldScore        = currentScore;
                double oldSimilarity   = currentSimilarity;
                Product oldPrevious    = previousProduct;

                double similarity = calculateSimilarity(previousProduct, candidateProduct);
                currentSimilarity += similarity;
                currentScore     += (1.0 - similarity);

                // Place the product and proceed with the next one
                currentShelf.addProduct(candidateProduct, height);
                remainingProducts.remove(candidateProduct);
                previousProduct = candidateProduct;

                recursivelyPlaceProducts(
                        nextShelfIndex,
                        remainingProducts,
                        shelves,
                        previousProduct,
                        currentScore,
                        currentSimilarity
                );

                // Reset state (backtracking)
                currentShelf.removeProduct(height);
                remainingProducts.add(i, candidateProduct);
                currentScore      = oldScore;
                currentSimilarity = oldSimilarity;
                previousProduct   = oldPrevious;
            }
        }

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
     * Determines if the solution is complete based on the current shelf index and remaining products.
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
