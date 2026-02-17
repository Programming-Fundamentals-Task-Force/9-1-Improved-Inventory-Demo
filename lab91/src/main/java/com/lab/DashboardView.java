package com.lab;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DashboardView extends JPanel {
    private MainController controller;
    private ItemModel model;
    private JTable table;
    private DefaultTableModel tableModel;

    public DashboardView(MainController controller, ItemModel model) {
        this.controller = controller;
        this.model = model;
        setLayout(new BorderLayout());

        String[] columns = { "Name", "Description" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        refreshTable();

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        Item selectedItem = model.getItems().get(selectedRow);
                        controller.showDetailView(selectedItem);
                    }
                }
            }
        });

        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                Item itemToDelete = model.getItems().get(selectedRow);
                // Delegate to Controller
                controller.deleteItem(itemToDelete);
            } else {
                JOptionPane.showMessageDialog(this, "Please select an item to delete.");
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(deleteBtn, BorderLayout.SOUTH);
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        for (Item item : model.getItems()) {
            tableModel.addRow(new Object[] { item.getName(), item.getDescription() });
        }
    }
}
