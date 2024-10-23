package edu.upc.subgrupprop113.supermarketmanager;

import edu.upc.subgrupprop113.supermarketmanager.OrderingStrategy;
import edu.upc.subgrupprop113.supermarketmanager.Product;
import edu.upc.subgrupprop113.supermarketmanager.ShelvingUnit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class BruteForce implements OrderingStrategy {

    @Override
    public ArrayList<ShelvingUnit> orderSupermarket(ArrayList<ShelvingUnit> distribution, Set<Product> products) {
        List<Product> productList = new ArrayList<>(products);
        ArrayList<ShelvingUnit> bestDistribution = new ArrayList<>(distribution);
        double maxScore = -1;

        // Generate all permutations of the product list
        List<List<Product>> permutations = generatePermutations(productList);

        // Evaluate each permutation
        for (List<Product> perm : permutations) {
            double currentScore = calculateSimilarityScore(perm);
            if (currentScore > maxScore) {
                maxScore = currentScore;
                // Update the bestDistribution based on the current permutation
                bestDistribution = arrangeProductsOnShelves(perm, distribution);
            }
        }

        return bestDistribution;
    }

    // Generate all permutations of the list of products
    private List<List<Product>> generatePermutations(List<Product> products) {
        List<List<Product>> permutations = new ArrayList<>();
        permute(products, 0, permutations);
        return permutations;
    }

    private void permute(List<Product> products, int l, List<List<Product>> permutations) {
        if (l == products.size()) {
            permutations.add(new ArrayList<>(products));
        } else {
            for (int i = l; i < products.size(); i++) {
                Collections.swap(products, l, i);
                permute(products, l + 1, permutations);
                Collections.swap(products, l, i);  // Backtrack
            }
        }
    }

    // Calculate the similarity score for a given permutation of products
    private double calculateSimilarityScore(List<Product> perm) {
        double score = 0;
        for (int i = 0; i < perm.size() - 1; i++) {
            score += perm.get(i).getSimililarity(perm.get(i + 1));
        }
        return score;
    }

    // Arrange the products on shelves based on the current permutation
    private ArrayList<ShelvingUnit> arrangeProductsOnShelves(List<Product> products, ArrayList<ShelvingUnit> distribution) {
        ArrayList<ShelvingUnit> newDistribution = new ArrayList<>(distribution);
        int productIndex = 0;

        for (ShelvingUnit shelf : newDistribution) {
            for (int i = 0; i < shelf.getAlcada(); i++) {
                if (productIndex < products.size()) {
                    shelf.addProduct(i, products.get(productIndex));
                    productIndex++;
                }
            }
        }

        return newDistribution;
    }
}
