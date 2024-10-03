# Contributing to Our Project

This guide will help you get started with setting up the repository and guide you through the process of contributing code using **VS Code**.

---

## Table of Contents

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
- [Questions](#questions)

---

## Cloning the Repository

To start working on the project, you first need to clone the repository to your local machine.

1. Open the folder where you want the project to be cloned with **VS Code**.
2. Open the **Terminal** in VS Code (`Ctrl + ~` on Windows/Linux or `Cmd + ~` on macOS).
3. Run the following command to clone the repository:

   ```bash
   git clone https://repo.fib.upc.es/grau-prop/subgrup-prop11.3.git
   ```

---

## Working with Git in VS Code

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

1. Go to the **Source Control** panel in VS Code.
2. Click on the icon that says **Create Pull Request**.
3. Click **New Merge Request**.
4. Choose your branch as the source and `main` as the target.
5. Add a descriptive title and details in the description box.
6. Assign a reviewer if necessary, and submit the merge request.

Your code will be reviewed and merged after approval.

---

## General Guidelines

### Branch Naming Conventions

To keep branches organized, please follow these naming conventions:

- **Features**: `feature/description-of-feature`
- **Bug Fixes**: `bugfix/description-of-bug`
- **Hotfixes**: `hotfix/description-of-fix`

Example:

```bash
git checkout -b feature/login-functionality
```

### Code Reviews and Approvals

- Every Merge Request (MR) should be reviewed by at least one other team member.
- All comments and feedback should be addressed before merging.
- Ensure you respond to or resolve any discussion threads in the MR.

### Testing

- Before creating a merge request, please ensure that your code passes all tests.
- Write unit tests for any new features or fixes.

---

## Questions

If you have any questions about contributing, feel free to reach out via:

- **Issues**: Open an issue in the repository with your question or concern.
- **Chat**: Use the team's communication tool (e.g., Slack, Discord).

Thank you for contributing!

```bash

### Key Points:
- **Cloning the Repository**: Provides instructions for cloning the repository using VS Code.
- **Branching and Git Operations**: Explains how to create new branches, add changes, pull the latest changes from `main`, and push branches.
- **Merge Requests**: Includes a step-by-step guide on creating a merge request.
- **Best Practices**: Enforces branch naming conventions, code reviews, and testing guidelines.

Feel free to customize the URLs, branch naming conventions, or specific guidelines to fit your team’s preferences!
```

---
