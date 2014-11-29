package com.enochc.software648.bikeshop;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import com.enochc.software648.bikeshop.Generator;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        String jsonString2 = "[{\"modelNumber\":\"0\",\"name\":\"Bike0\",\"description\":\"0\",\"price\":1492,\"quantity\":1},{\"modelNumber\":\"1\",\"name\":\"Bike1\",\"description\":\"1\",\"price\":214,\"quantity\":4},{\"modelNumber\":\"2\",\"name\":\"Bike2\",\"description\":\"2\",\"price\":1942,\"quantity\":4},{\"modelNumber\":\"3\",\"name\":\"Bike3\",\"description\":\"3\",\"price\":1150,\"quantity\":5},{\"modelNumber\":\"4\",\"name\":\"Bike4\",\"description\":\"4\",\"price\":533,\"quantity\":3}]";
        List<BikeData> bikeList = mapper.readValue(jsonString2, new TypeReference<List<BikeData>>(){});


        System.out.println(bikeList);
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