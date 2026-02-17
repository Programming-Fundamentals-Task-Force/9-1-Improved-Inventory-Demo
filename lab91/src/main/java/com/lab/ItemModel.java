package com.lab;

import java.util.ArrayList;
import java.util.List;

public class ItemModel {
    private List<Item> items = new ArrayList<>();

    public ItemModel() {
        // Add some dummy data so the students see something immediately
        items.add(new Item("Laptop", "Developer machine"));
        items.add(new Item("Coffee", "Fuel for the developer"));
    }

    public List<Item> getItems() { return items; }

    public void deleteItem(Item item) { items.remove(item); }
}
