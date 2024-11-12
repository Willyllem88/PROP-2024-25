package edu.upc.subgrupprop113.supermarketmanager;

import javafx.util.Pair;

import java.util.*;

/**
 * Represents the supermarket system, managing all operations related to users,
 * catalog, shelving units, and various strategies for ordering and file import/export.
 * Implements a singleton pattern to ensure only one instance of the supermarket is created.
 */
public class Supermarket {

    private static Supermarket instance;
    private OrderingStrategy orderingStrategy;
    private ImportFileStrategy importFileStrategy;
    private ExportFileStrategy exportFileStrategy;
    private final ArrayList<User> registeredUsers;
    private User logedUser;
    private ArrayList<ShelvingUnit> shelvingUnits;
    private int shelvingUnitHeight;

    // Usernames and passwords for both admin and user
    private static final String ADMIN_NAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private static final String EMPLOYEE_NAME = "employee";
    private static final String EMPLOYEE_PASSWORD = "employee";

    /**
     * Private constructor to initialize the supermarket with default strategies and users.
     * Sets default instances for ordering, catalog, import/export strategies,
     * and adds initial users (admin and employee).
     */
    private Supermarket() {
        this.orderingStrategy = new BruteForce();
        this.shelvingUnits = new ArrayList<ShelvingUnit>();
        this.shelvingUnitHeight = 0;
        this.importFileStrategy = new ImportFileJSON();
        this.exportFileStrategy = new ExportFileJSON();
        this.registeredUsers = new ArrayList<>();
        this.registeredUsers.add(new Employee(EMPLOYEE_NAME, EMPLOYEE_PASSWORD));
        this.registeredUsers.add(new Admin(ADMIN_NAME, ADMIN_PASSWORD));

        this.logedUser = null;
    }

    /**
     * Returns the singleton instance of the supermarket, creating it if it does not exist.
     *
     * @return the single instance of {@code Supermarket}.
     */
    public static Supermarket getInstance() {
        if (instance == null) {
            instance = new Supermarket();
        }
        return instance;
    }

    /**
     * Logs a user into the system with the specified username and password.
     * Verifies that no other user is already logged in and checks the provided credentials.
     *
     * @param username the username of the user attempting to log in.
     * @param password the password of the user attempting to log in.
     * @throws IllegalStateException if a user is already logged in.
     * @throws IllegalArgumentException if the username does not exist or if the password is incorrect.
     */
    public void logIn(final String username,final String password) {
        if (this.logedUser != null) throw new IllegalStateException("There is already a logged in user.");

        User user = findUser(username);
        if (user == null) throw new IllegalArgumentException("No such user found.");
        if (!user.isPasswordCheck(password)) throw new IllegalArgumentException("Wrong password.");

        this.logedUser = user;
    }

    /**
     * Logs out the currently logged-in user.
     *
     * @throws IllegalStateException if no user is currently logged in.
     */
    public void logOut() {
        if (this.logedUser == null) throw new IllegalStateException("There is no logged user.");

        this.logedUser = null;
    }

    /**
     * Retrieves the currently logged-in user.
     *
     * @return the {@link User} who is currently logged in, or {@code null} if no user is logged in.
     */
    public User getLogedUser() {
        return this.logedUser;
    }

    /**
     * Creates the distribution of shelving units based on specified temperature types and quantities.
     *
     * <p>This method configures the distribution of shelving units in the supermarket to match the
     * specified shelving height and distribution details. The supermarket must be empty.</p>
     *
     *
     * @param shelvingHeight the height of each shelving unit, specified in integer units
     * @param distribution   a set of pairs representing the temperature type and the quantity of units
     *                       for each temperature type. Each pair contains:
     *                       - Key: a constant in {@link ProductTemperature}.
     *                       - Value: an integer representing the quantity of units for the given temperature type.
     *
     * @throws IllegalStateException if the supermarket distribution is not empty or if the logged in user is not the admin.
     */
    public void createDistribution(int shelvingHeight, final ArrayList<Pair<ProductTemperature, Integer>> distribution) {
        checkLoggedUserIsAdmin();
        if (this.shelvingUnitHeight != 0 || !this.shelvingUnits.isEmpty()) throw new IllegalStateException("The supermarket distribution must be empty.");

        this.shelvingUnitHeight = shelvingHeight;

        int lastPosition = 0;
        for (Pair<ProductTemperature, Integer> shelvingUnitDefinition : distribution) {
            ProductTemperature temperature = shelvingUnitDefinition.getKey();
            int nbUnits = shelvingUnitDefinition.getValue();
            for (int i = 0; i < nbUnits; i++) {
                addShelvingUnit(lastPosition + i, temperature);
            }
            lastPosition += nbUnits;
        }
    }

    /**
     * Clears the current shelving unit distribution.
     * <p>This method removes all shelving units from the distribution list and resets
     * the shelving unit height to zero. Once called, the distribution will be empty,
     * and {@code shelvingUnitHeight} will be set to its default value (0).</p>
     */
    public void eraseDistribution() {
        checkLoggedUserIsAdmin();
        this.shelvingUnits.clear();
        this.shelvingUnitHeight = 0;
    }

    /**
     * Sorts the catalog products of the supermarket into the shelving units based on the current ordering strategy.
     * <p>This method retrieves the current product catalog and organizes them in the
     * {@code shelvingUnits} according to the rules defined by the
     * {@link OrderingStrategy}. The ordering strategy is applied to the list of shelving
     * units along with all products currently available in the catalog.</p>
     *
     * @throws IllegalStateException if the current user is not the admin.
     * @throws IllegalArgumentException if the Strategy fails
     */
    public void sortSupermarketCatalog() {
        checkLoggedUserIsAdmin();
        Catalog catalog = Catalog.getInstance();
        this.shelvingUnits = this.orderingStrategy.orderSupermarket(
                this.shelvingUnits,
                catalog.getAllProducts()
        );
    }

    /**
     * Sorts the products within the supermarket's shelving units based on the current ordering strategy.
     * <p>This method applies the specified {@link OrderingStrategy} to the list of
     * {@code shelvingUnits}, organizing products according to the rules defined in the strategy.
     * The set of products is obtained by calling {@code getAllProductsShelvingUnit()}, which retrieves all
     * products currently stored in the shelving units.</p>

     * @throws IllegalStateException if the current user is not the admin.
     * @throws IllegalArgumentException if the Strategy fails
     */
    public void sortSupermarketProducts() {
        checkLoggedUserIsAdmin();
        this.shelvingUnits = this.orderingStrategy.orderSupermarket(
                this.shelvingUnits,
                getAllProductsShelvingUnits()
        );
    }

    /**
     * Exports the current supermarket configuration to a specified file, including the product catalog and shelving units.
     * <p>This method verifies that the current user is an administrator, retrieves all products from the catalog, and
     * exports the products and shelving units using the specified file name.</p>
     *
     * @param filename the name of the file to which the supermarket data will be exported.
     * @throws IllegalStateException if the current user is not an administrator.
     * @throws IllegalArgumentException if the export fails because of the Strategy
     */
    public void exportSupermarket(String filename) {
        checkLoggedUserIsAdmin();
        Catalog catalog = Catalog.getInstance();
        ArrayList<Product> products = new ArrayList<Product>(catalog.getAllProducts());
        SupermarketData supermarketData = new SupermarketData(this.shelvingUnitHeight, products, this.shelvingUnits);
        this.exportFileStrategy.exportSupermarket(supermarketData, filename);
    }

    /**
     * Imports a supermarket configuration from a specified file, updating the shelving units and catalog with new data.
     * <p>This method checks that the current user is an administrator and ensures the supermarket has no pre-existing
     * shelving unit distribution. It then imports shelving units and catalog products from the file specified by
     * {@code filename}, clears the existing catalog, and validates the new shelving units for consistency in
     * temperature, catalog presence, unique UIDs, and uniform height.</p>
     *
     * <p><strong>Note:</strong> The shelving unit height is set to the height of the first imported unit, or to 0 if no units are imported.</p>
     *
     * @param filename the name of the file containing the supermarket data to import.
     * @throws IllegalStateException if there is an existing shelving unit distribution or if the product relations are incorrect.
     * @throws IllegalArgumentException if any imported shelving unit fails validation in {@code checkRTsImportShelvingUnits} or if any imported product fails the restrictions of the catalog.
     */
    public void importSupermarket(String filename) {
        checkLoggedUserIsAdmin();
        if (this.shelvingUnitHeight != 0) throw new IllegalStateException("The supermarket distribution must be empty.");
        SupermarketData supermarketData = this.importFileStrategy.importSupermarket(filename);
        ArrayList<ShelvingUnit> newShelvingUnits = supermarketData.getDistribution();
        ArrayList<Product> newCatalog = supermarketData.getProducts();
        Catalog catalog = Catalog.getInstance();
        catalog.clear();
        catalog.setAllProducts(newCatalog);
        checkRTsImportShelvingUnits(newShelvingUnits);
        this.shelvingUnits = newShelvingUnits;
        this.shelvingUnitHeight = supermarketData.getShelvingUnitHeight();
    }



    //TO DO
    public void addShelvingUnit(int position, final ProductTemperature temperature) {
        int uid = 0;
        if (!this.shelvingUnits.isEmpty()) uid = this.shelvingUnits.getLast().getUid() + 1;

        ShelvingUnit unit = new ShelvingUnit(uid, this.shelvingUnitHeight, temperature);
        this.shelvingUnits.add(unit);
    }

    //TO DO
    public void addProductToShelvingUnit(int position,int height, final Product product) {
        this.shelvingUnits.get(position).addProduct(product, height);
    }

    /**
     * Searches for a user by their username within the registered users.
     *
     * @param username the username of the user to search for.
     * @return the {@link User} object if found; {@code null} if no user with the specified username exists.
     */
    public User findUser(String username) {
        for (User u : registeredUsers)
            if (u.getUsername().equals(username)) return u;
        return null;
    }

    /**
     * Retrieves the list of shelving units currently configured in the distribution.
     *
     * @return an {@link ArrayList} of {@link ShelvingUnit} objects representing the current
     *         shelving units in the supermarket distribution.
     */
    public List<ShelvingUnit> getShelvingUnits() {
        return Collections.unmodifiableList(this.shelvingUnits);
    }

    /**
     * Retrieves the height of each shelving unit in the distribution.
     *
     * @return an integer representing the height of the shelving units.
     */
    public int getShelvingUnitHeight() {
        return this.shelvingUnitHeight;
    }

    /**
     * Sets the ordering strategy for the current instance.
     * <p>This method assigns the specified {@link OrderingStrategy} to be used in
     * organizing or sorting items according to the provided strategy's rules.
     * Replacing the strategy will affect all future ordering behavior.</p>
     *
     * @param orderingStrategy the {@link OrderingStrategy} to be applied.
     */
    public void setOrderingStrategy(OrderingStrategy orderingStrategy) {
        checkLoggedUserIsAdmin();
        this.orderingStrategy = orderingStrategy;
    }

    /**
     * Sets the strategy for importing files into the supermarket system.
     * <p>This method allows the selection of a specific import strategy to manage
     * the way data is read and incorporated from files.</p>
     *
     * @param importFileStrategy the {@link ImportFileStrategy} to be used for importing files.
     */
    public void setImportFileStrategy(ImportFileStrategy importFileStrategy) {
        checkLoggedUserIsAdmin();
        this.importFileStrategy = importFileStrategy;
    }

    /**
     * Sets the strategy for exporting files from the supermarket system.
     * <p>This method allows the selection of a specific export strategy to manage
     * the way data is written and saved to files.</p>
     *
     * @param exportFileStrategy the {@link ExportFileStrategy} to be used for exporting files.
     */
    public void setExportFileStrategy(ExportFileStrategy exportFileStrategy) {
        checkLoggedUserIsAdmin();
        this.exportFileStrategy = exportFileStrategy;
    }


    /**
     * Retrieves all products currently stored in the supermarket's shelving units.
     * <p>This method iterates through each shelving unit and collects all non-null products
     * up to the height specified by {@code shelvingUnitHeight}. It returns a list
     * containing all products currently placed within the shelving units.</p>
     *
     * @return an {@code ArrayList} of {@link Product} objects representing all products in the shelving units.
     *         If no products are stored, an empty list is returned.
     */
    public List<Product> getAllProductsShelvingUnits() {
        ArrayList<Product> productsShelvingUnit = new ArrayList<>();
        for (ShelvingUnit shelvingUnit : this.shelvingUnits) {
            for (int i = 0; i < this.shelvingUnitHeight; i++) {
                Product product = shelvingUnit.getProduct(i);
                if (Objects.nonNull(product)) {
                    productsShelvingUnit.add(product);
                }
            }
        }

        return Collections.unmodifiableList(productsShelvingUnit);
    }

    private void checkLoggedUserIsAdmin() {
        checkLoggedUser();
        if (!this.logedUser.isAdmin()) throw new IllegalStateException("The logged in user is not admin.");
    }

    private void checkLoggedUser() {
        if (this.logedUser == null) throw new IllegalStateException("There is no logged in user.");
    }

    /**
     * Validates the imported shelving units for consistency in temperature, catalog presence, unique identifiers, and height.
     * <p>This method verifies that each shelving unit in the provided list has consistent product temperatures with the unit’s
     * temperature setting, and that each product exists in the catalog. Additionally, it ensures all shelving units have a
     * uniform height and unique UIDs.</p>
     *
     * <p><strong>Note:</strong> This method relies on the {@link Catalog} singleton instance for catalog validation.</p>
     *
     * @param shelvingUnits a list of {@link ShelvingUnit} objects to validate.
     * @throws IllegalArgumentException if:
     *         <ul>
     *             <li>any product within a shelving unit has a different temperature than the unit's temperature setting,</li>
     *             <li>any product in a shelving unit is not present in the catalog,</li>
     *             <li>the shelving units have differing heights,</li>
     *             <li>any two shelving units share the same UID.</li>
     *         </ul>
     */
    public void checkRTsImportShelvingUnits(ArrayList<ShelvingUnit> shelvingUnits) {
        Catalog catalog = Catalog.getInstance();
        HashSet<Integer> heights = new HashSet<>();
        HashSet<Integer> uids = new HashSet<>();

        for (ShelvingUnit shelvingUnit : shelvingUnits) {
            heights.add(shelvingUnit.getHeight());
            uids.add(shelvingUnit.getUid());
            for (int i = 0; i < shelvingUnit.getHeight(); i++) {
                Product product = shelvingUnit.getProduct(i);
                if (Objects.nonNull(product)) {
                    if (product.getTemperature() != shelvingUnit.getTemperature()){
                        throw new IllegalArgumentException("There is at least one product in a shelving unit with different temperatures.");
                    }
                    if (!catalog.contains(product))
                        throw new IllegalArgumentException("There is at least one product not contained in the catalog.");
                }
            }
        }

        if (heights.size() != 1) throw new IllegalArgumentException("More than one height is provided.");
        if (uids.size() != shelvingUnits.size()) throw new IllegalArgumentException("There is at least one duplicated uid.");
    }

    public String getShelvingUnitInfo(int position) {
        if (position < 0 || position >= this.shelvingUnits.size()) throw new IllegalArgumentException("Position is out of bounds.");
        return this.shelvingUnits.get(position).getInfo();
    }

    //TODO
    public String getInfo() {
        return "Supermarket";
    }
}
