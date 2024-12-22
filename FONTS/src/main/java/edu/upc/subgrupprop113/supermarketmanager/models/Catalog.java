package edu.upc.subgrupprop113.supermarketmanager.models;

import java.util.*;

/**
 * Represents a catalog containing a collection of products.
 * This class implements the Singleton pattern.
 */
public class Catalog {
    /**
     * The single instance of the catalog (Singleton).
     */
    private static Catalog catalog;

    /**
     * A list of products available in the catalog.
     */
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
        this.products.clear();
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
     * Returns an immutable list of all products.
     * Any attempt to modify the returned list will result in
     * an UnsupportedOperationException.
     *
     * @return An immutable list of products.
     */
    public List<Product> getAllProducts() {
        return Collections.unmodifiableList(products);
    }

    /**
     * Checks if the catalog contains a product with the specified name.
     * Prints the name being searched and the number of products in the catalog.
     *
     * @param name the name of the product to search for.
     * @return {@code true} if a product with the specified name exists in the catalog, {@code false} otherwise.
     */
    public Boolean contains(String name) {
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
     * Sets the list of products for this instance.
     *
     * This method validates that all relationships between the provided products
     * are correctly defined by calling the {@link #checkRTsRelations(List)} method.
     * It ensures that:
     *      - The products list is not null.
     *      - There are no duplicate products in the list.
     *      - All related products have a value val: between 0.0 and 1.0
     *      - All related products are included in the provided list.
     *      - The total number of unique relationships matches the expected count.
     *
     * @param products the list of products to verify; must contain at least two products
     * @throws IllegalArgumentException if the provided list is null,if any relationship includes a product
     *          not in the given list, or if there are relations with values off bounds.
     * @throws IllegalStateException if the number of unique relationships does not match the expected count
     *          for a fully connected set of products
     */
    public void setAllProducts(List<Product> products) {
        checkRTsRelations(products);
        this.products = new ArrayList<>(products);
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
     * Searches for products in the catalog based on the given search input.
     *
     * This method filters the list of products in the catalog by evaluating whether
     * each product matches the provided search input. The filtering logic considers
     * both the product's name and its associated keywords:
     *
     * <ul>
     *   <li>The product name is checked for case-insensitive containment of the search input.</li>
     *   <li>The product's keywords are also checked, and a product is included in the results
     *       if any of its keywords (case-insensitively) contain the search input.</li>
     * </ul>
     *
     * The filtering process is case-insensitive to ensure user-friendly search behavior.
     * For example, a search input of "apple" will match products with the name "Apple"
     * or keywords such as "applesauce" or "red apple."
     *
     * @param searchInput the input string to search for (case-insensitive).
     * @return a list of products that match the search input, either by name or by keywords.
     */
    public List<Product> searchProduct(String searchInput) {

        // Filter products based on query
        List<Product> filteredProducts = this.products.stream()
                .filter(product ->
                        product.getName().toLowerCase().contains(searchInput.toLowerCase()) ||
                                product.getKeyWords().stream().anyMatch(keyword -> keyword.toLowerCase().contains(searchInput.toLowerCase()))
                )
                .toList();

        return filteredProducts;

        // Previous approach with Jaccard similarity
//        final int MAX_SEARCH_RETURNED_ENTRIES = 10;
//        Map<Product, Float> productSimilarityMap = new HashMap<>();
//
//        for (Product product : products) {
//            float nameSimilarity = getJaccardSimilarity(product.getName(), searchInput);
//
//            float keyWordSimilarity = 0.0f;
//            List<String> keyWords = product.getKeyWords();
//            for (String keyWord : keyWords) {
//                float actP = getJaccardSimilarity(keyWord, searchInput) * 0.5f;
//                keyWordSimilarity = Math.max(keyWordSimilarity, actP);
//            }
//            float maxSimilarity = Math.max(nameSimilarity, keyWordSimilarity);
//            productSimilarityMap.put(product, maxSimilarity);
//        }
//
//        // Sort products by similarity score from highest to lowest
//        return productSimilarityMap.entrySet()
//                .stream()
//                .sorted(Map.Entry.<Product, Float>comparingByValue().reversed()) // Descending order
//                .limit(MAX_SEARCH_RETURNED_ENTRIES) // Take the top 10 most similar products
//                .map(Map.Entry::getKey) // Return only the list of products
//                .collect(Collectors.toList());
    }

    /**
     * Returns a string with the information of the catalog. Including all products and their details.
     *
     * @return a string with the information of the catalog.
     */
    public String getInfo() {
        String res = "";
        res += "----- Catalog Information -----\n";
        res += "Catalog size: " + products.size() + "\n";
        res += "-------------------------------\n";
        for (Product product : products) {
            res += product.getInfo();
            res += "-------------------------------\n";
        }
        return res;
    }

//    This function is useful if it is decided to do the old approach of searchProduct.
//    private float getJaccardSimilarity(String s1, String s2) {
//        Set<Character> set1 = new HashSet<>();
//        Set<Character> set2 = new HashSet<>();
//
//        for (char c : s1.toLowerCase().toCharArray()) {
//            set1.add(c);
//        }
//        for (char c : s2.toLowerCase().toCharArray()) {
//            set2.add(c);
//        }
//
//        Set<Character> intersection = new HashSet<>(set1);
//        intersection.retainAll(set2);
//
//        Set<Character> union = new HashSet<>(set1);
//        union.addAll(set2);
//
//        return (float) intersection.size() / union.size();
//    }

    /**
     * Verifies that all products in the given list are related to each other through unique relationships.
     * Each product in the list must have a relationship with every other product, and the total number of
     * unique relationships should match the required number for a fully connected network.
     *
     * @param products the list of products to verify; must contain at least two products
     * @throws IllegalArgumentException if the provided list is null,if any relationship includes a product
     *          not in the given list, if there are relations with values off bounds, or if the number of
     *          unique relationships does not match the expected count for a fully connected set of products.
     */
    private void checkRTsRelations(List<Product> products) {
        if (products == null) {
            throw new IllegalArgumentException("Products must not be null.");
        }

        Set<Product> productSet = new HashSet<>(products);
        if (productSet.size() != products.size()) {
            throw new IllegalArgumentException("The products list contains duplicate entries.");
        }

        int n = products.size();
        int requiredRelations = (n * (n - 1)) / 2; // Total unique relations for n products
        Set<Set<Product>> uniqueRelations = new HashSet<>();

        for (Product product : products) {
            List<RelatedProduct> relatedProducts = product.getRelatedProducts();
            for (RelatedProduct rel : relatedProducts) {
                List<Product> relatedPair = rel.getProducts();

                if (rel.value < 0 || rel.value > 1.0) {
                    throw new IllegalArgumentException("There are relations with value off bounds.");
                }

                // Check that both products in the relation are in the input list
                for (Product relatedProduct : relatedPair) {
                    if (!productSet.contains(relatedProduct)) {
                        throw new IllegalArgumentException("Product " + relatedProduct
                                + " in relation with " + product
                                + " is not part of the provided products list.");
                    }
                }

                // Add the relation as a unique, unordered pair
                uniqueRelations.add(new HashSet<>(relatedPair));
            }
        }

        // Check if the total unique relations match the required relations
        if (uniqueRelations.size() != requiredRelations) {
            throw new IllegalArgumentException("The number of unique relations (" + uniqueRelations.size()
                    + ") does not match the required relations (" + requiredRelations + ").");
        }
    }
}
