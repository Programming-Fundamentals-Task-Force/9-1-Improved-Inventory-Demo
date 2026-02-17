package com.lab;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;

public class MainController {
    private ItemModel model;
    private JFrame frame;
    private JPanel rootPanel;
    private DashboardView dashboardView;
    private DetailView detailView;

    public MainController(ItemModel model) {
        this.model = model;

        rootPanel = new JPanel(new CardLayout());

        dashboardView = new DashboardView(this, model);
        detailView = new DetailView(this);

        rootPanel.add(dashboardView, "DASHBOARD");
        rootPanel.add(detailView, "DETAILS");

        frame = new JFrame("Inventory Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(rootPanel);
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);
    }

    public void start() {
        frame.setVisible(true);
    }

    public void showDetailView(Item item) {
        detailView.populateData(item);
        CardLayout layout = (CardLayout)rootPanel.getLayout();
        layout.show(rootPanel, "DETAILS");
    }

    public void saveItem(Item itemToUpdate, String newName, String newDesc) {
        // 1. Controller updates the model
        itemToUpdate.setName(newName);
        itemToUpdate.setDescription(newDesc);

        // 2. Controller coordinates the UI updates
        dashboardView.refreshTable();
        returnToDashboard();
    }

    public void deleteItem(Item itemToDelete) {
        // 1. Controller handles the business logic/model update
        model.deleteItem(itemToDelete);

        // 2. Controller tells the view to refresh
        dashboardView.refreshTable();
    }

    public void returnToDashboard() {
        CardLayout layout = (CardLayout)rootPanel.getLayout();
        layout.show(rootPanel, "DASHBOARD");
    }
}
