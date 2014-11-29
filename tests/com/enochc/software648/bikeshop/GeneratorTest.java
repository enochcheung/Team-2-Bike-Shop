package com.enochc.software648.bikeshop;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import com.enochc.software648.bikeshop.Generator;


import java.io.IOException;
import java.util.ArrayList;

public class GeneratorTest {
    private Generator generator;

    @Before
    public void setUp() throws Exception {
        generator = new Generator();
    }


    @Test
    public void testBikeSerialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String jsonString = "{\"modelNumber\":\"0\",\"name\":\"Bike0\",\"description\":\"0\",\"price\":1492,\"quantity\":1}";
        BikeData bike = mapper.readValue(jsonString,BikeData.class);
        System.out.println(bike);
    }


    @Test
    public void testGenerate() throws Exception{
        ArrayList<CustomerData> customerList = generator.generateCustomers(10);
        ArrayList<BikeData> bikeList = generator.generateBikes(10);
        ArrayList<OrderData> orderList = generator.generateOrders(20);

        System.out.println(customerList);
        System.out.println(bikeList);
        System.out.println(orderList);
    }



}