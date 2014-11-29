package com.enochc.software648.bikeshop;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BikeData {
    private String modelNumber;
    private String name;
    private String description;
    private int price;
    private int quantity;

    @JsonCreator
    public BikeData(@JsonProperty("modelNumber") String modelNumber,@JsonProperty("name") String name,
                    @JsonProperty("description")String description,@JsonProperty("price") int price,
                    @JsonProperty("quantity")int quantity) {
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
