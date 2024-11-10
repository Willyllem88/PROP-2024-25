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
        this.bestDistribution = deepCopyShelvingUnits(origDistribution, true);

        // Start with each product and build the best arrangement with backtracking
        for (Product startingProduct : products) {
            if (isShelfCompatible(origDistribution.getFirst(), startingProduct)) {
                Set<Product> remainingProducts = new HashSet<>(products);
                remainingProducts.remove(startingProduct);

                ArrayList<ShelvingUnit> currentDistribution = deepCopyShelvingUnits(origDistribution, true);
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
                this.bestDistribution = deepCopyShelvingUnits(shelves, false);
                this.maxScore = currentScore;
            }
        }
        else {
            ShelvingUnit currentShelf = shelves.get(currentShelfIndex % shelves.size());
            int height = this.shelvesHeight - 1 - (currentShelfIndex / shelves.size());

            // Use the findBestProduct function to always attempt placing the best possible product
            Pair<Product, Double> bestProductPair = findBestProduct(currentShelfIndex, remainingProducts, shelves, height);
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
    private Pair<Product, Double> findBestProduct(int currentShelfIndex, Set<Product> remainingProducts, ArrayList<ShelvingUnit> shelves, int height) {
        ShelvingUnit currentShelf = shelves.get(currentShelfIndex % shelves.size());
        Product bestProduct = null;
        double bestSimilarity = 0;

            int previousShelfIndex = getPreviousShelfIndex(currentShelfIndex, height, shelves.size());
            ShelvingUnit previousShelf = shelves.get(previousShelfIndex % shelves.size());
            int previousHeight = this.shelvesHeight - 1 - (previousShelfIndex / shelves.size());
            if (previousHeight < 0 || previousHeight >= this.shelvesHeight) {
                throw new IndexOutOfBoundsException("Height out of bounds: " + previousHeight);
            }
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


        return new Pair<>(bestProduct, bestSimilarity);
    }

    // Determines which is the next index to access after a specific shelf index at a certain height
    private int getNextShelfIndex(int currentShelfIndex, int height, int n)
    {
        int nextIndex;
        if (this.shelvesHeight % 2 == 0)
        {
            if ((currentShelfIndex % n == (n - 1) && height % 2 != 0) || (currentShelfIndex % n == 0 && height % 2 == 0)) {
                nextIndex = currentShelfIndex + n;
            }
            else if (height % 2 != 0) nextIndex = currentShelfIndex + 1;
            else nextIndex = currentShelfIndex - 1;
        }
        else
        {
            if ((currentShelfIndex % n == (n - 1) && height % 2 == 0) || (currentShelfIndex % n == 0 && height % 2 != 0)) {
                nextIndex = currentShelfIndex + n;
            }
            else if (height % 2 == 0) nextIndex = currentShelfIndex + 1;
            else nextIndex = currentShelfIndex - 1;
        }

        if (nextIndex < 0 || nextIndex >= n * shelvesHeight) {
            throw new IndexOutOfBoundsException("Next index out of bounds: " + nextIndex);
        }

        return nextIndex;
    }

    private int getPreviousShelfIndex(int currentShelfIndex, int height, int n)
    {
        int previousIndex;
        if (this.shelvesHeight % 2 == 0)
        {
            if ((currentShelfIndex % n == (n - 1) && height % 2 == 0) || (currentShelfIndex % n == 0 && height % 2 != 0)) {
                previousIndex = currentShelfIndex - n;
            }
            else if (height % 2 != 0) previousIndex = currentShelfIndex - 1;
            else previousIndex = currentShelfIndex + 1;
        }
        else
        {
            if ((currentShelfIndex % n == (n - 1) && height % 2 != 0) || (currentShelfIndex % n == 0 && height % 2 == 0)) {
                previousIndex = currentShelfIndex - n;
            }
            else if (height % 2 == 0) previousIndex = currentShelfIndex - 1;
            else previousIndex = currentShelfIndex + 1;
        }

        if (previousIndex < 0 || previousIndex >= n * shelvesHeight) {
            throw new IndexOutOfBoundsException("Previous index out of bounds: " + previousIndex);
        }

        return previousIndex;
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
    private ArrayList<ShelvingUnit> deepCopyShelvingUnits(ArrayList<ShelvingUnit> originalShelves, boolean empty) {
        ArrayList<ShelvingUnit> copiedShelves = new ArrayList<>();
        for (ShelvingUnit shelf : originalShelves) {
            ShelvingUnit copy = new ShelvingUnit(shelf); // Using the copy constructor
            if (empty) copy.emptyShelvingUnit(); // Ensure the copy has no products
            copiedShelves.add(copy);
        }
        return copiedShelves;
    }
}
