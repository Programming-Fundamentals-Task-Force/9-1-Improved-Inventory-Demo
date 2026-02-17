package com.lab;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        // Start the App on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            ItemModel model = new ItemModel();
            MainController controller = new MainController(model);
            controller.start();
        });
    }
}