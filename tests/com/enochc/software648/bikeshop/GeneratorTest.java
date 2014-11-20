package com.enochc.software648.bikeshop;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.enochc.software648.bikeshop.Generator;


import java.util.ArrayList;

public class GeneratorTest {
    Generator generator;

    @Before
    public void setUp() throws Exception {
        generator = new Generator();
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