# Assignment 9-1: Improved Inventory Demo

Name:

ID:

## Problem Overview

In this assignment, you will extend the Inventory Demo application by adding **price** and **amount** tracking to items, implementing **input validation**, creating a **ReportView** that summarizes inventory data, and applying the **FlatLaf Dark** look and feel.

## Setup and Environment

- **WARNING:** Do not modify the `.github` or `gradle-tests` directories. These directories are essential for the automated grading process, and any changes may result in incorrect grading.

### Project Structure

The Maven project is already set up for you under the `lab91/` directory with the package `com.lab`. The key files you will be working with are:

| File | Description |
|------|-------------|
| `lab91/src/main/java/com/lab/Item.java` | Data class representing an inventory item |
| `lab91/src/main/java/com/lab/App.java` | Application entry point (sets up Look & Feel) |
| `lab91/src/main/java/com/lab/MainController.java` | Controller managing view transitions via CardLayout |
| `lab91/src/main/java/com/lab/DashboardView.java` | Main view with item table and action buttons |
| `lab91/src/main/java/com/lab/DetailView.java` | Form view for editing/adding items |
| `lab91/src/main/java/com/lab/ItemModel.java` | Model holding the list of items |

### Build and Run Commands

```bash
# Compile the project
mvn compile -f lab91/pom.xml

# Run the application
mvn exec:java -f lab91/pom.xml -Dexec.mainClass=com.lab.App
```

## Requirements and Specifications

Complete the following tasks. Each task describes what you need to implement and how it will be graded.

---

### Task 1: Add Price and Amount Fields to `Item` (10 pts)

Add two new fields to the `Item` class:

- **`price`** — a `double` representing the item's price.
- **`amount`** — an `int` representing the quantity in stock.

Each field must have a getter and a setter:
- `getPrice()` / `setPrice(double price)`
- `getAmount()` / `setAmount(int amount)`

Update the `Item` constructor to accept `price` and `amount` as parameters.

---

### Task 2: Add "Add" and "Report" Buttons to `DashboardView` (10 pts)

Add two buttons to the `DashboardView`:

- **"Add" button** — navigates to `DetailView` with empty fields so the user can enter a new item.
- **"Report" button** — navigates to `ReportView` to display inventory totals.

---

### Task 3: Update `DetailView` with Price and Amount Fields (7 pts)

Update `DetailView` so that it displays and allows editing of the **price** and **amount** fields, in addition to the existing name and description fields.

Use `JLabel` + `JTextField` pairs with labels: `"Name:"`, `"Description:"`, `"Price:"`, `"Amount:"`.

---

### Task 4: Input Validation on Save (30 pts)

When the user clicks the **Save** button in `DetailView`, validate all fields before saving:

| Constraint | Rule |
|------------|------|
| **Name** | Cannot be empty (after stripping whitespace) |
| **Price** | Must not be negative (`< 0`) |
| **Amount** | Must be a non-negative integer (`< 0` is invalid) |

If **any** constraint is violated, display a pop-up dialog using:

```java
JOptionPane.showMessageDialog(this, "Invalid details. Please correct them and try again.");
```

The user must remain on `DetailView` after the error (do **not** navigate back to the dashboard).

If all fields are valid, save the item and return to the dashboard.

---

### Task 5: Create `ReportView` (31 pts)

Create a `ReportView` that displays two computed values from the item list:

| Label | Calculation |
|-------|-------------|
| **"Total Amount"** | Sum of all items' `amount` values |
| **"Total Price"** | Sum of `amount × price` for each item |

Requirements:
- The totals must be **recalculated** each time the user navigates to `ReportView`.
- Display each total with a `JLabel` header (e.g., `"Total Amount"`) and a `JLabel` value next to it.
- Include an **"OK" button** that returns the user to `DashboardView`.

**Example with default data:**
| Item | Price | Amount |
|------|-------|--------|
| Laptop | 1000.0 | 1 |
| Coffee | 10.0 | 3 |

- Total Amount = 1 + 3 = **4**
- Total Price = (1 × 1000.0) + (3 × 10.0) = **1030.0**

---

### Task 6: Apply FlatLaf Dark Look and Feel (5 pts)

Use **FlatLaf Dark** (`FlatDarkLaf`) as the application's Look and Feel.

1. Add the FlatLaf dependency to `lab91/pom.xml`:
   ```xml
   <dependency>
       <groupId>com.formdev</groupId>
       <artifactId>flatlaf</artifactId>
       <version>3.7</version>
   </dependency>
   ```

2. Initialize it in `App.java` **before** creating any Swing components:
   ```java
   FlatDarkLaf.setup();
   ```

---

## Grading Breakdown

| Test | Points | What it checks |
|------|--------|----------------|
| Item has price field with getter/setter | 5 | `getPrice()` / `setPrice()` exist |
| Item has amount field with getter/setter | 5 | `getAmount()` / `setAmount()` exist |
| Dashboard has Add button | 5 | "Add" button present in `DashboardView` |
| Dashboard has Report button | 5 | "Report" button present in `DashboardView` |
| DetailView has price and amount fields | 7 | Price and Amount text fields exist in `DetailView` |
| Save validates non-empty name | 10 | Empty name triggers error dialog |
| Save validates non-negative price | 10 | Negative price triggers error dialog |
| Save validates non-negative amount | 10 | Negative amount triggers error dialog |
| Report button opens ReportView (Total Amount label) | 7 | "Total Amount" label appears in `ReportView` |
| ReportView shows Total Price label | 7 | "Total Price" label appears in `ReportView` |
| ReportView has OK button | 7 | OK button exists and returns to dashboard |
| ReportView shows correct total amount | 9 | Total amount value is computed correctly |
| ReportView shows correct total price | 8 | Total price value is computed correctly |
| Application uses FlatLaf Look and Feel | 5 | `FlatDarkLaf` is active |
| **Total** | **100** | |

## Additional Notes

- Refer to the lecture slides or the [FlatLaf website](https://www.formdev.com/flatlaf/) for setup instructions.
- Double-clicking a table row in `DashboardView` navigates to `DetailView` for editing that item.
- The application uses `CardLayout` in `MainController` to switch between views (`"DASHBOARD"`, `"DETAILS"`, `"REPORT"`).
