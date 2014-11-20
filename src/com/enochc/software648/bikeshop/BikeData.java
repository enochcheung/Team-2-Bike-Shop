package com.enochc.software648.bikeshop;

public class BikeData {
    private String modelNumber;
    private String name;
    private String description;
    private int price;
    private int quantity;

    public BikeData(String modelNumber, String name, String description, int price, int quantity) {
        this.modelNumber = modelNumber;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "BikeData{" +
                "modelNumber='" + modelNumber + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
