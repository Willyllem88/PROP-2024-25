package edu.upc.subgrupprop113.supermarketmanager;

import java.util.ArrayList;

public class HelperFunctions {

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
        if (productA == null || productB == null) {
            return 0;
        }
        return productA.getRelatedValue(productB);
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

        if (nextIndex < 0 || nextIndex >= numShelves * shelfHeight) {
            throw new IndexOutOfBoundsException("Next index out of bounds: " + nextIndex);
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

        if (previousIndex < 0 || previousIndex >= numShelves * shelfHeight) {
            throw new IndexOutOfBoundsException("Previous index out of bounds: " + previousIndex);
        }

        return previousIndex;
    }
}
