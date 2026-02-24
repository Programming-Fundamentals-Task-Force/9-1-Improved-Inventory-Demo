package org.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.swing.launcher.ApplicationLauncher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.Robot;
import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.timing.Pause;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JTableFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.JOptionPaneFixture;
import org.assertj.swing.timing.Timeout;

import javax.swing.*;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.lab.*;

/**
 * Test suite for Assignment 9-1: Improved Inventory Demo
 *
 * This class uses AssertJ Swing to test Swing GUI applications.
 * AssertJ Swing simulates user interactions (clicks, typing, navigation)
 * and verifies that the UI components behave correctly.
 *
 * KEY CONCEPTS:
 * - Robot: Simulates user input (mouse clicks, keyboard typing)
 * - FrameFixture: Wraps the JFrame and provides methods to interact with components
 * - GenericTypeMatcher: Finds components based on custom criteria
 * - Each test runs in isolation with fresh setup/teardown
 *
 * TESTING STRATEGY:
 * 1. Setup creates a fresh application instance for each test
 * 2. Tests find components by their properties (text, type, position)
 * 3. Tests simulate user actions (click, type, double-click)
 * 4. Tests verify expected behavior (dialogs appear, navigation works)
 * 5. Teardown cleans up the GUI to prevent interference between tests
 */
class AppTest {
    // FrameFixture wraps the main JFrame and provides testing methods
    private FrameFixture window;

    // Application components we create for testing
    private ItemModel model;
    private MainController controller;

    // Robot simulates user interactions (mouse, keyboard)
    private Robot robot;

    @BeforeEach
    protected void setUp() {
        // Create robot with optimized settings
        robot = BasicRobot.robotWithNewAwtHierarchy();
        robot.settings().delayBetweenEvents(5);
        robot.settings().eventPostingDelay(10);
        robot.settings().timeoutToBeVisible(3000);
        robot.settings().timeoutToFindPopup(2000);

        // Launch the application
        ApplicationLauncher.application(App.class).start();

        // Find the application window
        window = WindowFinder.findFrame(JFrame.class).using(robot);
        robot.waitForIdle();
    }

    @AfterEach
    protected void tearDown() {
        // Clean up window (closes and disposes)
        if (window != null) {
            window.cleanUp();
        }
        // Clean up robot (releases resources)
        if (robot != null) {
            robot.cleanUp();
        }
    }

    // ========================================
    // ITEM CLASS TESTS
    // ========================================
    // These tests verify the Item class has required fields and methods.
    // Uses Java Reflection to check for getters/setters.

    /**
     * Test: Item class must have price field with getter and setter.
     *
     * APPROACH:
     * 1. Create an Item instance
     * 2. Use reflection to find getPrice() and setPrice(double) methods
     * 3. Call setPrice() with a value
     * 4. Call getPrice() and verify the value was set correctly
     */
    @Test
    void item_has_price_field_with_getter_and_setter() {
        try {
            Item item = new Item("Test", "Description", 0.0, 1);

            // Check if price field exists via setter/getter
            Method setPrice = Item.class.getMethod("setPrice", double.class);
            Method getPrice = Item.class.getMethod("getPrice");

            setPrice.invoke(item, 99.99);
            Object price = getPrice.invoke(item);

            assertEquals(99.99, (Double) price, 0.001, "Item should have a working price field");
        } catch (NoSuchMethodException e) {
            fail("Item class must have getPrice() and setPrice(double) methods");
        } catch (Exception e) {
            fail("Error testing price field: " + e.getMessage());
        }
    }

    /**
     * Test: Item class must have amount field with getter and setter.
     *
     * Similar to price test, but for integer amount field.
     */
    @Test
    void item_has_amount_field_with_getter_and_setter() {
        try {
            Item item = new Item("Test", "Description", 0.0, 1);

            // Check if amount field exists via setter/getter
            Method setAmount = Item.class.getMethod("setAmount", int.class);
            Method getAmount = Item.class.getMethod("getAmount");

            setAmount.invoke(item, 42);
            Object amount = getAmount.invoke(item);

            assertEquals(42, (Integer) amount, "Item should have a working amount field");
        } catch (NoSuchMethodException e) {
            fail("Item class must have getAmount() and setAmount(int) methods");
        } catch (Exception e) {
            fail("Error testing amount field: " + e.getMessage());
        }
    }

    // ========================================
    // DASHBOARDVIEW TESTS
    // ========================================
    // These tests verify the dashboard has required buttons.
    // Uses GenericTypeMatcher to find buttons by their text.

    /**
     * Test: DashboardView must have an "Add" button.
     *
     * APPROACH:
     * 1. Use window.button() with GenericTypeMatcher to find button
     * 2. Matcher checks if button text contains "add" (case-insensitive)
     * 3. If found, test passes; if not found or exception, test fails
     *
     * NOTE: GenericTypeMatcher.isMatching() is called for each JButton
     * in the component hierarchy until one matches or all are checked.
     */
    @Test
    void dashboard_has_add_button() {
        try {
            JButtonFixture addButton = window.button(new GenericTypeMatcher<JButton>(JButton.class) {
                @Override
                protected boolean isMatching(JButton button) {
                    String text = button.getText();
                    return text != null && text.toLowerCase().contains("add");
                }
            });
            assertNotNull(addButton, "DashboardView must have an Add button");
        } catch (Exception e) {
            e.printStackTrace();
            fail("DashboardView must have an 'Add' button: " + e.getMessage());
        }
    }

    /**
     * Test: DashboardView must have a "Report" button.
     * Same approach as Add button test.
     */
    @Test
    void dashboard_has_report_button() {
        try {
            JButtonFixture reportButton = window.button(new GenericTypeMatcher<JButton>(JButton.class) {
                @Override
                protected boolean isMatching(JButton button) {
                    String text = button.getText();
                    return text != null && text.toLowerCase().contains("report");
                }
            });
            assertNotNull(reportButton, "DashboardView must have a Report button");
        } catch (Exception e) {
            fail("DashboardView must have a 'Report' button");
        }
    }

    // ========================================
    // DETAILVIEW TESTS
    // ========================================
    // These tests navigate to DetailView and verify its components.
    // Navigation uses double-click on table row (simulates user behavior).

    /**
     * Test: DetailView must have Price and Amount text fields with labels.
     *
     * APPROACH:
     * 1. Navigate: Double-click first row in table to open DetailView
     * 2. Wait for UI to update (CardLayout switches views)
     * 3. Find text fields by searching for labels next to them
     * 4. The matcher checks: Is there a JLabel with "price"/"amount" text
     *    immediately before this text field in the parent container?
     *
     * WHY THIS COMPLEX MATCHING?
     * Text fields don't have identifying names, so we find them by
     * their relationship to adjacent labels in the layout.
     */
    @Test
    void detail_view_has_price_and_amount_fields() {
        // Step 1: Navigate to DetailView by double-clicking first table row
        JTableFixture table = window.table();
        table.cell(org.assertj.swing.data.TableCell.row(0).column(0)).doubleClick();

        // Step 2: Wait for navigation to complete
        robot.waitForIdle();

        try {
            // Step 3a: Find price field using label-based matching
            JTextComponentFixture priceField = window.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
                @Override
                protected boolean isMatching(JTextField textField) {
                    // Get the parent container (probably DetailView panel)
                    Component parent = textField.getParent();
                    if (parent instanceof Container) {
                        // Get all components in the parent
                        Component[] components = ((Container) parent).getComponents();

                        // Check if this textField is immediately after a "Price:" label
                        for (int i = 0; i < components.length - 1; i++) {
                            if (components[i] instanceof JLabel) {
                                JLabel label = (JLabel) components[i];
                                String text = label.getText();
                                // If label contains "price" and next component is this textField
                                if (text != null && text.toLowerCase().contains("price") &&
                                    i + 1 < components.length && components[i + 1] == textField) {
                                    return true; // Found it!
                                }
                            }
                        }
                    }
                    return false; // Not the price field
                }
            });

            // Step 3b: Find amount field using same label-based approach
            JTextComponentFixture amountField = window.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
                @Override
                protected boolean isMatching(JTextField textField) {
                    Component parent = textField.getParent();
                    if (parent instanceof Container) {
                        Component[] components = ((Container) parent).getComponents();
                        for (int i = 0; i < components.length - 1; i++) {
                            if (components[i] instanceof JLabel) {
                                JLabel label = (JLabel) components[i];
                                String text = label.getText();
                                if (text != null && text.toLowerCase().contains("amount") &&
                                    i + 1 < components.length && components[i + 1] == textField) {
                                    return true;
                                }
                            }
                        }
                    }
                    return false;
                }
            });

            assertNotNull(priceField, "DetailView must have a price field");
            assertNotNull(amountField, "DetailView must have an amount field");
        } catch (Exception e) {
            fail("DetailView must have price and amount text fields with corresponding labels");
        }
    }

    /**
     * Test: Save button must validate that name field is not empty.
     *
     * APPROACH:
     * 1. Navigate to DetailView by double-clicking a table row
     * 2. Wait for Save button to confirm DetailView is loaded
     * 3. Clear the name field (make it empty)
     * 4. Click Save using invokeLater (non-blocking) because
     *    JOptionPane.showMessageDialog is modal and would deadlock
     *    if triggered via GuiActionRunner.execute (invokeAndWait)
     * 5. Find the JOptionPane dialog and verify its message
     * 6. Close the dialog by clicking OK
     */
    @Test
    void save_button_validates_non_empty_name() {
        // Step 1: Navigate to DetailView
        JTableFixture table = window.table();
        table.cell(org.assertj.swing.data.TableCell.row(0).column(0)).doubleClick();
        robot.waitForIdle();

        // Step 2: Find Save button (proves DetailView is loaded)
        JButtonFixture saveButton = window.button(new GenericTypeMatcher<JButton>(JButton.class) {
            @Override
            protected boolean isMatching(JButton button) {
                String text = button.getText();
                return text != null && text.toLowerCase().contains("save");
            }
        });
        saveButton.requireVisible();
        Pause.pause(500);

        // Step 3: Find name field and clear it via EDT
        JTextComponentFixture nameField = window.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                Component parent = textField.getParent();
                if (parent instanceof Container) {
                    Component[] components = ((Container) parent).getComponents();
                    for (int i = 0; i < components.length - 1; i++) {
                        if (components[i] instanceof JLabel) {
                            JLabel label = (JLabel) components[i];
                            String text = label.getText();
                            if (text != null && text.toLowerCase().contains("name") &&
                                i + 1 < components.length && components[i + 1] == textField) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        });
        GuiActionRunner.execute(() -> nameField.target().setText(""));

        // Step 4: Click Save using invokeLater (non-blocking).
        // showMessageDialog() is modal and blocks the EDT in a nested event loop.
        // Using invokeLater avoids deadlocking the test thread.
        SwingUtilities.invokeLater(() -> saveButton.target().doClick());

        // Step 5: Find the JOptionPane dialog (with timeout for safety)
        JOptionPaneFixture optionPane = window.optionPane(Timeout.timeout(5000));
        Object msg = GuiActionRunner.execute(() -> optionPane.target().getMessage());
        assertTrue(msg != null && msg.toString().toLowerCase().contains("invalid"),
            "Validation error should mention 'invalid' when name is empty");

        // Step 6: Close the dialog
        optionPane.okButton().click();
    }

    /**
     * Test: Save button must validate that price is non-negative.
     *
     * Same approach as name validation: uses invokeLater for the button click
     * to avoid deadlocking with the modal JOptionPane dialog.
     * Sets price to "-10.5" and verifies validation error appears.
     */
    @Test
    void save_button_validates_non_negative_price() {
        // Navigate to DetailView
        JTableFixture table = window.table();
        table.cell(org.assertj.swing.data.TableCell.row(0).column(0)).doubleClick();
        robot.waitForIdle();

        // Find Save button (proves DetailView is loaded)
        JButtonFixture saveButton = window.button(new GenericTypeMatcher<JButton>(JButton.class) {
            @Override
            protected boolean isMatching(JButton button) {
                String text = button.getText();
                return text != null && text.toLowerCase().contains("save");
            }
        });
        saveButton.requireVisible();
        Pause.pause(500);

        // Find price field and set negative value via EDT
        JTextComponentFixture priceField = window.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                Component parent = textField.getParent();
                if (parent instanceof Container) {
                    Component[] components = ((Container) parent).getComponents();
                    for (int i = 0; i < components.length - 1; i++) {
                        if (components[i] instanceof JLabel) {
                            JLabel label = (JLabel) components[i];
                            String text = label.getText();
                            if (text != null && text.toLowerCase().contains("price") &&
                                i + 1 < components.length && components[i + 1] == textField) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        });
        GuiActionRunner.execute(() -> priceField.target().setText("-10.5"));

        // Click Save using invokeLater (non-blocking) to avoid modal dialog deadlock
        SwingUtilities.invokeLater(() -> saveButton.target().doClick());

        // Find and verify the validation error dialog
        JOptionPaneFixture optionPane = window.optionPane(Timeout.timeout(5000));
        Object msg = GuiActionRunner.execute(() -> optionPane.target().getMessage());
        assertTrue(msg != null && msg.toString().toLowerCase().contains("invalid"),
            "Validation error should mention 'invalid' when price is negative");

        // Close the dialog
        optionPane.okButton().click();
    }

    /**
     * Test: Save button must validate that amount is non-negative.
     *
     * Same approach as name/price validation: uses invokeLater for the button click
     * to avoid deadlocking with the modal JOptionPane dialog.
     * Sets amount to "-5" and verifies validation error appears.
     */
    @Test
    void save_button_validates_non_negative_amount() {
        // Navigate to DetailView
        JTableFixture table = window.table();
        table.cell(org.assertj.swing.data.TableCell.row(0).column(0)).doubleClick();
        robot.waitForIdle();

        // Find Save button (proves DetailView is loaded)
        JButtonFixture saveButton = window.button(new GenericTypeMatcher<JButton>(JButton.class) {
            @Override
            protected boolean isMatching(JButton button) {
                String text = button.getText();
                return text != null && text.toLowerCase().contains("save");
            }
        });
        saveButton.requireVisible();
        Pause.pause(500);

        // Find amount field and set negative value via EDT
        JTextComponentFixture amountField = window.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                Component parent = textField.getParent();
                if (parent instanceof Container) {
                    Component[] components = ((Container) parent).getComponents();
                    for (int i = 0; i < components.length - 1; i++) {
                        if (components[i] instanceof JLabel) {
                            JLabel label = (JLabel) components[i];
                            String text = label.getText();
                            if (text != null && text.toLowerCase().contains("amount") &&
                                i + 1 < components.length && components[i + 1] == textField) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        });
        GuiActionRunner.execute(() -> amountField.target().setText("-5"));

        // Click Save using invokeLater (non-blocking) to avoid modal dialog deadlock
        SwingUtilities.invokeLater(() -> saveButton.target().doClick());

        // Find and verify the validation error dialog
        JOptionPaneFixture optionPane = window.optionPane(Timeout.timeout(5000));
        Object msg = GuiActionRunner.execute(() -> optionPane.target().getMessage());
        assertTrue(msg != null && msg.toString().toLowerCase().contains("invalid"),
            "Validation error should mention 'invalid' when amount is negative");

        // Close the dialog
        optionPane.okButton().click();
    }

    // ========================================
    // REPORTVIEW TESTS
    // ========================================
    // These tests verify the Report button functionality and ReportView display.

    /**
     * Test: Report button must open ReportView showing "Total Amount" label.
     *
     * APPROACH:
     * 1. Find and click Report button on dashboard
     * 2. Wait for view transition
     * 3. Search for a label containing both "total" and "amount"
     * 4. If found, ReportView is displayed correctly
     */
    @Test
    void report_button_opens_report_view() {
        // Step 1: Find and click Report button
        JButtonFixture reportButton = window.button(new GenericTypeMatcher<JButton>(JButton.class) {
            @Override
            protected boolean isMatching(JButton button) {
                String text = button.getText();
                return text != null && text.toLowerCase().contains("report");
            }
        });

        reportButton.click();
        robot.waitForIdle();

        // Step 2: Verify "Total Amount" label exists in ReportView
        try {
            JLabel totalAmountLabel = window.label(new GenericTypeMatcher<JLabel>(JLabel.class) {
                @Override
                protected boolean isMatching(JLabel label) {
                    String text = label.getText();
                    return text != null && text.toLowerCase().contains("total") &&
                           text.toLowerCase().contains("amount");
                }
            }).target();

            assertNotNull(totalAmountLabel, "ReportView must display 'Total Amount' label");
        } catch (Exception e) {
            fail("ReportView must have a 'Total Amount' label");
        }
    }

    /**
     * Test: ReportView must display "Total Price" label.
     * Similar to Total Amount test.
     */
    @Test
    void report_view_shows_total_price() {
        // Navigate to ReportView
        JButtonFixture reportButton = window.button(new GenericTypeMatcher<JButton>(JButton.class) {
            @Override
            protected boolean isMatching(JButton button) {
                String text = button.getText();
                return text != null && text.toLowerCase().contains("report");
            }
        });

        reportButton.click();
        robot.waitForIdle();

        // Check for Total Price label
        try {
            JLabel totalPriceLabel = window.label(new GenericTypeMatcher<JLabel>(JLabel.class) {
                @Override
                protected boolean isMatching(JLabel label) {
                    String text = label.getText();
                    return text != null && text.toLowerCase().contains("total") &&
                           text.toLowerCase().contains("price");
                }
            }).target();

            assertNotNull(totalPriceLabel, "ReportView must display 'Total Price' label");
        } catch (Exception e) {
            fail("ReportView must have a 'Total Price' label");
        }
    }

    /**
     * Test: ReportView must have OK button that returns to dashboard.
     *
     * APPROACH:
     * 1. Navigate to ReportView
     * 2. Find OK button
     * 3. Click OK button
     * 4. Verify we're back at dashboard by finding the table
     */
    @Test
    void report_view_has_ok_button() {
        // Navigate to ReportView
        JButtonFixture reportButton = window.button(new GenericTypeMatcher<JButton>(JButton.class) {
            @Override
            protected boolean isMatching(JButton button) {
                String text = button.getText();
                return text != null && text.toLowerCase().contains("report");
            }
        });

        reportButton.click();
        robot.waitForIdle();
        Pause.pause(500); // Wait for view transition

        // Find and test OK button
        // The button might say "OK", "Ok", "Close", or "Back"
        JButtonFixture okButton = window.button(new GenericTypeMatcher<JButton>(JButton.class) {
            @Override
            protected boolean isMatching(JButton button) {
                String text = button.getText();
                if (text == null) return false;
                String lowerText = text.toLowerCase();
                return lowerText.equals("ok") || lowerText.equals("close") || lowerText.equals("back");
            }
        });

        assertNotNull(okButton, "ReportView must have an 'OK' button");

        // Click OK to return to dashboard - use EDT to trigger button click
        GuiActionRunner.execute(() -> okButton.target().doClick());
        robot.waitForIdle();
        Pause.pause(300); // Wait for view transition back

        // Verify we returned to dashboard by finding the table
        // (table only exists in DashboardView)
        JTableFixture table = window.table();
        assertNotNull(table, "OK button should return to DashboardView");
    }

    /**
     * Test: ReportView must display the correct total amount.
     *
     * The default ItemModel has:
     *   Laptop  (amount=1)
     *   Coffee  (amount=3)
     * Expected total amount = 1 + 3 = 4
     *
     * APPROACH:
     * 1. Click the Report button to navigate to ReportView
     * 2. Find the "Total Amount" header label in the GridLayout
     * 3. Get its sibling label (the next component) which holds the value
     * 4. Assert it equals the expected sum
     */
    @Test
    void report_view_shows_correct_total_amount() {
        // Navigate to ReportView by clicking Report button via EDT
        JButtonFixture reportButton = window.button(new GenericTypeMatcher<JButton>(JButton.class) {
            @Override
            protected boolean isMatching(JButton button) {
                String text = button.getText();
                return text != null && text.toLowerCase().contains("report");
            }
        });
        GuiActionRunner.execute(() -> reportButton.target().doClick());
        robot.waitForIdle();
        Pause.pause(300);

        // ReportView GridLayout(2,2): [JLabel("Total Amount"), amountField, JLabel("Total Price"), priceField]
        // Find the value label that sits right after the "Total Amount" header label
        String amountText = GuiActionRunner.execute(() -> {
            for (Component c : getAllComponents(((JFrame) window.target()).getContentPane())) {
                if (c instanceof Container container) {
                    Component[] children = container.getComponents();
                    for (int i = 0; i < children.length - 1; i++) {
                        if (children[i] instanceof JLabel header &&
                            header.getText() != null &&
                            header.getText().toLowerCase().contains("total") &&
                            header.getText().toLowerCase().contains("amount") &&
                            children[i + 1] instanceof JLabel valueLabel) {
                            return valueLabel.getText();
                        }
                    }
                }
            }
            return null;
        });

        assertNotNull(amountText, "ReportView must display a value next to 'Total Amount'");
        assertEquals("4", amountText.strip(),
            "Total amount should be 4 (Laptop:1 + Coffee:3)");
    }

    /**
     * Test: ReportView must display the correct total price.
     *
     * The default ItemModel has:
     *   Laptop  (price=1000.0, amount=1) → contributes 1*1000.0 = 1000.0
     *   Coffee  (price=10.0, amount=3)   → contributes 3*10.0   = 30.0
     * Expected total price = 1030.0
     *
     * APPROACH:
     * 1. Click the Report button to navigate to ReportView
     * 2. Find the "Total Price" header label in the GridLayout
     * 3. Get its sibling label (the next component) which holds the value
     * 4. Assert it equals the expected sum
     */
    @Test
    void report_view_shows_correct_total_price() {
        // Navigate to ReportView by clicking Report button via EDT
        JButtonFixture reportButton = window.button(new GenericTypeMatcher<JButton>(JButton.class) {
            @Override
            protected boolean isMatching(JButton button) {
                String text = button.getText();
                return text != null && text.toLowerCase().contains("report");
            }
        });
        GuiActionRunner.execute(() -> reportButton.target().doClick());
        robot.waitForIdle();
        Pause.pause(300);

        // ReportView GridLayout(2,2): [JLabel("Total Amount"), amountField, JLabel("Total Price"), priceField]
        // Find the value label that sits right after the "Total Price" header label
        String priceText = GuiActionRunner.execute(() -> {
            for (Component c : getAllComponents(((JFrame) window.target()).getContentPane())) {
                if (c instanceof Container container) {
                    Component[] children = container.getComponents();
                    for (int i = 0; i < children.length - 1; i++) {
                        if (children[i] instanceof JLabel header &&
                            header.getText() != null &&
                            header.getText().toLowerCase().contains("total") &&
                            header.getText().toLowerCase().contains("price") &&
                            children[i + 1] instanceof JLabel valueLabel) {
                            return valueLabel.getText();
                        }
                    }
                }
            }
            return null;
        });

        assertNotNull(priceText, "ReportView must display a value next to 'Total Price'");
        assertEquals("1030.0", priceText.strip(),
            "Total price should be 1030.0 ((1*1000.0) + (3*10.0))");
    }

    /**
     * Helper: Recursively collect all components from a container.
     */
    private java.util.List<Component> getAllComponents(Container container) {
        java.util.List<Component> result = new java.util.ArrayList<>();
        for (Component c : container.getComponents()) {
            result.add(c);
            if (c instanceof Container sub) {
                result.addAll(getAllComponents(sub));
            }
        }
        return result;
    }

    /**
     * Test: Application must use FlatLaf Look and Feel.
     *
     * FlatLaf is a modern Swing Look and Feel library.
     * This test checks UIManager to see if FlatLaf is active.
     */
    @Test
    void application_uses_flatlaf_look_and_feel() {
        String lafClassName = UIManager.getLookAndFeel().getClass().getName();
        assertTrue(lafClassName.contains("FlatDarkLaf") || lafClassName.contains("Flat"),
            "Application must use FlatLaf Look and Feel (current: " + lafClassName + ")");
    }
}
