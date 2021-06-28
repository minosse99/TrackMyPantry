package com.example.mypantry.ui.login;

import com.example.mypantry.dummy.DummyItem;

public class ListItem {
    private String key;
    private DummyItem item;

    public ListItem(String key, DummyItem item) {
        this.key = key;
        this.item = item;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public DummyItem getItem() {
        return item;
    }

    public void setItem(DummyItem item) {
        this.item = item;
    }


}
