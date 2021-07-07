package com.example.mypantry.item;

import com.example.mypantry.item.Item;

public class ListItem{
    private String key;
    private Item item;

    public ListItem(String key, Item item) {
        this.key = key;
        this.item = item;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
