# Contributing to Our Project

This guide will help you get started with setting up the repository and guide you through the process of contributing code.

---

## Table of Contents

- [Important information when contributing](#important-information-when-contributing)
- [Cloning the Repository](#cloning-the-repository)
- [Working with Git in VS Code](#working-with-git-in-vs-code)
  - [Creating a New Branch](#creating-a-new-branch)
  - [Adding Your Changes](#adding-your-changes)
  - [Pulling Latest Changes from Main](#pulling-latest-changes-from-main)
  - [Pushing Your Branch](#pushing-your-branch)
  - [Creating a Merge Request](#creating-a-merge-request)
- [General Guidelines](#general-guidelines)
  - [Branch Naming Conventions](#branch-naming-conventions)
  - [Code Reviews and Approvals](#code-reviews-and-approvals)
  - [Testing](#testing)
- [Folder Structure](#folder-structure)
  - [1. `src/main/java/`](#1-srcmainjava)
  - [2. `src/main/resources/`](#2-srcmainresources)
  - [How to Use the Folder Structure](#how-to-use-the-folder-structure)
    - [1. Java Classes](#1-java-classes)
    - [2. FXML Files](#2-fxml-files)
    - [3. CSS Files](#3-css-files)
- [Workflow for FXML Views and Controllers](#workflow-for-fxml-views-and-controllers)
  - [Step 1: Create the FXML File](#step-1-create-the-fxml-file)
  - [Step 2: Create the Controller](#step-2-create-the-controller)
  - [Step 3: Link the FXML and Controller](#step-3-link-the-fxml-and-controller)
- [Team Workflow Recommendations](#team-workflow-recommendations)

---

## Important information when contributing

- **Do not push directly to the `main` branch**. Always create a new branch for your changes. `main` is supposed to be the stable version of the code. All new features and fixes should be developed in separate branches in order to keep the codebase clean and organized.
- **Always pull the latest changes from `main` before pushing your branch**. This ensures that your branch is up-to-date with the latest changes and helps prevent merge conflicts.
- **Each new feature or bug fix must be reviewed by another team member**. This ensures that the code is of high quality and follows the project's standards.

## Cloning the Repository

To start working on the project, you first need to clone the repository to your local machine.

1. Open the folder where you want the project to be cloned.
2. Open a **Terminal**.
3. Run the following command to clone the repository:

   ```bash
   git clone https://repo.fib.upc.es/grau-prop/subgrup-prop11.3.git
   ```

---

## Working with Git in VS Code

### Setting Up GitLab extension in VS Code

1. Install the **GitLab Workflow** extension in VS Code.
2. Click on the **GitLab** icon in the sidebar.
3. Click **Sign in with GitLab** and follow the instructions to authenticate your account.
4. When asked to choose a GitLab instance, choose the one where the repository is hosted.
5. Create a **personal access token** in GitLab and paste it in the extension to authenticate. Make sure to save the token in a secure place.
6. Now you can perform git operations directly from VS Code and keep your account authenticated.

### Creating a New Branch

Before starting any new feature or fix, create a **new branch**.

1. In **VS Code**, navigate to the bottom-left corner and look for the name `main`. This is the current branch.
2. To switch branches, click on `main` and select **Create new branch**, or select an existing one
3. Enter the name of your new branch (e.g., `feature/login-functionality`).
4. Press `Enter` to create the branch and switch to it.
5. Now, you are on your new branch and can start making changes. All the changes done to the code will be isolated to this branch.

### Adding Your Changes

After making changes to the code:

1. Go to the **Source Control** panel on the left sidebar of VS Code.
2. Stage the changes that you want to commit by clicking the `+` icon next to each file, or click **Stage All Changes** to stage everything.
3. Commit your changes:
   - Enter a meaningful commit message in the text box.
   - Click the checkmark (✓) button to commit.

Alternatively, you can use the terminal:

```bash
git add . # Stage all changes
git commit -m "Your descriptive commit message"
```

### Pulling Latest Changes from Main

Before pushing your changes, make sure your branch is up-to-date with `main`.

1. First, checkout the `main` branch by clicking the branch name in the bottom-left corner and selecting `main`. Or use the terminal:

   ```bash
   git checkout main
   ```

2. Pull the latest changes by clicking the **Synchronize Changes** button in the bottom-right corner of VS Code. Or use the terminal:

   ```bash
   git pull origin main
   ```

3. Go back to your feature branch by clicking the branch name and selecting your branch. Or use the terminal:

   ```bash
   git checkout feature/your-feature-name
   ```

4. Merge the latest changes from `main` into your branch:

   ```bash
   git merge main
   ```

Resolve any merge conflicts if needed.

### Pushing Your Branch

Once you’ve committed your changes and merged any updates from `main`:

1. Navigate to the **Source Control** panel in VS Code.
2. If there aren't any other changes to pull, click the **Synchronize Changes** button to pull the latest changes.

   - Note that this button will only appear if there are no incoming changes.

3. Alternatively, you can push your branch using the terminal:

   ```bash
   git push origin feature/your-feature-name
   ```

### Creating a Merge Request

Now that your branch is pushed, create a Merge Request (MR) on GitLab to merge your changes into the `main` branch:

1. Go to the GitLab repository page.
2. Click on **Merge Requests** in the left sidebar.
3. Click **New Merge Request**.
4. Choose your branch as the source and `main` as the target.
5. Add a descriptive title and details in the description box.
6. Assign a reviewer (another member of the team), and submit the merge request.

Your code will be reviewed and merged after approval.

---

## General Guidelines

### Branch Naming Conventions

To keep branches organized, please follow these naming conventions:

- **Features**: `feature/description-of-feature`
- **Bug Fixes**: `bugfix/description-of-bug`
- **Hotfixes**: `hotfix/description-of-fix`

### Code Reviews and Approvals

- Every Merge Request (MR) should be reviewed by at least one other team member.
- All comments and feedback should be addressed before merging.
- Ensure you respond to or resolve any discussion threads in the MR.

### Testing

- Before creating a merge request, please ensure that your code passes all tests.
- Write unit tests for any new features or fixes.

---

## Working with IntelliJ IDEA

### **Opening the Project**

Open the `pom.xml` in `FONTS` folder by using `Ctrl + O` to open the project folder in IntelliJ IDEA or go to Files > Open.

This will open the project in IntelliJ IDEA with the proper Maven configuration.

---

## Folder Structure

### **1. `src/main/java/`**

This folder contains all the Java code for the project. The structure is divided as follows:

| **Folder**                    | **Description**                                                                                                                                      |
| ----------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------- |
| `controllers/`                | Contains all JavaFX controllers that manage FXML views. Each FXML file should have a corresponding controller. It also contains the DomainController |
| `models/`                     | Contains data models representing core entities in the application (e.g., `Product`, `User`).                                                        |
| `services/`                   | Contains backend logic, such as file import/export, ordening strategies, ...                                                                         |
| `utils/`                      | Contains utility classes (e.g., serializers, helpers).                                                                                               |
| `Main.java`                   | The application entry point that initializes the JavaFX `Stage` and loads the first FXML view.                                                       |
| `DomainControllerDriver.java` | A driver class to test the DomainController.                                                                                                         |

---

### **2. `src/main/resources/`**

This folder contains all non-Java resources, such as FXML files, CSS stylesheets, and other assets.

| **Folder**      | **Description**                                                                                                     |
| --------------- | ------------------------------------------------------------------------------------------------------------------- |
| `fxml/`         | Contains all FXML files for the user interface. Each file should have a corresponding controller in `controllers/`. |
| `css/`          | Contains global and view-specific CSS stylesheets. See the section below for details on usage.                      |
| `images/`       | Contains images used in the UI, such as logos, backgrounds, or icons.                                               |
| `fonts/`        | Contains custom fonts.                                                                                              |
| `dataExamples/` | Contains example or configuration data files (e.g., JSON files).                                                    |

---

### **How to Use the Folder Structure**

#### **1. Java Classes**

1. **Controllers**: Place in `controllers/`. Each FXML file must have a corresponding controller class.
2. **Models**: Represent your application's data (e.g., `Product`, `User`) and go in `models/`.
3. **Services**: Handle business logic, such as file processing or authentication, and go in `services/`.
4. **Strategies**: Implement algorithms and go in `strategies/`.
5. **Utilities**: Place reusable helper methods (e.g., UI helpers or serializers) in `utils/`.

---

#### **2. FXML Files**

- Place all FXML files in the `fxml/` folder.
- Each FXML file should have:
  - A corresponding controller in `controllers/`.
  - A specific purpose or view (e.g., `logIn.fxml` for the login screen).

---

#### **3. CSS Files**

- **Global Styles**:

  - The `css/global.css` file is applied to the entire application.
  - Define common styles, such as:
    - Color palette.
    - Font family and sizes.
    - Button styles (e.g., `.button-primary`, `.button-secondary`).
  - Load it in `Main.java`:

    ```java
    scene.getStylesheets().add(getClass().getResource("/css/global.css").toExternalForm());
    ```

- **View-Specific Styles**:

  - For styles specific to one view, create a separate CSS file in `css/`.
  - Example: `css/login.css` for `logIn.fxml`.
  - Load it in the FXML file:

    ```xml
    <VBox xmlns="http://javafx.com/javafx/17.0.1" xmlns:fx="http://javafx.com/fxml/1" stylesheets="@../css/login.css">
        <!-- FXML Content -->
    </VBox>
    ```

---

### **Workflow for FXML Views and Controllers**

#### **Step 1: Create the FXML File**

1. Place the file in the `fxml/` folder.
2. Design the UI using Scene Builder or manually in XML.
3. Define the `fx:controller` attribute to link to the corresponding controller class.

##### Example: `logIn.fxml`

```xml
<VBox xmlns="http://javafx.com/javafx/17.0.1" xmlns:fx="http://javafx.com/fxml/1" fx:controller="edu.upc.subgrupprop113.supermarketmanager.controllers.LogInController">
    <Button text="Log In" onAction="#handleLogin"/>
</VBox>
```

---

#### **Step 2: Create the Controller**

1. Place the controller in `controllers/`.
2. Define it with `@FXML` annotations to bind FXML elements.
3. Implement event handlers for user interactions.

##### Example: `LogInController.java`

```java
package edu.upc.subgrupprop113.supermarketmanager.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class LogInController {
    @FXML
    private Button loginButton;

    @FXML
    private void handleLogin() {
        System.out.println("Login button clicked!");
    }
}
```

---

#### **Step 3: Link the FXML and Controller**

1. Load the FXML in `Main.java` or another controller.
2. Set the scene and apply the global CSS.

##### Example: `Main.java`

```java
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/logIn.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/global.css").toExternalForm());

        primaryStage.setTitle("Supermarket Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

---

### **Team Workflow Recommendations**

1. **Global Consistency**:

   - Use `global.css` for shared styles like fonts, buttons, and color palette.
   - Define reusable styles as classes (e.g., `.button-primary`, `.error-label`).

2. **View-Specific Styles**:

   - Use separate CSS files for complex, view-specific styles.
   - Keep FXML clean by avoiding inline styles.

3. **Controller Logic**:

   - Keep controllers focused on handling user interactions and view logic.
   - Delegate business logic to `services/`.

4. **Separation of Concerns**:
   - Place FXML in `fxml/`, controllers in `controllers/`, and shared logic in `services/`.

## Using Icons in the UI

The project uses the Ikonli library to display icons in the UI. Ikonli provides a wide range of icon packs that can be used in JavaFX applications.

We currently use the `Feather` icon pack, which includes a variety of icons for different purposes.

A list of available icons can be found on the [Ikonli Feather Cheat Sheet](https://kordamp.org/ikonli/cheat-sheet-feather.html).

To use Ikonli icons in any controller, follow these steps:

1. Import the necessary classes:

   ```java
   import org.kordamp.ikonli.javafx.FontIcon;
   ```

2. Create a new `FontIcon` object with the desired icon:

   ```java
   FontIcon icon = new FontIcon("fth-power");
   ```

If you want to use a different icon pack, you will need to add the necessary dependencies to the `pom.xml` file and update the module-info.java file.

---
