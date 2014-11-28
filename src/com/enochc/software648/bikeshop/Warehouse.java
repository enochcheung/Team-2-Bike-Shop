package com.enochc.software648.bikeshop;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Warehouse {
    public static final String TABLE_NAME = "Bikes";
    private final AmazonDynamoDBClient client;

    public Warehouse() {
        client = new AmazonDynamoDBClient(new ProfileCredentialsProvider());
        client.setRegion(Region.getRegion(Regions.US_WEST_2));

    }

    /**
     * Return bikes with model number between start (inclusive) and end (exclusive)
     *
     * @param start
     * @param end
     * @return
     */
    public ArrayList<BikeData> readBikes(int start, int end) {
        ArrayList<BikeData> list = new ArrayList<BikeData>();

        for (int i = start; i < end; i++) {
            String modelNumber = String.valueOf(i);


            Condition hashKeyCondition = new Condition()
                    .withComparisonOperator(ComparisonOperator.EQ)
                    .withAttributeValueList(new AttributeValue().withS(modelNumber));

            Map<String, Condition> keyConditions = new HashMap<String, Condition>();
            keyConditions.put("ModelNumber", hashKeyCondition);

            QueryRequest queryRequest = new QueryRequest()
                    .withTableName(TABLE_NAME)
                    .withKeyConditions(keyConditions);

            QueryResult result = client.query(queryRequest);
            for (Map<String, AttributeValue> item : result.getItems()) {
                AttributeValue bikeNameAttribute = item.get("Name");
                String bikeName = (bikeNameAttribute == null ? "" : bikeNameAttribute.getS());

                AttributeValue descriptionAttribute = item.get("Description");
                String description = (descriptionAttribute == null ? "" : descriptionAttribute.getS());

                AttributeValue priceAttribute = item.get("Price");
                int price = (priceAttribute == null ? -1 : Integer.valueOf(priceAttribute.getN()));

                AttributeValue quantityAttribute = item.get("Quantity");
                int quantity = (quantityAttribute == null ? -1 : Integer.valueOf(quantityAttribute.getN()));

                BikeData bike = new BikeData(modelNumber,bikeName,description,price,quantity);

                list.add(bike);
            }
        }

        return list;
    }



}
