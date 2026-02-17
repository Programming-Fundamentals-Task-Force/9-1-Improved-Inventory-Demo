# Assignment 9-1: Improved Inventory Demo

Name:

ID:

## Problem Overview

In this assignment, you will add features to Inventory Demo to handle prices and amounts, create one more View, and improve its look and feel.

## Setup and Environment

- **WARNING:** Do not modify the `.github` or `gradle-tests` directories. These directories are essential for the automated grading process, and any changes may result in incorrect grading.

The Maven project has already been created for you. The package name is `com.lab`, and the project name (and directory) is `lab91`. Use `mvn compile` to build the project and `java -cp target/classes com.lab.App` or `mvn exec:java "-Dexec.mainClass=com.lab.App"` to run it.

## Requirements and Specifications

### Instructions

**Add price and amount fields**

- Add fields in `Item` to store the price and the amount of the item. Use `double` for the price and `int` for the amount.
- Expose the fields via appropriate getters and setters, namely, `getPrice()`, `setPrice()`, `getAmount()`, and `setAmount()`.
- The constructor must also takes all four values as parameters: `name`, `description`, `price`, and `amount`, respectively.
- `DetailView` must also show and allow editing of these new fields.

**Add "Add" button**

- Add an `Add` button in `DashboardView` to add a new item.
- `Add` button should bring the user to `DetailView` to enter the new item's details, which now includes the price and amount.
- `Save` button should validate all the fields according to the following constraints:
  - Name cannot be empty.
  - Price must not be negative.
  - Amount must be a non-negative integer.
- Violation of these constraints should result in a pop-up dialog, showing the message "Invalid details. Please correct them and try again." and stay in the `DetailView`.

**Create `ReportVew`**

- Add an `Report` button in `DashboardView` to summarize the total number of items and total price.
- Add `ReportView` to the application. The View should show only two things, "Total Amount" and "Total Price", calculated from all the items.
- `ReportView` must also have an `OK` button to return to `DashboardView`.

**Customize the GUI**

- Use FlatLaf Dark (`FlatDarkLaf`) Look-and-Feel. Update the `pom.xml` file and `App.java` to properly install and initialize FlatLaf.

## Additional Notes

- Refer to the slides or FlatLaf website on how to add FlatLaf to the project.
