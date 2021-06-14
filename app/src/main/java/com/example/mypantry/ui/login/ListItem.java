package com.example.mypantry.ui.login;

import com.example.mypantry.dummy.DummyItem;

public class ListItem {
    private Integer key;
    private DummyItem item;

    public ListItem(Integer key,DummyItem item) {
        this.key = key;
        this.item = item;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public DummyItem getItem() {
        return item;
    }

    public void setItem(DummyItem item) {
        this.item = item;
    }


}
