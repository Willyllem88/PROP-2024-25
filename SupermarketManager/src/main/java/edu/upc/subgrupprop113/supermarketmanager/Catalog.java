package edu.upc.subgrupprop113.supermarketmanager;

import java.util.*;
import java.util.stream.Collectors;

public class Catalog {

    private static Catalog catalog;
    private List<Product> products;

    public static Catalog getCatalog() {
        if (catalog == null) {
            catalog = new Catalog();
            catalog.products = new ArrayList<>();
        }
        return catalog;
    }

    public Boolean contains(String productName) {
        System.out.println("Name: " + productName);
        System.out.println("Products length: " + products.size());
        for (Product product : products) {
            if (product.getName().equals(productName)) return true;
        }
        return false;
    }

    public Boolean contains(Product product) {
        return products.contains(product);
    }

    /**
     * Creates a new product and adds it to the system, if a product with the same name doesn't already exist.
     *
     * @param name             The name of the product to be created.
     * @param price            The price of the product.
     * @param temperature      The temperature category for the product (FROZEN, REFRIGERATED, AMBIENT).
     * @param imgPath          The file path to the image representing the product.
     * @param keyWords         A list of keywords associated with the product for easier search.
     * @param relatedProducts  A list of related products that have a connection with this product.
     *
     * @throws IllegalArgumentException If a product with the given name already exists in the system.
     */
    public void createNewProduct(String name, float price, ProductTemperature temperature, String imgPath, List<String> keyWords, List<RelatedProduct> relatedProducts)  {
        if (this.contains(name)) {
            throw new IllegalArgumentException("Product with name '" + name + "' already exists.");
        }

        //TODO: ensure that the length of relatedProducts is the right one

        Product newProduct = new Product(name, price, temperature, imgPath);

        for (String keyWord : keyWords) {
            newProduct.addKeyWord(keyWord);
        }

        for (RelatedProduct relatedProduct : relatedProducts) {
            newProduct.addRelatedProduct(relatedProduct);
        }

        products.add(newProduct);
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

        if (Supermarket.getInstance().containsProduct(productToRemove)) {
            //TODO: ask if user wants to erase that product from the shelves
            //if true
                Supermarket.getInstance().eraseProduct(productToRemove);
            //else return
        }

        if (productToRemove == null) {
            throw new IllegalArgumentException("Product with name '" + productName + "' does not exist.");
        }

        productToRemove.eliminateAllRelations();
    }

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

    //FIX: just for testing
    static public void main(String[] args) {
        // Create a catalog instance
        Catalog catalog = Catalog.getCatalog();

        // Create some products with names, prices, temperature, image path, keywords, and related products
        List<String> keywords1 = Arrays.asList("fruit", "apple", "sweet");
        List<String> keywords2 = Arrays.asList("snack", "chocolate", "sweet");
        List<String> keywords3 = Arrays.asList("drink", "water", "refreshing");

        List<RelatedProduct> emptyRelatedProducts = new ArrayList<>(); // No related products in this test

        // Create products and add them to the catalog
        catalog.createNewProduct("Apple", 1.99f, ProductTemperature.AMBIENT, "/images/apple.jpg", keywords1, emptyRelatedProducts);
        catalog.createNewProduct("Chocolate Bar", 2.50f, ProductTemperature.AMBIENT, "/images/chocolate.jpg", keywords2, emptyRelatedProducts);
        catalog.createNewProduct("Water Bottle", 1.00f, ProductTemperature.AMBIENT, "/images/water.jpg", keywords3, emptyRelatedProducts);

        // Test searching for a product with similar keywords or names
        System.out.println("Search results for 'choco':");
        List<Product> results = catalog.searchProduct("choco");
        for (Product result : results) {
            System.out.println(result.getName());
        }

        // Test the contains method
        System.out.println("Catalog contains 'Apple': " + catalog.contains("Apple"));  // Should return true
        System.out.println("Catalog contains 'Banana': " + catalog.contains("Banana"));  // Should return false

        // Test product deletion
        catalog.eraseProduct("Apple");
        System.out.println("Catalog contains 'Apple' after deletion: " + catalog.contains("Apple"));  // Should return false

        String searchInp = "Chocolate";
        List<Product> similarProducts = catalog.searchProduct(searchInp);
        for (Product p : similarProducts) {
            System.out.println(p.getName());
        }
    }
}
