package edu.upc.subgrupprop113.supermarketmanager.services;

import edu.upc.subgrupprop113.supermarketmanager.models.Product;
import edu.upc.subgrupprop113.supermarketmanager.models.ShelvingUnit;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static edu.upc.subgrupprop113.supermarketmanager.utils.HelperFunctions.*;

/**
 * Class that implements the sorting algorithm by using approximation.
 * */
public class Approximation implements OrderingStrategy {

    private int shelfHeight;

    /**
     * Orders the supermarket shelves using a simulated annealing approach.
     * @param initialShelves The initial state of the supermarket shelves.
     * @param products The list of products to be placed on the shelves.
     * @return The ordered list of supermarket shelves.
     */
    @Override
    public ArrayList<ShelvingUnit> orderSupermarket(List<ShelvingUnit> initialShelves, List<Product> products) {
        this.shelfHeight = initialShelves.getFirst().getHeight();

        // Simulated annealing parameters
        int steps = 100000; // Number of steps for the annealing process
        double k = 5.0; // Boltzmann constant
        double lambda = 0.99; // Cooling rate
        double temperature = 1000.0; // Initial temperature

        // Start with a greedy solution
        ArrayList<ShelvingUnit> greedyShelves = new GreedyBacktracking().orderSupermarket(initialShelves, products);
        List<Product> unplacedProducts = new ArrayList<>(products);

        // Remove placed products from the list of unplaced products
        removePlacedProductsFromList(greedyShelves, unplacedProducts);

        Pair<ArrayList<ShelvingUnit>, Double> resultWithGreedyInitial = simulatedAnnealing(greedyShelves, unplacedProducts, steps, k, lambda, temperature);

        ArrayList<ShelvingUnit> bestShelves = resultWithGreedyInitial.getKey();
        double bestScore = resultWithGreedyInitial.getValue();

        // Repeat the process multiple times with random initial states and keep the best solution
        for (int i = 0; i < 4; ++i) {
            // Initialize unplaced products
            unplacedProducts = new ArrayList<>(products);

            // Generate random initial state
            ArrayList<ShelvingUnit> shelves = generateInitialSolution((ArrayList<ShelvingUnit>) initialShelves, unplacedProducts);

            // Remove placed products from the list of unplaced products
            removePlacedProductsFromList(shelves, unplacedProducts);

            Pair<ArrayList<ShelvingUnit>, Double> result = simulatedAnnealing(shelves, unplacedProducts, steps, k, lambda, temperature);

            double partialScore = result.getValue();
            if (partialScore > bestScore) {
                bestScore = partialScore;
                bestShelves = result.getKey();
            }
        }

        return bestShelves;
    }

    /**
     * Simulated annealing algorithm to find the optimal distribution of products on the shelves.
     * @param shelves The initial state of the supermarket shelves.
     * @param products The list of products to be placed on the shelves.
     * @param steps The number of steps for the annealing process.
     * @param k The Boltzmann constant.
     * @param lambda The cooling rate.
     * @param temperature The initial temperature.
     * @return A pair containing the optimal distribution of shelves and the highest similarity score.
     */
    private Pair<ArrayList<ShelvingUnit>, Double> simulatedAnnealing(ArrayList<ShelvingUnit> shelves, List<Product> products, int steps, double k, double lambda, double temperature) {

        List<Product> unplacedProducts = new ArrayList<>(products);

        double currentScore = calculateTotalSimilarity(shelves);
        ArrayList<ShelvingUnit> optimalDistribution = deepCopyShelves(shelves, false);
        double highestScore = currentScore;

        Random rand = new Random();

        for (int step = 0; step < steps; step++) {
            // Decide which operator to use
            int operatorChoice = rand.nextInt(3); // 0, 1, or 2
            ArrayList<ShelvingUnit> neighborShelves;
            List<Product> neighborUnplacedProducts = new ArrayList<>(unplacedProducts);

            if (operatorChoice == 0) {
                neighborShelves = swapTwoProducts(shelves);
            } else if (operatorChoice == 1) {
                neighborShelves = moveProductToEmptyPosition(shelves);
            } else {
                neighborShelves = swapWithUnplacedProduct(shelves, neighborUnplacedProducts);
            }

            // Calculate the score of the neighbor
            double neighborScore = calculateTotalSimilarity(neighborShelves);

            // Calculate delta (difference in scores)
            double delta = neighborScore - currentScore;

            // Decide whether to accept the neighbor
            if (delta > 0) {
                // Neighbor is better, accept it
                shelves = neighborShelves;
                currentScore = neighborScore;
                if (operatorChoice == 2) {
                    unplacedProducts = neighborUnplacedProducts;
                }
            } else {
                // Neighbor is worse, accept it with probability e^(delta / (fTemperature))
                double fTemperature = k * Math.exp(-lambda * temperature);
                double probability = Math.exp(delta / fTemperature);
                if (rand.nextDouble() < probability) {
                    shelves = neighborShelves;
                    currentScore = neighborScore;
                    if (operatorChoice == 2) {
                        unplacedProducts = neighborUnplacedProducts;
                    }
                }
            }

            // Update the best solution found
            if (currentScore > highestScore) {
                optimalDistribution = deepCopyShelves(shelves, false);
                highestScore = currentScore;
            }

            // Cool down the temperature
            temperature *= lambda;

            // Optional: Break if temperature is too low
            if (temperature < 1e-5) {
                break;
            }
        }

        return new Pair<>(optimalDistribution, highestScore);
    }


    /**
     * Generates an initial solution for the simulated annealing algorithm.
     *
     * @param shelves  The list of shelves to be filled.
     * @param unplacedProducts The list of unplaced products to be placed.
     * @return An initial solution for the simulated annealing algorithm.
     */
    private ArrayList<ShelvingUnit> generateInitialSolution(ArrayList<ShelvingUnit> shelves, List<Product> unplacedProducts) {
        ArrayList<ShelvingUnit> state = deepCopyShelves(shelves, true);

        // Shuffle products to randomize initial placement
        Collections.shuffle(unplacedProducts, new Random());

        int totalPositions = shelves.size() * this.shelfHeight;
        int currentIndex = 0;

        // Iterate through positions until we run out of products or positions
        while (!unplacedProducts.isEmpty() && currentIndex < totalPositions) {
            // Calculate next shelf index

            ShelvingUnit shelf = state.get(currentIndex % shelves.size());
            int heightIndex = getShelfHeight(currentIndex, shelves.size(), this.shelfHeight);

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

            currentIndex = calculateNextShelfIndex(currentIndex, shelves.size(), this.shelfHeight);
        }

        return state;
    }

    /**
     * Swaps two products in the current state to generate a neighbor solution.
     * Ensures that the products are compatible with the new shelves after swap.
     *
     * @param currentState The current state of the shelves.
     * @return A new state with two products swapped.
     */
    private ArrayList<ShelvingUnit> swapTwoProducts(ArrayList<ShelvingUnit> currentState) {
        // Deep copy the current state to avoid modifying the original
        ArrayList<ShelvingUnit> neighborState = deepCopyShelves(currentState, false);

        int[] productPositions = getProductPositions(neighborState);

        if (productPositions.length < 2) {
            // Not enough products to swap
            return neighborState;
        }

        // Randomly select two positions
        Random rand = new Random();
        int index1 = rand.nextInt(productPositions.length);
        int index2 = rand.nextInt(productPositions.length);

        while (index2 == index1) {
            index2 = rand.nextInt(productPositions.length);
        }

        int pos1 = productPositions[index1];
        int pos2 = productPositions[index2];

        ShelvingUnit shelf1 = getCurrentShelf(neighborState, pos1);
        ShelvingUnit shelf2 = getCurrentShelf(neighborState, pos2);
        int heightIndex1 = getShelfHeight(pos1, neighborState.size(), this.shelfHeight);
        int heightIndex2 = getShelfHeight(pos2, neighborState.size(), this.shelfHeight);

        Product product1 = shelf1.getProduct(heightIndex1);
        Product product2 = shelf2.getProduct(heightIndex2);

        // Check temperature compatibility after swap
        if (isShelfCompatible(shelf1, product2) && isShelfCompatible(shelf2, product1)) {
            // Perform swap
            shelf1.removeProduct(heightIndex1);
            shelf2.removeProduct(heightIndex2);
            shelf1.addProduct(product2, heightIndex1);
            shelf2.addProduct(product1, heightIndex2);
        }

        return neighborState;
    }

    /**
     * Swaps a placed product with an unplaced product.
     * Ensures that the unplaced product is compatible with the shelf.
     * @param currentState The current state of the shelves.
     * @return A new state with a placed product swapped with an unplaced product.
     */
    private ArrayList<ShelvingUnit> swapWithUnplacedProduct(ArrayList<ShelvingUnit> currentState, List<Product> neighborUnplacedProducts) {
        // Deep copy the current state to avoid modifying the original
        ArrayList<ShelvingUnit> neighborState = deepCopyShelves(currentState, false);

        int[] productPositions = getProductPositions(neighborState);

        if (productPositions.length == 0 || neighborUnplacedProducts.isEmpty()) {
            // Cannot swap if there are no placed products or no unplaced products
            return neighborState;
        }

        // Randomly select a placed product position
        Random rand = new Random();
        int productPosIndex = rand.nextInt(productPositions.length);
        int pos = productPositions[productPosIndex];

        ShelvingUnit shelf = getCurrentShelf(neighborState, pos);
        int heightIndex = getShelfHeight(pos, neighborState.size(), this.shelfHeight);

        Product placedProduct = shelf.getProduct(heightIndex);

        // Find compatible unplaced products
        List<Product> compatibleUnplacedProducts = new ArrayList<>();
        for (Product product : neighborUnplacedProducts) {
            if (isShelfCompatible(shelf, product)) {
                compatibleUnplacedProducts.add(product);
            }
        }

        if (compatibleUnplacedProducts.isEmpty()) {
            // No compatible unplaced products to swap with
            return neighborState;
        }

        // Randomly select a compatible unplaced product
        Product unplacedProduct = compatibleUnplacedProducts.get(rand.nextInt(compatibleUnplacedProducts.size()));

        // Swap the products
        shelf.removeProduct(heightIndex);
        shelf.addProduct(unplacedProduct, heightIndex);

        // Update the neighborUnplacedProducts list
        neighborUnplacedProducts.remove(unplacedProduct);
        neighborUnplacedProducts.add(placedProduct);

        return neighborState;
    }


    /**
     * Moves a product to an empty position on a compatible shelf.
     * @param currentState The current state of the shelves.
     * @return A new state with a product moved.
     */
    private ArrayList<ShelvingUnit> moveProductToEmptyPosition(ArrayList<ShelvingUnit> currentState) {
        // Deep copy the current state to avoid modifying the original
        ArrayList<ShelvingUnit> neighborState = deepCopyShelves(currentState, false);

        int[] productPositions = getProductPositions(neighborState);
        int[] emptyPositions = getEmptyPositions(neighborState);

        if (productPositions.length == 0 || emptyPositions.length == 0) {
            // Cannot move a product if there are no products or no empty positions
            return neighborState;
        }

        // Randomly select a product position and an empty position
        Random rand = new Random();
        int productPosIndex = rand.nextInt(productPositions.length);
        int emptyPosIndex = rand.nextInt(emptyPositions.length);

        int fromPos = productPositions[productPosIndex];
        int toPos = emptyPositions[emptyPosIndex];

        // Get shelf and height indices for from and to positions
        ShelvingUnit fromShelf = getCurrentShelf(neighborState, fromPos);
        ShelvingUnit toShelf = getCurrentShelf(neighborState, toPos);
        int fromHeightIndex = getShelfHeight(fromPos, neighborState.size(), this.shelfHeight);
        int toHeightIndex = getShelfHeight(toPos, neighborState.size(), this.shelfHeight);

        Product product = fromShelf.getProduct(fromHeightIndex);

        // Check temperature compatibility at the new position
        if (isShelfCompatible(toShelf, product)) {
            // Move product
            fromShelf.removeProduct(fromHeightIndex);
            toShelf.addProduct(product, toHeightIndex);
        }

        return neighborState;
    }

    /**
     * Gets a list of positions where products are placed.
     * Each position is represented as an int array: [shelfIndex, heightIndex]
     *
     * @param state The current state of the shelves.
     * @return A list of positions with products.
     */
    private int[] getProductPositions(ArrayList<ShelvingUnit> state) {
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < state.size() * this.shelfHeight; ++i) {
            ShelvingUnit shelf = getCurrentShelf(state, i);
            int heightIndex = getShelfHeight(i, state.size(), this.shelfHeight);
            Product p = shelf.getProduct(heightIndex);
            if (p != null) {
                positions.add(i); // Add the position of the product to the list
            }
        }
        return positions.stream().mapToInt(i -> i).toArray();
    }

    /**
     * Gets a list of positions that are empty.
     * @param state The current state of the shelves.
     * @return An array of empty positions.
     */
    private int[] getEmptyPositions(ArrayList<ShelvingUnit> state) {
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < state.size() * this.shelfHeight; ++i) {
            ShelvingUnit shelf = getCurrentShelf(state, i);
            int heightIndex = getShelfHeight(i, state.size(), this.shelfHeight);
            Product p = shelf.getProduct(heightIndex);
            if (p == null) {
                positions.add(i); // Add the position of the empty slot to the list
            }
        }
        return positions.stream().mapToInt(i -> i).toArray();
    }

    private void removePlacedProductsFromList(ArrayList<ShelvingUnit> shelves, List<Product> unplacedProducts) {
        for (ShelvingUnit su : shelves) {
            for (int pos = 0; pos < shelfHeight; pos++) {
                Product p = su.getProduct(pos);
                if (p != null) {
                    unplacedProducts.remove(p);
                }
            }
        }
    }
}
