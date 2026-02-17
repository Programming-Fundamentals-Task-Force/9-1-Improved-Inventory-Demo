package org.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.Robot;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JTableFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;

import javax.swing.*;
import java.awt.Component;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.lab.*;

/**
 * Test suite for Assignment 9-1: Improved Inventory Demo
 * Tests the implementation of price/amount fields, validation, ReportView, and UI components.
 */
class AppTest extends AssertJSwingJUnitTestCase {
    private FrameFixture window;
    private ItemModel model;
    private MainController controller;

    @BeforeAll
    static void setUpOnce() {
        // Set headless mode for Swing testing
        System.setProperty("java.awt.headless", "false");
    }

    @Override
    protected void onSetUp() {
        // Create the application on EDT
        JFrame frame = GuiActionRunner.execute(() -> {
            model = new ItemModel();
            controller = new MainController(model);
            return getFrame(controller);
        });
        window = new FrameFixture(robot(), frame);
        window.show();
    }

    @Override
    protected void onTearDown() {
        window.cleanUp();
    }

    /**
     * Helper method to get the JFrame from MainController using reflection
     */
    private JFrame getFrame(MainController controller) {
        try {
            Field frameField = MainController.class.getDeclaredField("frame");
            frameField.setAccessible(true);
            return (JFrame) frameField.get(controller);
        } catch (Exception e) {
            fail("Could not access frame field in MainController: " + e.getMessage());
            return null;
        }
    }

    // ===== Item Class Tests =====

    @Test
    void item_has_price_field_with_getter_and_setter() {
        try {
            Item item = new Item("Test", "Description");

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

    @Test
    void item_has_amount_field_with_getter_and_setter() {
        try {
            Item item = new Item("Test", "Description");

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

    // ===== DashboardView Tests =====

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
            fail("DashboardView must have an 'Add' button");
        }
    }

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

    // ===== DetailView Tests =====

    @Test
    void detail_view_has_price_and_amount_fields() {
        // Navigate to DetailView by double-clicking an item
        JTableFixture table = window.table();
        table.cell(org.assertj.swing.data.TableCell.row(0).column(0)).doubleClick();

        robot().waitForIdle();

        try {
            // Try to find price field
            JTextComponentFixture priceField = window.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
                @Override
                protected boolean isMatching(JTextField textField) {
                    Component parent = textField.getParent();
                    if (parent != null) {
                        Component[] components = parent.getComponents();
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

            // Try to find amount field
            JTextComponentFixture amountField = window.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
                @Override
                protected boolean isMatching(JTextField textField) {
                    Component parent = textField.getParent();
                    if (parent != null) {
                        Component[] components = parent.getComponents();
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

    @Test
    void save_button_validates_non_empty_name() {
        // Navigate to DetailView
        JTableFixture table = window.table();
        table.cell(org.assertj.swing.data.TableCell.row(0).column(0)).doubleClick();

        robot().waitForIdle();

        // Find name field and clear it
        JTextComponentFixture nameField = window.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                Component parent = textField.getParent();
                if (parent != null) {
                    Component[] components = parent.getComponents();
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

        nameField.deleteText();

        // Click Save button
        JButtonFixture saveButton = window.button(new GenericTypeMatcher<JButton>(JButton.class) {
            @Override
            protected boolean isMatching(JButton button) {
                String text = button.getText();
                return text != null && text.toLowerCase().contains("save");
            }
        });

        saveButton.click();
        robot().waitForIdle();

        // Check for error dialog
        try {
            DialogFixture dialog = window.dialog();
            String message = dialog.label().text();
            assertTrue(message.toLowerCase().contains("invalid"),
                "Validation error should mention 'invalid' when name is empty");
            dialog.button().click(); // Close dialog
        } catch (Exception e) {
            fail("Save should show a validation error dialog when name is empty");
        }
    }

    @Test
    void save_button_validates_non_negative_price() {
        // Navigate to DetailView
        JTableFixture table = window.table();
        table.cell(org.assertj.swing.data.TableCell.row(0).column(0)).doubleClick();

        robot().waitForIdle();

        // Find price field and set negative value
        JTextComponentFixture priceField = window.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                Component parent = textField.getParent();
                if (parent != null) {
                    Component[] components = parent.getComponents();
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

        priceField.deleteText();
        priceField.enterText("-10.5");

        // Click Save button
        JButtonFixture saveButton = window.button(new GenericTypeMatcher<JButton>(JButton.class) {
            @Override
            protected boolean isMatching(JButton button) {
                String text = button.getText();
                return text != null && text.toLowerCase().contains("save");
            }
        });

        saveButton.click();
        robot().waitForIdle();

        // Check for error dialog
        try {
            DialogFixture dialog = window.dialog();
            String message = dialog.label().text();
            assertTrue(message.toLowerCase().contains("invalid"),
                "Validation error should mention 'invalid' when price is negative");
            dialog.button().click(); // Close dialog
        } catch (Exception e) {
            fail("Save should show a validation error dialog when price is negative");
        }
    }

    @Test
    void save_button_validates_non_negative_amount() {
        // Navigate to DetailView
        JTableFixture table = window.table();
        table.cell(org.assertj.swing.data.TableCell.row(0).column(0)).doubleClick();

        robot().waitForIdle();

        // Find amount field and set negative value
        JTextComponentFixture amountField = window.textBox(new GenericTypeMatcher<JTextField>(JTextField.class) {
            @Override
            protected boolean isMatching(JTextField textField) {
                Component parent = textField.getParent();
                if (parent != null) {
                    Component[] components = parent.getComponents();
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

        amountField.deleteText();
        amountField.enterText("-5");

        // Click Save button
        JButtonFixture saveButton = window.button(new GenericTypeMatcher<JButton>(JButton.class) {
            @Override
            protected boolean isMatching(JButton button) {
                String text = button.getText();
                return text != null && text.toLowerCase().contains("save");
            }
        });

        saveButton.click();
        robot().waitForIdle();

        // Check for error dialog
        try {
            DialogFixture dialog = window.dialog();
            String message = dialog.label().text();
            assertTrue(message.toLowerCase().contains("invalid"),
                "Validation error should mention 'invalid' when amount is negative");
            dialog.button().click(); // Close dialog
        } catch (Exception e) {
            fail("Save should show a validation error dialog when amount is negative");
        }
    }

    // ===== ReportView Tests =====

    @Test
    void report_button_opens_report_view() {
        // Click Report button
        JButtonFixture reportButton = window.button(new GenericTypeMatcher<JButton>(JButton.class) {
            @Override
            protected boolean isMatching(JButton button) {
                String text = button.getText();
                return text != null && text.toLowerCase().contains("report");
            }
        });

        reportButton.click();
        robot().waitForIdle();

        // Check for Total Amount label
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

    @Test
    void report_view_shows_total_price() {
        // Click Report button
        JButtonFixture reportButton = window.button(new GenericTypeMatcher<JButton>(JButton.class) {
            @Override
            protected boolean isMatching(JButton button) {
                String text = button.getText();
                return text != null && text.toLowerCase().contains("report");
            }
        });

        reportButton.click();
        robot().waitForIdle();

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

    @Test
    void report_view_has_ok_button() {
        // Click Report button
        JButtonFixture reportButton = window.button(new GenericTypeMatcher<JButton>(JButton.class) {
            @Override
            protected boolean isMatching(JButton button) {
                String text = button.getText();
                return text != null && text.toLowerCase().contains("report");
            }
        });

        reportButton.click();
        robot().waitForIdle();

        // Check for OK button
        try {
            JButtonFixture okButton = window.button(new GenericTypeMatcher<JButton>(JButton.class) {
                @Override
                protected boolean isMatching(JButton button) {
                    String text = button.getText();
                    return text != null && text.equalsIgnoreCase("ok");
                }
            });

            assertNotNull(okButton, "ReportView must have an 'OK' button");

            // Test that OK returns to dashboard
            okButton.click();
            robot().waitForIdle();

            // Verify we're back to dashboard by checking for table
            JTableFixture table = window.table();
            assertNotNull(table, "OK button should return to DashboardView");
        } catch (Exception e) {
            fail("ReportView must have an 'OK' button that returns to DashboardView");
        }
    }

    // ===== FlatLaf Look and Feel Test =====

    @Test
    void application_uses_flatlaf_look_and_feel() {
        String lafClassName = UIManager.getLookAndFeel().getClass().getName();
        assertTrue(lafClassName.contains("FlatLaf") || lafClassName.contains("Flat"),
            "Application must use FlatLaf Look and Feel (current: " + lafClassName + ")");
    }
}