package edu.upc.subgrupprop113.supermarketmanager.utils;

import edu.upc.subgrupprop113.supermarketmanager.models.Product;
import edu.upc.subgrupprop113.supermarketmanager.models.ShelvingUnit;
import edu.upc.subgrupprop113.supermarketmanager.models.Supermarket;

import java.util.ArrayList;

/**
 * Class that implements useful functions for all the three sorting algorithms.
 */
//TODO: enhance the name of the class
public class HelperFunctions {

    /**
     * Calculates the total similarity score of a given state.
     * @param shelves The list of shelving units to be evaluated.
     * @return The total similarity score of the state.
     */
    public static double calculateTotalSimilarity(ArrayList<ShelvingUnit> shelves) {
        double totalSimilarity = 0.0;

        int shelfHeight = shelves.getFirst().getHeight();
        int numShelves = shelves.size();
        int totalPositions = numShelves * shelfHeight;

        int i = 0;
        while (i < totalPositions) {
            int currentShelfIndex = i % numShelves;
            int heightIndex = shelfHeight - 1 - (i / numShelves);
            i = calculateNextShelfIndex(i, numShelves, shelfHeight);
            if (i < 0 || i >= totalPositions) break;
            int nextShelfIndex = i % numShelves;
            int nextHeightIndex = shelfHeight - 1 - (i / numShelves);

            Product currentProduct = shelves.get(currentShelfIndex).getProduct(heightIndex);
            Product nextProduct = shelves.get(nextShelfIndex).getProduct(nextHeightIndex);

            totalSimilarity += calculateSimilarity(currentProduct, nextProduct);

            if (isLastPosition(i, numShelves, shelfHeight)) {
                Product startingProduct = shelves.getFirst().getProduct(shelfHeight - 1);
                totalSimilarity += calculateSimilarity(startingProduct, nextProduct);
            }
        }
        return totalSimilarity;
    }

    /**
     * Creates a deep copy of the list of shelving units.
     * @param originalShelves The original list of shelves.
     * @param empty Whether the shelves should be emptied during the copying process.
     * @return A deep copy of the shelving units.
     */
    public static ArrayList<ShelvingUnit> deepCopyShelves(ArrayList<ShelvingUnit> originalShelves, boolean empty) {
        ArrayList<ShelvingUnit> copiedShelves = new ArrayList<>();
        for (ShelvingUnit shelf : originalShelves) {
            ShelvingUnit copy = new ShelvingUnit(shelf); // Using the copy constructor
            if (empty) {
                copy.emptyShelvingUnit(); // Ensure the copy has no products
            }
            copiedShelves.add(copy);
        }
        return copiedShelves;
    }

    /**
     * Calculates the similarity between two products.
     * @param productA The first product.
     * @param productB The second product.
     * @return The similarity score between the two products.
     */
    public static double calculateSimilarity(Product productA, Product productB) {
        double similarity = 0.0;
        if (productA != null && productB != null) similarity = productA.getRelatedValue(productB);
        return similarity;
    }

    /**
     * Checks if a product can be placed on a given shelf based on temperature compatibility.
     * @param shelf The shelf to be checked.
     * @param product The product to be placed.
     * @return True if the product is compatible, false otherwise.
     */
    public static Boolean isShelfCompatible(ShelvingUnit shelf, Product product) {
        return shelf.getTemperature() == product.getTemperature();
    }

    /**
     * Calculates the next index to place a product, moving across shelves and heights.
     * @param shelfIndex The index of the current shelf.
     * @param numShelves The total number of shelves.
     * @param shelfHeight The height of the shelves.
     * @return The next shelf index to access.
     */
    public static int calculateNextShelfIndex(int shelfIndex, int numShelves, int shelfHeight) {
        int height = shelfHeight - 1 - (shelfIndex / numShelves);
        int direction = (height % 2 == 0) ? 1 : -1;
        int nextIndex = shelfIndex + direction;

        if ((shelfIndex % numShelves == 0 && direction == -1) || (shelfIndex % numShelves == numShelves - 1 && direction == 1)) {
            nextIndex = shelfIndex + numShelves;
        }

        return nextIndex;
    }

    /**
     * Calculates the previous index for backtracking purposes.
     * @param shelfIndex The index of the current shelf.
     * @param numShelves The total number of shelves.
     * @param shelfHeight The height of the shelves.
     * @return The previous shelf index to access.
     */
    public static int calculatePreviousShelfIndex(int shelfIndex, int numShelves, int shelfHeight) {
        int height = shelfHeight - 1 - (shelfIndex / numShelves);
        int direction = (height % 2 == 0) ? -1 : 1;
        int previousIndex = shelfIndex + direction;

        if ((shelfIndex % numShelves == 0 && direction == -1) || (shelfIndex % numShelves == numShelves - 1 && direction == 1)) {
            previousIndex = shelfIndex - numShelves;
        }

        return previousIndex;
    }

    /**
     * Prints the distribution of products in the supermarket.
     * @param distribution The list of shelving units to be printed.
     */
    public static void printDistribution(ArrayList<ShelvingUnit> distribution) {
        int shelfHeight = distribution.getFirst().getHeight();
        for (ShelvingUnit shelf : distribution) {
            for (int i = shelfHeight - 1; i >= 0; i--) {
                Product product = shelf.getProduct(i);
                if (product != null) {
                    System.out.println("Shelf " + shelf.getUid() + ", Height " + i + ": " + product.getName());
                }
                else {
                    System.out.println("Shelf " + shelf.getUid() + ", Height " + i + ": Empty");
                }
            }
        }
    }

    /**
     * Says if is in the last position to sort.
     * @param shelfIndex is the position of the {@link ShelvingUnit} at the {@link Supermarket}.
     * @param numShelves is the number of {@link ShelvingUnit} at the {@link Supermarket}.
     * @param shelfHeight is the height of the {@link ShelvingUnit}.
     * @return {@code true} if it is the last position or {@code false} otherwise.
     */
    public static boolean isLastPosition(int shelfIndex, int numShelves, int shelfHeight)
    {
        return (shelfIndex == numShelves * shelfHeight - 1 && shelfHeight % 2 == 1) || (shelfIndex == numShelves * shelfHeight - numShelves && shelfHeight % 2 == 0);
    }
}
