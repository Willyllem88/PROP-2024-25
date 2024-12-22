package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.dtos.ProductDto;
import edu.upc.subgrupprop113.supermarketmanager.dtos.RelatedProductDto;
import edu.upc.subgrupprop113.supermarketmanager.dtos.ShelvingUnitDto;
import edu.upc.subgrupprop113.supermarketmanager.models.ProductTemperature;

import java.util.List;

/**
 * Interface for the domain controller, which acts as the mediator between the application logic
 * and the user interface. It provides the core functionalities and operations required to manage
 * the application's domain.
 */
public interface IDomainController {
    /**
     * Logs a user into the system with the specified username and password.
     * Verifies that no other user is already logged in and checks the provided credentials.
     *
     * @param username the username of the user attempting to log in.
     * @param password the password of the user attempting to log in.
     * @throws IllegalStateException if a user is already logged in.
     * @throws IllegalArgumentException if the username does not exist or if the password is incorrect.
     */
    void logIn(String username, String password) ;

    void closeApp() ;

    /**
     * Logs out the currently logged-in user.
     *
     * @throws IllegalStateException if no user is currently logged in.
     */
    void logOut() ;

    /**
     * Imports a supermarket configuration from a specified file, updating the shelving units and catalog with new data.
     * @param filename the name of the file containing the supermarket data to import.
     * @throws IllegalStateException if there is an existing shelving unit distribution or if the product relations are incorrect or the logged user is not the admin.
     * @throws IllegalArgumentException if any imported shelving unit fails validation or if any imported product fails the restrictions of the catalog.
     */
    void importSupermarketConfiguration(String filename) ;

    /**
     * Exports the current supermarket configuration to a specified file, including the product catalog and shelving units.
     *
     * @param filename the name of the file to which the supermarket data will be exported.
     * @throws IllegalStateException if the current user is not an administrator.
     * @throws IllegalArgumentException if the export fails
     */
    void exportSupermarketConfiguration(String filename) ;

    /**
     * Creates the distribution of shelving units in the supermarket according to specified heights, temperature types, and quantities.
     * <p>This method takes in a shelving unit height, a list of temperature types, and a corresponding list of quantities
     * to define each shelving unit. Each temperature type in ;@code temperatures} must have a corresponding quantity in
     * ;@code quantities}, and both lists must be of the same length.</p>
     *
     * @param shelvingUnitsHeight the height of each shelving unit
     * @param temperatures        a list of temperature types for each shelving unit, as strings, which must match values in ;@link ProductTemperature}
     * @param quantities          a list of quantities representing the number of shelving units for each temperature type
     *
     * @throws IllegalStateException if the logged user is not the admin.
     * @throws IllegalArgumentException if the ;@code temperatures} or ;@code quantities} lists are null, have different sizes,
     *                                  or contain invalid temperature values. Also, if the supermarket is not empty or if the logged-in user is not the admin.
     */
    void createSupermarketDistribution(int shelvingUnitsHeight, List<String> temperatures, List<Integer> quantities) ;

    void eraseSupermarketDistribution() ;

    boolean loggedAdmin();


    /**
     * Sorts the supermarket shelving units based on the specified catalog sorting strategy.
     * <p>This method sets the ordering strategy for sorting the shelving units according to
     * the specified ;@code sortingStrategy}. It then applies the chosen strategy to sort the
     * products of the catalog in the supermarket's shelving units.</p>
     *
     * @param sortingStrategy the sorting strategy to be applied, which determines the order
     *                        in which catalog products are placed in shelving units
     *
     * @throws IllegalStateException if the logged user is not the admin.
     * @throws IllegalArgumentException if the specified sorting strategy is invalid
     */
    void sortSupermarketByCatalogProducts(String sortingStrategy) ;


    /**
     * Sorts the products within the supermarket's shelving units based on the specified sorting strategy.
     * <p>This method sets the ordering strategy to arrange products in shelving units according to the
     * provided ;@code sortingStrategy} and then applies this strategy to sort the products.</p>
     *
     * @param sortingStrategy the sorting strategy to be applied, which determines the order
     *                        in which products are arranged within the supermarket's shelving units
     *
     * @throws IllegalStateException if the logged user is not the admin.
     * @throws IllegalArgumentException if the specified sorting strategy is invalid
     */
    void sortSupermarketProducts(String sortingStrategy) ;


    /**
     * Adds a specified product to a shelving unit at the given position and height.
     * <p>This method retrieves the product with the specified name from the catalog
     * and places it at the specified position and height within the shelving unit.</p>
     *
     * @param productName         the name of the product to add to the shelving unit
     * @param height              the height level within the shelving unit where the product should be placed
     * @param shelvingUnitPosition the position of the shelving unit in which to place the product
     *
     * @throws IllegalStateException if the logged user is not the admin.
     * @throws IllegalArgumentException if the product with the specified name is not found in the catalog
     */
    void addProductToShelvingUnit(String productName, int height, int shelvingUnitPosition) ;


    /**
     * Removes a product from a specified position within a shelving unit.
     * <p>This method removes the product located at the specified ;@code height} within the shelving
     * unit at the given ;@code shelvingUnitPosition} in the supermarket.</p>
     *
     * @param height               the height position within the shelving unit from which to remove the product
     * @param shelvingUnitPosition the position of the shelving unit in the supermarket's distribution
     *
     * @throws IllegalStateException if the logged user is not the admin.
     * @throws IllegalArgumentException if the specified height or shelving unit position is invalid or the logged user is not the admin.
     */
    void removeProductFromShelvingUnit(int height, int shelvingUnitPosition) ;


    /**
     * Swaps two products between specified positions within shelving units.
     * <p>This method exchanges the products located at the specified positions
     * (;@code position1}, ;@code height1}) and (;@code position2}, ;@code height2})
     * within the supermarket's shelving units.</p>
     *
     * @param position1 the position of the first shelving unit involved in the swap
     * @param position2 the position of the second shelving unit involved in the swap
     * @param height1   the height level within the first shelving unit from which to swap the product
     * @param height2   the height level within the second shelving unit from which to swap the product
     *
     * @throws IllegalStateException if the logged user is not the admin.
     * @throws IllegalArgumentException if any of the specified positions or height levels are invalid
     */
    void swapProductsFromShelvingUnits(int position1, int position2, int height1, int height2) ;


    /**
     * Modifies the temperature type of a shelving unit at the specified position.
     * <p>This method sets the temperature type of the shelving unit at the given ;@code position}
     * to the specified ;@code temperatureType}. The temperature type must match one of the
     * values in the ;@link ProductTemperature} enum.</p>
     *
     * @param position       the position of the shelving unit to modify
     * @param temperatureType the new temperature type for the shelving unit, as a string
     *
     * @throws IllegalStateException if the logged user is not the admin.
     * @throws IllegalArgumentException if the ;@code temperatureType} is invalid, if the ;@code position} is out of bounds
     * or if the shelving unit has products in it.
     */
    void modifyShelvingUnitType(int position, String temperatureType) ;


    /**
     * Adds a new shelving unit at the specified position with the specified temperature type.
     * <p>This method creates a shelving unit with the given temperature setting, placing it at the specified
     * position in the supermarket's distribution of shelving units and moving the next ones to the next position.</p>
     *
     * @param position    the position in the distribution where the shelving unit should be added
     * @param temperatureType the temperature setting for the shelving unit, as a string, which must match
     *                    a value in ;@link ProductTemperature}
     *
     * @throws IllegalStateException if the logged user is not the admin.
     * @throws IllegalArgumentException if the specified temperature does not match any value in ;@link ProductTemperature}
     */
    void addShelvingUnit(int position, String temperatureType) ;


    /**
     * Removes a shelving unit from the supermarket at the specified position.
     * <p>This method deletes the shelving unit located at the given ;@code position}
     * from the supermarket's shelving unit distribution.</p>
     *
     * @param position the position of the shelving unit to remove
     *
     * @throws IllegalStateException if the logged user is not the admin.
     * @throws IllegalArgumentException if the specified position is out of bounds
     */
    void removeShelvingUnit(int position) ;


    /**
     * Swaps the positions of two shelving units in the supermarket.
     * <p>This method exchanges the shelving units located at ;@code position1} and ;@code position2}
     * within the supermarket's shelving unit distribution.</p>
     *
     * @param position1 the position of the first shelving unit to swap
     * @param position2 the position of the second shelving unit to swap
     *
     * @throws IllegalStateException if the logged user is not the admin.
     * @throws IllegalArgumentException if either ;@code position1} or ;@code position2} is out of bounds
     */
    void swapShelvingUnits(int position1, int position2) ;


    /**
     * Empties all products from the shelving unit at the specified position.
     * <p>This method removes all products stored within the shelving unit located at
     * the given ;@code position} in the supermarket's shelving unit distribution.</p>
     *
     * @param position the position of the shelving unit to empty
     *
     * @throws IllegalStateException if the logged user is not the admin.
     * @throws IllegalArgumentException if the specified position is out of bounds
     */
    void emptyShelvingUnit(int position) ;

    /**
     * Checks if the supermarket has a product with the specified name.
     * <p>This method verifies if the supermarket contains a product with the specified name.</p>
     *
     * @param product the product dto to search for in the supermarket
     * @return true if the product is found in the supermarket, false otherwise
     *
     * @throws IllegalArgumentException if the product does not exist in the catalog
     */
    boolean supermarketHasProduct(ProductDto product);

    /**
     * Creates a new product in the catalog with specified attributes, keywords, related products, and values.
     * <p>This method defines a product with the specified name, temperature, price, image path, keywords,
     * and related products. Related products are identified by name and retrieved from the catalog.
     * The product is then added to the catalog.</p>
     *
     * @param productDto is a DTO containing the defintion of a new product
     *
     * @throws IllegalStateException if the logged user is not the admin. Also, if the image of the product cannot be copied.
     * @throws IllegalArgumentException if the specified temperature does not match any value in {@link ProductTemperature}.
     * Also, if the image path is not valid.
     */
    void createProduct(ProductDto productDto);

    /**
     * Removes a product from the catalog by its name.
     * <p>This method deletes the specified product from the catalog, identified by its name.</p>
     *
     * @param productName the name of the product to be removed from the catalog
     *
     * @throws IllegalStateException if the logged user is not the admin.
     * @throws IllegalArgumentException if the specified product does not exist in the catalog
     */
    void removeProduct(String productName) ;

    /**
     * Modifies the details of an existing product in the catalog.
     *
     *<p>This method updates the price, temperature, image path, and associated keywords
     * for a product specified by its name. It retrieves the product from the catalog
     * using the provided product name and modifies the product's properties based
     * on the provided
     *</p>
     *
     * @param productDto is a DTO containing the expected changes
     *
     * @throws IllegalStateException if the logged user is not the admin. Also, if the image cannot be deleted or copied (when necessary).
     * @throws IllegalArgumentException if the product name does not exist in the catalog. If the provided temperature is not a valid enum value for {@link ProductTemperature}.
     *  Also, if the image path is not valid.
     */
    void modifyProduct(ProductDto productDto);

    /**
     * Modifies the relationship between two products in the catalog.
     *
     *<p> This method updates the relationship between two products based on a given
     * relation value. It retrieves both products from the catalog using their
     * respective names and then updates the relation between the products with
     * the specified value.
     *</p>
     *
     * @param relatedProductDto containing the information for the modification
     *
     * @throws IllegalStateException if the logged user is not the admin.
     * @throws IllegalArgumentException if either of the products does not exist in the catalog.
     * If the relation cannot be modified for any other reason (e.g., invalid relation value).
     */
    void modifyProductRelation(RelatedProductDto relatedProductDto) ;

    /**
     * Searches for products in the catalog based on the provided search text.
     *
     *<p>This method performs a search operation on the product catalog, returning a list of
     * products whose names or attributes match the given search text. The search is typically
     * performed using keywords, product name.
     *</p>
     *
     * @param searchText The text used for searching products.
     *
     * @return A list of ;@link Product} objects that match the search criteria.
     *         If no products match, an empty list is returned.
     */
    List<ProductDto> searchProduct(String searchText) ;

    /**
     * Retrieves information about the supermarket.
     * This method fetches and returns general information about the supermarket.
     *
     * @return A string containing the supermarket's information.
     */
    String getSupermarketInfo()  ;

    /**
     * Retrieves information about the catalog.
     * This method fetches and returns general information about the catalog.
     *
     * @return A string containing the catalog's information.
     */
    String getCatalogInfo()  ;

    /**
     * Retrieves information about the specified shelving unit.
     * This method fetches and returns general information about the shelving unit.
     *
     * @param position The position of the shelving unit in the supermarket.
     * @return A string containing the supermarket's information.
     * @throws IllegalArgumentException if the position is out of bound.
     */
    String getShelvingUnitInfo(int position)  ;

    /**
     * Retrieves information about the specified product.
     * This method fetches and returns general information about the product.
     *
     * @param productName The productName to get information.
     * @return A string containing the product's information.
     * @throws IllegalArgumentException if the product does not exist.
     */
    String getProductInfo(String productName)  ;

    /**
     * Returns true if changes have been made to the data.
     * @return true if changes have been made, false otherwise.
     */
    boolean hasChangesMade() ;

    /**
     * Retrieves the {@link ShelvingUnitDto} for a specific position.
     *
     * @param position the position of the shelving unit to retrieve
     * @return the {@link ShelvingUnitDto} at the specified position
     * @throws IllegalArgumentException if the position is out of bounds
     */
    ShelvingUnitDto getShelvingUnit(int position);

    /**
     * Retrieves all the {@link ShelvingUnitDto}s.
     *
     * @return a list of {@link ShelvingUnitDto}s
     */
    List<ShelvingUnitDto> getShelvingUnits();

    /**
     * Retrieves the {@link ProductDto} for a specific product by name.
     *
     * @param productName the name of the product to retrieve
     * @return the {@link ProductDto} for the specified product
     * @throws IllegalArgumentException if the product with the specified name does not exist
     */
    ProductDto getProduct(String productName);

    /**
     * Retrieves all the {@link ProductDto}s.
     *
     * @return a list of {@link ProductDto}s
     */
    List<ProductDto> getProducts();

    /**
     * Retrieves the absolute path to the specified temperature-related icon.
     * <p>
     * The icon corresponds to one of the predefined temperature storage types:
     * <ul>
     *     <li><strong>AMBIENT</strong>: Icon representing ambient temperature storage.</li>
     *     <li><strong>REFRIGERATED</strong>: Icon representing refrigerated storage.</li>
     *     <li><strong>FROZEN</strong>: Icon representing frozen storage.</li>
     * </ul>
     * </p>
     *
     * @param temperature The temperature type as a {@code String}.
     *                     Valid values are "AMBIENT", "REFRIGERATED", or "FROZEN".
     *                     Case-sensitive input is expected.
     * @return The absolute path to the corresponding icon as a {@code String}.
     *         The path is constructed using the default temperature directory and the respective icon file name.
     * @throws IllegalArgumentException if the provided temperature type is invalid.
     */
    String getTemperatureIcon(String temperature);

    /**
     * Retrieves the absolute path to the default error image.
     * <p>
     * This image is used as a fallback when a temperature-related icon or other expected image is not found.
     * The method constructs the path using the assets directory for temperature images and appends the
     * "assets/error-img.png" file name.
     * </p>
     *
     * @return The absolute path to the error image as a {@code String}.
     * @throws IllegalStateException if the assets directory path cannot be resolved.
     */
    String getErrorImage();
}
