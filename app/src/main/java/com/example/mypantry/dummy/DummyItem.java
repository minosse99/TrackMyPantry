package com.example.mypantry.dummy;

public class DummyItem {

    public final String content;
    public final String details;

    public DummyItem(String content, String details) {
        this.content = content;
        this.details = details;
    }

    @Override
    public String toString() {
        return content;
    }
}