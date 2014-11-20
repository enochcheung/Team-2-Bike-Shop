package com.enochc.software648.bikeshop;

import java.util.Date;

public class OrderData {
    private String orderID;
    private String customerId;
    private String bikeId;
    private int quantity;
    private int price;
    private Date date;

    public OrderData(String orderID, String customerId, String bikeId, int quantity, int price, Date date) {
        this.orderID = orderID;
        this.customerId = customerId;
        this.bikeId = bikeId;
        this.quantity = quantity;
        this.price = price;
        this.date = date;
    }

    public String getOrderID() {
        return orderID;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getBikeId() {
        return bikeId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "OrderData{" +
                "orderID='" + orderID + '\'' +
                ", customerId='" + customerId + '\'' +
                ", bikeId='" + bikeId + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", date=" + date +
                '}';
    }
}
