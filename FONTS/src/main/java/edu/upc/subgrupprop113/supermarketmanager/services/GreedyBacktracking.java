package edu.upc.subgrupprop113.supermarketmanager.services;

import edu.upc.subgrupprop113.supermarketmanager.models.Product;
import edu.upc.subgrupprop113.supermarketmanager.models.ShelvingUnit;
import javafx.util.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
            ShelvingUnit shelf = getCurrentShelf((ArrayList<ShelvingUnit>) initialShelves, currentShelfIndex);
            for (Product startingProduct : products) {
                if (isShelfCompatible(shelf, startingProduct)) {

                    remainingProducts.remove(startingProduct);
                    shelf.addProduct(startingProduct, currentHeight);

                    int nextIndex = calculateNextShelfIndex(currentShelfIndex, initialShelves.size(), this.shelfHeight);
                    recursivelyPlaceProducts(nextIndex, remainingProducts, (ArrayList<ShelvingUnit>) initialShelves, startingProduct, 0, 0);

                    shelf.removeProduct(currentHeight);
                    remainingProducts.add(startingProduct);
                }
            }
            currentShelfIndex = calculateNextShelfIndex(currentShelfIndex, initialShelves.size(), this.shelfHeight);
            currentHeight = getShelfHeight(currentShelfIndex, initialShelves.size(), this.shelfHeight);
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
        if (shouldPruneBranch(currentScore, currentSimilarity)) return;

        if (isSolutionComplete(currentShelfIndex, remainingProducts, shelves, this.shelfHeight)) {
            updateBestSolutionIfNecessary(shelves, currentScore, currentSimilarity);
            return;
        }

        ShelvingUnit currentShelf = getCurrentShelf(shelves, currentShelfIndex);
        int height = getShelfHeight(currentShelfIndex, shelves.size(), this.shelfHeight);
        if (height < 0) {
            // If height is out of bounds, stop recursion
            return;
        }

        int nextIndex = calculateNextShelfIndex(currentShelfIndex, shelves.size(), this.shelfHeight);
        if (previousProduct == null) {
            for (Product bestProduct : new ArrayList<>(remainingProducts)) {
                if (isShelfCompatible(currentShelf, bestProduct)) {
                    handlePlacementAndRecurse(currentShelf, bestProduct, height, remainingProducts, shelves, nextIndex, currentScore, currentSimilarity, 1, 0);
                }
            }
        }
        else {
            Pair<Product, Double> bestProductPair = findBestProductToPlace(currentShelfIndex, remainingProducts, shelves, previousProduct);
            Product bestProduct = bestProductPair.getKey();
            if (bestProduct != null) {
                double bestSimilarity = bestProductPair.getValue();
                double bestInvertedSimilarity = 1 - bestSimilarity;
                handlePlacementAndRecurse(currentShelf, bestProduct, height, remainingProducts, shelves, nextIndex, currentScore, currentSimilarity, bestInvertedSimilarity, bestSimilarity);
            }
            else if (remainingProducts.stream().filter(Objects::nonNull).map(Product::getTemperature).anyMatch(temp -> Objects.equals(temp, currentShelf.getTemperature()))) {
                for (Product product : new ArrayList<>(remainingProducts)) {
                    if (isShelfCompatible(currentShelf, bestProduct)) {
                        handlePlacementAndRecurse(currentShelf, product, height, remainingProducts, shelves, nextIndex, currentScore, currentSimilarity, 1, 0);
                    }
                }
            }
            else {
                // No compatible product found for this shelf and height
                // Proceed to the next index
                currentShelf.removeProduct(height);
                recursivelyPlaceProducts(nextIndex, remainingProducts, shelves, null, currentScore, currentSimilarity);
            }
        }
    }

    /**
     * Determines if the current branch should be pruned based on the current score and similarity.
     * @param currentScore The current accumulated inverted similarity.
     * @param currentSimilarity The current accumulated similarity score.
     * @return True if the branch should be pruned, false otherwise.
     */
    private boolean shouldPruneBranch(double currentScore, double currentSimilarity) {
        return currentScore >= this.bestScore && currentSimilarity <= this.highestSimilarity;
    }

    /**
     * Updates the best solution if the current score is better and the similarity is higher.
     * @param shelves The current state of the shelves.
     * @param currentScore The current accumulated inverted similarity score.
     * @param currentSimilarity The current accumulated similarity score.
     */
    private void updateBestSolutionIfNecessary(ArrayList<ShelvingUnit> shelves, double currentScore, double currentSimilarity) {
        if (currentScore < this.bestScore && currentSimilarity > this.highestSimilarity) {
            this.optimalDistribution = deepCopyShelves(shelves, false);
            this.bestScore = currentScore;
            this.highestSimilarity = currentSimilarity;
        }
    }

    /**
     * Handles the placement of the best product and recurses to the next index.
     * @param currentShelf The current shelf.
     * @param bestProduct The best product to be placed.
     * @param height The height at which the product will be placed.
     * @param remainingProducts The products that still need to be placed.
     * @param shelves The current state of the shelves.
     * @param nextIndex The next index to be accessed.
     * @param currentScore The current accumulated inverted similarity score.
     * @param currentSimilarity The current accumulated similarity score.
     * @param bestInvertedSimilarity The inverted similarity score of the best product.
     * @param bestSimilarity The similarity score of the best product.
     */
    private void handlePlacementAndRecurse(ShelvingUnit currentShelf, Product bestProduct, int height, List<Product> remainingProducts, ArrayList<ShelvingUnit> shelves, int nextIndex, double currentScore, double currentSimilarity, double bestInvertedSimilarity, double bestSimilarity) {
        if (isLastPosition(nextIndex, shelves.size(), this.shelfHeight)) {
            Product startingProduct = shelves.getFirst().getProduct(this.shelfHeight - 1);
            double lastSimilarity = calculateSimilarity(startingProduct, bestProduct);
            bestSimilarity += lastSimilarity;
            bestInvertedSimilarity += 1 - lastSimilarity;
        }

        currentShelf.addProduct(bestProduct, height);
        remainingProducts.remove(bestProduct);

        recursivelyPlaceProducts(nextIndex, remainingProducts, shelves, bestProduct, currentScore + bestInvertedSimilarity, currentSimilarity + bestSimilarity);

        currentShelf.removeProduct(height);
        remainingProducts.add(bestProduct);
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
        double bestSimilarity = -1;

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
