package edu.upc.subgrupprop113.supermarketmanager;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class BruteForce implements OrderingStrategy {

    private int shelvesHeight;
    private double maxScore;
    private ArrayList<ShelvingUnit> bestDistribution;

    @Override
    public ArrayList<ShelvingUnit> orderSupermarket(ArrayList<ShelvingUnit> origDistribution, Set<Product> products) {

        this.shelvesHeight = origDistribution.getFirst().getHeight();
        this.maxScore = -1;
        this.bestDistribution = null;

        // Start with each product and build the best arrangement with backtracking
        for (Product startingProduct : products) {
            if (isShelfCompatible(origDistribution.getFirst(), startingProduct)) {
                Set<Product> remainingProducts = new HashSet<>(products);
                remainingProducts.remove(startingProduct);

                ArrayList<ShelvingUnit> currentDistribution = deepCopyShelvingUnits(origDistribution);
                currentDistribution.getFirst().addProduct(startingProduct, this.shelvesHeight - 1);

                placeAllProducts(1, remainingProducts, currentDistribution, 0);
            }
        }

        return this.bestDistribution;
    }

    // Backtracking method to place all products
    private void placeAllProducts(int currentShelfIndex, Set<Product> remainingProducts, ArrayList<ShelvingUnit> shelves, double currentScore)
    {
        if (remainingProducts.isEmpty()) {
            if (currentScore > maxScore) {
                this.bestDistribution = deepCopyShelvingUnits(shelves);
                this.maxScore = currentScore;
            }
        }
        else {
            ShelvingUnit currentShelf = shelves.get(currentShelfIndex % shelves.size());
            int height = Math.abs(currentShelfIndex / this.shelvesHeight - this.shelvesHeight - 1);

            // Use the findBestProduct function to always attempt placing the best possible product
            Pair<Product, Double> bestProductPair = findBestProduct(currentShelfIndex, remainingProducts, shelves);
            Product bestProduct = bestProductPair.getKey();
            double bestSimilarity = bestProductPair.getValue();

            if (bestProduct != null) {
                // Place the best product
                currentShelf.addProduct(bestProduct, height);
                remainingProducts.remove(bestProduct);

                // Recurse to place the rest of the products
                int nextIndex = getNextShelfIndex(currentShelfIndex, height, shelves.size());
                placeAllProducts(nextIndex, remainingProducts, shelves, currentScore + bestSimilarity);

                // Backtrack: if placing subsequent products fails, undo placement
                currentShelf.removeProduct(height);
                remainingProducts.add(bestProduct);
            }
        }
    }

    // Finds the best next product to place based on similarity and compatibility with the current shelves
    private Pair<Product, Double> findBestProduct(int currentShelfIndex, Set<Product> remainingProducts, ArrayList<ShelvingUnit> shelves) {
        ShelvingUnit currentShelf = shelves.get(currentShelfIndex % shelves.size());
        Product bestProduct = null;
        double bestSimilarity = 0;

        if (currentShelfIndex > 0) {
            ShelvingUnit previousShelf = shelves.get((currentShelfIndex - 1) % shelves.size());
            int previousHeight = Math.abs((currentShelfIndex - 1) / this.shelvesHeight - this.shelvesHeight - 1);
            Product previousProduct = previousShelf.getProduct(previousHeight);

            for (Product candidate : remainingProducts) {
                if (isShelfCompatible(currentShelf, candidate)) {
                    double similarity = getSimilarity(previousProduct, candidate);
                    if (similarity > bestSimilarity) {
                        bestSimilarity = similarity;
                        bestProduct = candidate;
                    }
                }
            }
        }

        return new Pair<>(bestProduct, bestSimilarity);
    }

    // Determines which is the next index to access after a specific shelf index at a certain height
    private int getNextShelfIndex(int currentShelfIndex, int height, int n)
    {
        int nextIndex;
        if (this.shelvesHeight % 2 == 0)
        {
            if ((currentShelfIndex == n - 1 && height % 2 != 0) || (currentShelfIndex == 0 && height % 2 == 0)) {
                nextIndex = currentShelfIndex + n;
            }
            else if (height % 2 != 0) nextIndex = currentShelfIndex + 1;
            else nextIndex = currentShelfIndex - 1;
        }
        else
        {
            if ((currentShelfIndex == n - 1 && height % 2 == 0) || (currentShelfIndex == 0 && height % 2 != 0)) {
                nextIndex = currentShelfIndex + n;
            }
            else if (height % 2 == 0) nextIndex = currentShelfIndex + 1;
            else nextIndex = currentShelfIndex - 1;
        }

        return nextIndex;
    }

    // Test if a product is compatible with a shelf
    private Boolean isShelfCompatible(ShelvingUnit shelf, Product product) {
        return shelf.getTemperature() == product.getTemperature();
    }

    // Retrieve similarity between two products from SimilitudDeProductes
    private double getSimilarity(Product productA, Product productB) {
        return productA.getRelatedValue(productB);
    }

    // Creates a deep copy of the shelving units
    private ArrayList<ShelvingUnit> deepCopyShelvingUnits(ArrayList<ShelvingUnit> originalShelves) {
        ArrayList<ShelvingUnit> copiedShelves = new ArrayList<>();
        for (ShelvingUnit shelf : originalShelves) {
            ShelvingUnit copy = new ShelvingUnit(shelf); // Using the copy constructor
            copy.emptyShelvingUnit(); // Ensure the copy has no products
            copiedShelves.add(copy);
        }
        return copiedShelves;
    }
}
