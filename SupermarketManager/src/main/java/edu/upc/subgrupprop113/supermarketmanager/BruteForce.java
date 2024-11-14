package edu.upc.subgrupprop113.supermarketmanager;

import java.util.ArrayList;
import java.util.List;

import static edu.upc.subgrupprop113.supermarketmanager.HelperFunctions.*;

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
                    int previousShelfIndex = calculatePreviousShelfIndex(currentShelfIndex, shelves.size(), this.shelfHeight);
                    ShelvingUnit previousShelf = shelves.get(previousShelfIndex % shelves.size());
                    int previousHeight = this.shelfHeight - 1 - (previousShelfIndex / shelves.size());
                    Product previousProduct = previousShelf.getProduct(previousHeight);
                    double similarity = calculateSimilarity(previousProduct, candidate);

                    // Place the product and continue recursion
                    currentShelf.addProduct(candidate, height);
                    remainingProducts.remove(candidate);
                    recursivelyPlaceProducts(nextIndex, remainingProducts, shelves, currentScore + similarity);

                    // Backtrack: undo placement
                    currentShelf.removeProduct(height);
                    remainingProducts.add(candidate);

                    placedProduct = true;
                }
            }

            if (!placedProduct) {
                // If no product was placed, continue recursion without placing anything
                recursivelyPlaceProducts(nextIndex, remainingProducts, shelves, currentScore);
            }
        }
    }
}
