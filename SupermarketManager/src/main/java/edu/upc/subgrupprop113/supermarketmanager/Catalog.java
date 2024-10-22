package edu.upc.subgrupprop113.supermarketmanager;

import java.util.*;
import java.util.stream.Collectors;

public class Catalog {

    private static Catalog catalog;
    private List<Product> products;

    /**
     * Returns the singleton instance of the Catalog.
     * If the instance doesn't exist, it creates a new Catalog and initializes the products list.
     *
     * @return the singleton instance of the Catalog.
     */
    public static Catalog getInstance() {
        if (catalog == null) {
            catalog = new Catalog();
            catalog.products = new ArrayList<>();
        }
        return catalog;
    }

    /**
     * Clears all products from the catalog
     */
    public void clear() {
        products.clear();
    }

    /**
     * Retrieves a product from the catalog by its name.
     *
     * @param name The name of the product to be retrieved.
     * @return the product identified by name.
     * @throws IllegalArgumentException if the product is not contained in the catalog.
     */
    public Product getProduct(String name) {
        for (Product product : products) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        throw new IllegalArgumentException("Product with name '" + name + "' is not contained in the catalog.");
    }

    /**
     * Checks if the catalog contains a product with the specified name.
     * Prints the name being searched and the number of products in the catalog.
     *
     * @param name the name of the product to search for.
     * @return {@code true} if a product with the specified name exists in the catalog, {@code false} otherwise.
     */
    public Boolean contains(String name) {
        System.out.println("Name: " + name);
        System.out.println("Products length: " + products.size());
        for (Product product : products) {
            if (product.getName().equals(name)) return true;
        }
        return false;
    }

    /**
     * Checks if the catalog contains the specified product.
     *
     * @param product the product to search for in the catalog.
     * @return {@code true} if the product exists in the catalog, {@code false} otherwise.
     */
    public Boolean contains(Product product) {
        return products.contains(product);
    }


    /**
     * Creates a new product and adds it to the system if a product with the same name doesn't already exist.
     *
     * @param name             The name of the product to be created.
     * @param price            The price of the product.
     * @param temperature      The temperature category for the product (FROZEN, REFRIGERATED, AMBIENT).
     * @param imgPath          The file path to the image representing the product.
     * @param keyWords         A list of keywords associated with the product for easier search.
     * @param relatedProducts  A list of related products that have a connection with this product.
     * @param relatedValues    A list of float values representing the relationship strength with each related product.
     *
     * @throws IllegalArgumentException If a product with the given name already exists in the system.
     * @throws IllegalArgumentException If the size of relatedProducts or relatedValues does not match the number of products in the system.
     */
    public Product createNewProduct(String name, float price, ProductTemperature temperature, String imgPath, final List<String> keyWords, final List<Product> relatedProducts, final List<Float> relatedValues)  {
        if (this.contains(name)) {
            throw new IllegalArgumentException("Product with name '" + name + "' already exists.");
        }

        if (this.products.size() != relatedProducts.size()) {
            throw new IllegalArgumentException("The size of relatedProducts is not correct. Must have an entry for each product.");
        }
        if (this.products.size() != relatedValues.size()) {
            throw new IllegalArgumentException("The size of relatedValues is not correct. Must have an entry for each product.");
        }

        Product newProduct = new Product(name, price, temperature, imgPath);
        products.add(newProduct);

        for (int i = 0; i < relatedProducts.size(); i++) {
            Product relProd = relatedProducts.get(i);
            if (relProd.getName().equals(name)) {
                throw new IllegalArgumentException("Product with name '" + name + "' already exists.");
            }
            new RelatedProduct(relProd, newProduct, relatedValues.get(i));
        }

        for (String keyWord : keyWords) {
            newProduct.addKeyWord(keyWord);
        }

        return newProduct;
    }

    /**
     * Removes the product with the specified name from the catalog.
     *
     * @param productName The name of the product to be removed.
     * @throws IllegalArgumentException If a product with the given name does not exist in the catalog.
     */
    public void eraseProduct(String productName) {
        Product productToRemove = null;
        for (Product product : products) {
            if (product.getName().equals(productName)) {
                productToRemove = product;
            }
        }

        /*if (Supermarket.getInstance().containsProduct(productToRemove)) {
            //TODO: ask if user wants to erase that product from the shelves
            //if true
                Supermarket.getInstance().eraseProduct(productToRemove);
            //else return
        }*/

        if (productToRemove == null) {
            throw new IllegalArgumentException("Product with name '" + productName + "' does not exist.");
        }

        productToRemove.eliminateAllRelations();
        products.remove(productToRemove);
    }

    /**
     * Modifies the relationship value between two specified products.
     * If either product is null, an {@code IllegalArgumentException} is thrown.
     *
     * @param product1 the first product to modify the relation for.
     * @param product2 the second product to modify the relation with.
     * @param relationValue the new relationship value to set between the two products.
     * @throws IllegalArgumentException if either product is null.
     */
    public void modifyRelationProduct(Product product1, Product product2, float relationValue) {
        if (product1 == null || product2 == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        product1.setRelatedValue(product2, relationValue);
    }

    /**
     * Searches for products based on a given input string and returns a list of the most similar products.
     * The similarity is calculated using the Jaccard similarity between the search input and the product's name or keywords.
     * The similarity is calculated as follows:
     * - If comparing with the product name, it uses {@code getJaccardSimilarity(product.getName(), searchInput)}.
     * - If comparing with the product's keywords, it uses {@code 0.5 * max(getJaccardSimilarity(product.getKeyWord[i], searchInput))}.
     * - The final similarity score for each product is the maximum between the name similarity and the keyword similarity.
     * The returned list contains the top 10 most similar products, ordered from highest to lowest similarity.
     *
     * @param searchInput The input string to search for.
     * @return A list of the top 10 most similar products, ordered by similarity.
     */
    public List<Product> searchProduct(String searchInput) {
        final int MAX_SEARCH_RETURNED_ENTRIES = 10;
        Map<Product, Float> productSimilarityMap = new HashMap<>();

        for (Product product : products) {
            float nameSimilarity = getJaccardSimilarity(product.getName(), searchInput);

            float keyWordSimilarity = 0.0f;
            List<String> keyWords = product.getKeyWords();
            for (String keyWord : keyWords) {
                float actP = getJaccardSimilarity(keyWord, searchInput) * 0.5f;
                keyWordSimilarity = Math.max(keyWordSimilarity, actP);
            }
            float maxSimilarity = Math.max(nameSimilarity, keyWordSimilarity);
            productSimilarityMap.put(product, maxSimilarity);
        }

        //FIX: just for debugging
        System.out.println("SEARCH RESULTS: ");
        for (Map.Entry<Product, Float> entry : productSimilarityMap.entrySet()) {
            System.out.println(entry.getKey().getName() + ": " + entry.getValue());
        }

        // Sort products by similarity score from highest to lowest
        return productSimilarityMap.entrySet()
                .stream()
                .sorted(Map.Entry.<Product, Float>comparingByValue().reversed()) // Descending order
                .limit(MAX_SEARCH_RETURNED_ENTRIES) // Take the top 10 most similar products
                .map(Map.Entry::getKey) // Return only the list of products
                .collect(Collectors.toList());
    }

    private float getJaccardSimilarity(String s1, String s2) {
        Set<Character> set1 = new HashSet<>();
        Set<Character> set2 = new HashSet<>();

        for (char c : s1.toLowerCase().toCharArray()) {
            set1.add(c);
        }
        for (char c : s2.toLowerCase().toCharArray()) {
            set2.add(c);
        }

        Set<Character> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<Character> union = new HashSet<>(set1);
        union.addAll(set2);

        return (float) intersection.size() / union.size();
    }
}
