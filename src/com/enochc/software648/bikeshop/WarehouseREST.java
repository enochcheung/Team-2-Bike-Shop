package com.enochc.software648.bikeshop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.Map;

@Path("/warehouse")
public class WarehouseREST {
    private Warehouse warehouse;
    private void init() {
        if (warehouse == null) {
            warehouse = new Warehouse();
        }

    }

    @GET
    @Path("/bike")
    @Produces("application/json")
    public String getBikes(@QueryParam("start") String start,@QueryParam("end") String end) {
        init();

        int startNum=0;
        int endNum=0;
        try {
            startNum = Integer.parseInt(start);
            endNum = Integer.parseInt(end);
        } catch (NumberFormatException e) {
            throw new WebApplicationException(404);
        }

        ArrayList<BikeData> bikes= warehouse.getBikes(startNum,endNum);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS,true);


        try {
            return mapper.writeValueAsString(bikes);
        } catch (JsonProcessingException e) {
            throw new WebApplicationException(404);
        }

    }

    @GET
    @Path("/bike/{modelNumber}")
    @Produces("application/json")
    public String getBikes(@PathParam("modelNumber") String modelNumber) {
        init();

        BikeData bike = warehouse.getBike(modelNumber);
        if (bike==null) {
            throw new WebApplicationException(404);
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS,true);

        try {
            return mapper.writeValueAsString(bike);
        } catch (JsonProcessingException e) {
            throw new WebApplicationException(404);
        }

    }

    @GET
    @Path("/order")
    @Produces("text/html")
    public String orderBikes(@QueryParam("orderID") String orderID, @QueryParam("modelNumber") String modelNumber,
                           @QueryParam("quantity") String quantity) {
        init();

        if (quantity==null || orderID==null || modelNumber == null) {
            throw new WebApplicationException(404);
        }

        int quantityInt = 0;
        try {
            quantityInt = Integer.parseInt(quantity);
        } catch (NumberFormatException e) {
            throw new WebApplicationException(404);
        }

        if (quantityInt <= 0) {
            throw new WebApplicationException(404);
        }

        return warehouse.orderBike(modelNumber,quantityInt,orderID);
    }

    @GET
    @Path("/hello")
    @Produces("text/html")
    public String hello() {
        StringBuilder sb = new StringBuilder();
        Map<String, String> env = System.getenv();
        for (Map.Entry<String,String> entry : env.entrySet()) {
            sb.append(entry.getKey()+" : "+entry.getValue()+"\n");
        }
        return sb.toString();
    }


}
