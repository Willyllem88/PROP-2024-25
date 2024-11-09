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
     * specified shelving height and distribution details. Any previous shelving distribution will be
     * replaced by the new configuration specified in the parameters.</p>
     *
     *  <p><b>Warning:</b> The current shelving distribution will be cleared and replaced by the new
     * distribution specified in the parameters.</p>
     *
     * @param shelvingHeight the height of each shelving unit, specified in integer units
     * @param distribution   a set of pairs representing the temperature type and the quantity of units
     *                       for each temperature type. Each pair contains:
     *                       - Key: a constant in {@link ProductTemperature}.
     *                       - Value: an integer representing the quantity of units for the given temperature type.
     *
     * @throws IllegalStateException if the supermarket distribution is not empty
     */
    public void createDistribution(int shelvingHeight, final ArrayList<Pair<ProductTemperature, Integer>> distribution) {
        checkLoggedUserIsAdmin();
        if (!Objects.equals(this.shelvingUnitHeight, 0) || !this.shelvingUnits.isEmpty()) throw new IllegalStateException("The supermarket distribution must be empty.");

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
        //TODO
        //Eliminate the product binding
        this.shelvingUnits.clear();
        this.shelvingUnitHeight = 0;
    }

    /**
     * Sorts the catalog products of the supermarket into the shelving units based on the current ordering strategy.
     * <p>This method retrieves the current product catalog and organizes the
     * {@code shelvingUnits} according to the rules defined by the
     * {@link OrderingStrategy}. The ordering strategy is applied to the list of shelving
     * units along with all products currently available in the catalog.</p>
     *
     * <p><strong>Note:</strong> An ordering strategy must be defined before
     * calling this method. If no ordering strategy is set, an
     * {@link IllegalStateException} is thrown.
     * </p>
     */
    public void sortSupermarketCatalog() {
        Catalog catalog = Catalog.getInstance();
        this.shelvingUnits = this.orderingStrategy.orderSupermarket(
                this.shelvingUnits,
                new HashSet<>(catalog.getAllProducts())
        );
    }

    /**
     * Sorts the products within the supermarket's shelving units based on the current ordering strategy.
     * <p>This method applies the specified {@link OrderingStrategy} to the list of
     * {@code shelvingUnits}, organizing products according to the rules defined in the strategy.
     * The set of products is obtained by calling {@code getAllProductsShelvingUnit()}, which retrieves all
     * products currently stored in the shelving units.</p>
     */
    public void sortSupermarketProducts() {
        checkLoggedUserIsAdmin();
        this.shelvingUnits = this.orderingStrategy.orderSupermarket(
                this.shelvingUnits,
                new HashSet<>(getAllProductsShelvingUnits())
        );
    }

    public void exportSupermarket(String filename) {
        checkLoggedUserIsAdmin();
        Catalog catalog = Catalog.getInstance();
        ArrayList<Product> products = (ArrayList<Product>) catalog.getAllProducts();
        this.exportFileStrategy.exportSupermarket(products, this.shelvingUnits, filename);
    }

    public void importSupermarket(String filename) {
        checkLoggedUserIsAdmin();
        if (this.shelvingUnitHeight != 0) throw new IllegalStateException("The supermarket distribution must be empty.");
        Pair<ArrayList<Product>, ArrayList<ShelvingUnit>> supermarketData = this.importFileStrategy.importSupermarket(filename);
        ArrayList<ShelvingUnit> newShelvingUnits = supermarketData.getValue();
        ArrayList<Product> newCatalog = supermarketData.getKey();
        if (!supermarketData.getValue().isEmpty()) this.shelvingUnitHeight = supermarketData.getValue().getFirst().getHeight();
        else this.shelvingUnitHeight = 0;
        Catalog catalog = Catalog.getInstance();
        catalog.clear();
        //TO DO
        //ADD PRODUCTS TO CATALOG
        checkRTsImportShelvingUnits(newShelvingUnits);
        this.shelvingUnits = newShelvingUnits;
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

    public void checkLoggedUserIsAdmin() {
        if (this.logedUser == null) throw new IllegalStateException("There is no logged in user.");
        if (!this.logedUser.isAdmin()) throw new IllegalStateException("The logged in user is not admin.");
    }

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
                    if (product.getTemperature() != shelvingUnit.getTemperature())
                        throw new IllegalArgumentException("There is at least one product in a shelving unit with different temperatures.");
                    if (!catalog.contains(product))
                        throw new IllegalArgumentException("There is at least one product not contained in the catalog.");

                }
            }
        }

        if (heights.size() != 1) throw new IllegalArgumentException("More than one height is provided.");
        if (uids.size() != shelvingUnits.size()) throw new IllegalArgumentException("There is at least one duplicated uid.");
    }
}
