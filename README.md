# Supermarket Manager Project

This project uses Maven for building, compiling, and running the application.

## Setting Up and Running the Project

### Prerequisites
- **Java 22**: Install from [Amazon Corretto](https://aws.amazon.com/corretto/).
- **Maven**: Use the Maven Wrapper (`./mvnw` or `mvnw.cmd` on Windows) if Maven is not installed.

---

### Steps to Run the Project

This set of commands allows you to compile, test, and run the project. Start by navigating to 
the `FONTS` directory, which contains the source files. Then, use `mvn clean package` to remove any 
previously generated files and create a deployable package. Next, `mvn compile` compiles the source 
code. Once the code is compiled, `mvn test` runs all the tests in the project to ensure everything 
works as expected. Finally, the `mvn exec:java` command launches the application, specifying the 
main class by its fully qualified name.

As said previously, if Maven is not installed in `./mvnw` and `mvnw.cmd` can be also used.

```bash
# Navigate to the project directory
cd FONTS

# Clean, package, compile, and run tests
mvn clean package
mvn compile
mvn test

# Run the application
mvn javafx:run
```

For running the CLI (Command Line Interface) version of the application, use the following command:

```bash
mvn exec:java -Dexec.mainClass=edu.upc.subgrupprop113.supermarketmanager.DomainControllerDriver
```

---

#### Running a Specific Test

If you need to execute only a specific test, use the following command, replacing `TestClassName` 
with the name of the desired test class. This is helpful for debugging or validating individual 
test cases without running the entire suite:

```bash
mvn -Dtest=TestClassName test
```

---

## Additional information

The application comes with the following default profiles for access:

| Role             | Username  | Password  |
|------------------|-----------|-----------|
| **Administrator** | `admin`   | `admin`   |
| **Employee**      | `employee`   | `employee` |

## Troubleshooting

- **Permissions**: On Linux/macOS, ensure `./mvnw` has execute permissions:
  ```bash
  chmod +x ./mvnw
  ```
- **Missing Dependencies**: Run `./mvnw clean install` to fetch dependencies.
