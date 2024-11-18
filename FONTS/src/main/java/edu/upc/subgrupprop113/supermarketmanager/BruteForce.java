package edu.upc.subgrupprop113.supermarketmanager;

import java.util.ArrayList;
import java.util.List;

import static edu.upc.subgrupprop113.supermarketmanager.HelperFunctions.*;
/**
 * Class that implements the sorting algorithm by using backtracking.
 * */
public class BruteForce implements OrderingStrategy {

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

        List<Product> remainingProducts = new ArrayList<>(products);

        while (currentShelfIndex < initialShelves.size() * shelfHeight) {
            ShelvingUnit shelf = initialShelves.get(currentShelfIndex % initialShelves.size());
            for (Product startingProduct : products) {
                if (isShelfCompatible(shelf, startingProduct)) {

                    remainingProducts.remove(startingProduct);
                    initialShelves.get(currentShelfIndex % initialShelves.size()).addProduct(startingProduct, currentHeight);

                    int nextIndex = calculateNextShelfIndex(currentShelfIndex, initialShelves.size(), this.shelfHeight);
                    recursivelyPlaceProducts(nextIndex, remainingProducts, (ArrayList<ShelvingUnit>) initialShelves, startingProduct, 0);

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
    private void recursivelyPlaceProducts(int currentShelfIndex, List<Product> remainingProducts, ArrayList<ShelvingUnit> shelves, Product previousProduct, double currentScore) {
        if (remainingProducts.isEmpty() || currentShelfIndex >= shelves.size() * this.shelfHeight) {
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

            int nextIndex = calculateNextShelfIndex(currentShelfIndex, shelves.size(), this.shelfHeight);

            boolean placedProduct = false;

            for (Product candidate : new ArrayList<>(remainingProducts)) {
                if (isShelfCompatible(currentShelf, candidate)) {
                    double similarity = calculateSimilarity(previousProduct, candidate);

                    double maxPossibleScore = currentScore + similarity + remainingProducts.size() + 1.0; // Adds one at the end because it counts the end of the circle with position 0.

                    // If it is the last position, then compute similarity with product in shelfIndex = 0 to make it circular and add it to the currentScore
                    if (isLastPosition(currentShelfIndex, shelves.size(), this.shelfHeight)) {
                        Product startingProduct = shelves.getFirst().getProduct(this.shelfHeight - 1);
                        similarity += calculateSimilarity(startingProduct, candidate);
                        maxPossibleScore -= 1.0;
                    }

                    if (maxPossibleScore <= highestScore) {
                        // If the maximum possible score is lower than the current highest score, stop recursion
                        continue;
                    }

                    // Place the product and continue recursion
                    currentShelf.addProduct(candidate, height);
                    remainingProducts.remove(candidate);

                    recursivelyPlaceProducts(nextIndex, remainingProducts, shelves, candidate, currentScore + similarity);

                    // Backtrack: undo placement
                    currentShelf.removeProduct(height);
                    remainingProducts.add(candidate);

                    placedProduct = true;
                }
            }

            if (!placedProduct) {
                // If no product was placed, continue recursion without placing anything
                recursivelyPlaceProducts(nextIndex, remainingProducts, shelves, null, currentScore);
            }
        }
    }
}
