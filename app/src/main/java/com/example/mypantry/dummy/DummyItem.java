package com.example.mypantry.dummy;

public class DummyItem {

    public String productID;
    public final String name;
    public final String details;
    public int quantity;
    public final String barcode;

    public DummyItem(String id,String content, String details, int quantity,String barcode) {
        this.productID= id;
        this.name = content;
        this.details = details;
        this.quantity = quantity;
        this.barcode = barcode;
    }

    @Override
    public String toString() {
        return name;
    }



    public DummyItem sub(){quantity--;return this;}

    public DummyItem add(){quantity++;return this;}
}