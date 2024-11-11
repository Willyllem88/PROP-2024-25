package edu.upc.subgrupprop113.supermarketmanager;

import javafx.util.Pair;
import java.util.*;

public class Approximation implements OrderingStrategy {

    private int shelfHeight;
    private double highestScore;
    private List<ShelvingUnit> optimalDistribution;

    @Override
    public ArrayList<ShelvingUnit> orderSupermarket(List<ShelvingUnit> initialShelves, List<Product> products) {
        if (initialShelves == null || initialShelves.isEmpty()) {
            throw new IllegalArgumentException("Initial shelves cannot be null or empty.");
        }
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Products cannot be null or empty.");
        }

        this.shelfHeight = initialShelves.getFirst().getHeight();
        this.highestScore = -1;
        this.optimalDistribution = deepCopyShelves((ArrayList<ShelvingUnit>) initialShelves, true);

        // Simulated annealing algorithm
        ArrayList<ShelvingUnit> currentShelves = deepCopyShelves((ArrayList<ShelvingUnit>) initialShelves, true);
        int steps = 1000; // Number of steps for the annealing process
        double k = 1.0; // Boltzmann constant
        double lambda = 0.99; // Cooling rate

        // Generate random initial state
        ArrayList<ShelvingUnit> initialState = generateInitialSolution(currentShelves, products);

        return new ArrayList<>();
    }

    /**
     * Generates an initial solution for the simulated annealing algorithm.
     * @param shelves The list of shelves to be filled.
     * @param products The list of products to be placed.
     * @return An initial solution for the simulated annealing algorithm.
     */
    public ArrayList<ShelvingUnit> generateInitialSolution(ArrayList<ShelvingUnit> shelves, List<Product> products) {
        ArrayList<ShelvingUnit> state = deepCopyShelves(shelves, true);
        List<Product> unplacedProducts = new ArrayList<>(products);

        // Shuffle products to randomize initial placement
        Collections.shuffle(unplacedProducts, new Random());

        int totalPositions = shelves.size() * shelfHeight;
        int currentIndex = 0;

        // Iterate through positions until we run out of products or positions
        while (!unplacedProducts.isEmpty() && currentIndex < totalPositions) {
            // Calculate next shelf index
            int shelfIndex = currentIndex % shelves.size();
            int heightIndex = this.shelfHeight - 1 - (currentIndex / shelves.size());

            ShelvingUnit shelf = state.get(shelfIndex);

            // Find a compatible product
            Product productToPlace = null;
            for (Product product : unplacedProducts) {
                if (isShelfCompatible(shelf, product)) {
                    productToPlace = product;
                    break;
                }
            }

            if (productToPlace != null) {
                // Place the product on the shelf
                shelf.addProduct(productToPlace, heightIndex);
                unplacedProducts.remove(productToPlace);
            }

           currentIndex = calculateNextShelfIndex(currentIndex, heightIndex, shelves.size());
        }

        return state;
    }


    /**
     * Calculates the next index to place a product, moving across shelves and heights.
     * @param currentShelfIndex The current index being filled.
     * @param height The current height on the shelf.
     * @param numShelves The total number of shelves.
     * @return The next shelf index to access.
     */
    private int calculateNextShelfIndex(int currentShelfIndex, int height, int numShelves) {
        int direction = (height % 2 == 0) ? 1 : -1;
        int nextIndex = currentShelfIndex + direction;

        if ((currentShelfIndex % numShelves == 0 && direction == -1) || (currentShelfIndex % numShelves == numShelves - 1 && direction == 1)) {
            nextIndex = currentShelfIndex + numShelves;
        }

        if (nextIndex < 0 || nextIndex >= numShelves * shelfHeight) {
            throw new IndexOutOfBoundsException("Next index out of bounds: " + nextIndex);
        }

        return nextIndex;
    }

    /**
     * Calculates the previous index for backtracking purposes.
     * @param currentShelfIndex The current index being backtracked from.
     * @param height The current height on the shelf.
     * @param numShelves The total number of shelves.
     * @return The previous shelf index to access.
     */
    private int calculatePreviousShelfIndex(int currentShelfIndex, int height, int numShelves) {
        int direction = (height % 2 == 0) ? -1 : 1;
        int previousIndex = currentShelfIndex + direction;

        if ((currentShelfIndex % numShelves == 0 && direction == -1) || (currentShelfIndex % numShelves == numShelves - 1 && direction == 1)) {
            previousIndex = currentShelfIndex - numShelves;
        }

        if (previousIndex < 0 || previousIndex >= numShelves * shelfHeight) {
            throw new IndexOutOfBoundsException("Previous index out of bounds: " + previousIndex);
        }

        return previousIndex;
    }

    /**
     * Checks if a product can be placed on a given shelf based on temperature compatibility.
     * @param shelf The shelf to be checked.
     * @param product The product to be placed.
     * @return True if the product is compatible, false otherwise.
     */
    private Boolean isShelfCompatible(ShelvingUnit shelf, Product product) {
        return shelf.getTemperature() == product.getTemperature();
    }

    /**
     * Calculates the similarity between two products.
     * @param productA The first product.
     * @param productB The second product.
     * @return The similarity score between the two products.
     */
    private double calculateSimilarity(Product productA, Product productB) {
        return productA.getRelatedValue(productB);
    }

    /**
     * Creates a deep copy of the list of shelving units.
     * @param originalShelves The original list of shelves.
     * @param empty Whether the shelves should be emptied during the copying process.
     * @return A deep copy of the shelving units.
     */
    private ArrayList<ShelvingUnit> deepCopyShelves(ArrayList<ShelvingUnit> originalShelves, boolean empty) {
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
}
