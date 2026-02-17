package com.lab;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import java.awt.GridLayout;

public class DetailView extends JPanel {
    private MainController controller;
    private Item currentItem;
    private JTextField nameField;
    private JTextField descField;

    public DetailView(MainController controller) {
        this.controller = controller;
        setLayout(new GridLayout(3, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        nameField = new JTextField();
        descField = new JTextField();

        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(e -> {
            if (currentItem != null) {
                // Delegate to Controller
                String newName = nameField.getText();
                String newDesc = descField.getText();
                controller.saveItem(currentItem, newName, newDesc);
            }
        });

        cancelBtn.addActionListener(e -> controller.returnToDashboard());

        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Description:"));
        add(descField);
        add(saveBtn);
        add(cancelBtn);
    }

    public void populateData(Item item) {
        this.currentItem = item;
        nameField.setText(item.getName());
        descField.setText(item.getDescription());
    }
}
