package com.example.mypantry.item;

public class Item {

    private final String productID;
    private final String name;
    private final String details;
    private int quantity;
    private final String barcode;

    public Item(String id, String content, String details, int quantity, String barcode) {
        this.productID= id;
        this.name = content;
        this.details = details;
        this.quantity = quantity;
        this.barcode = barcode;
    }

    public String getProductID() {
        return productID;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getBarcode() {
        return barcode;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", details='" + details + '\'' +
                ", quantity=" + quantity +
                ", barcode='" + barcode + '\'' +
                '}';
    }

    public Item sub(){quantity--;return this;}

    public Item add(){quantity++;return this;}
}