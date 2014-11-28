package com.enochc.software648.bikeshop;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class WarehouseTest {
    private Warehouse warehouse;

    @Before
    public void setUp() {
        this.warehouse = new Warehouse();
    }

    @Test
    public void testGetBikes(){
        ArrayList<BikeData> bikeList = warehouse.readBikes(0, 5);

        System.out.println(bikeList);

    }
}