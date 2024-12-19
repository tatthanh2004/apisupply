package com.example.apisupply;

public class Supply {
    private int supplyId;
    private String supplyName;
    private int price;

    public Supply() { }

    public Supply(int supplyId, String supplyName, int price) {
        this.supplyId = supplyId;
        this.supplyName = supplyName;
        this.price = price;
    }

    public int getSupplyId() {
        return supplyId;
    }

    public String getSupplyName() {
        return supplyName;
    }

    public int getPrice() {
        return price;
    }
}

