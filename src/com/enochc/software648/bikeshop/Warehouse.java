package com.enochc.software648.bikeshop;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
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
        client = new AmazonDynamoDBClient(new ClasspathPropertiesFileCredentialsProvider());
        client.setRegion(Region.getRegion(Regions.US_WEST_2));

    }

    /**
     * Return bikes with model number between start (inclusive) and end (exclusive)
     *
     * @param start
     * @param end
     * @return
     */
    public ArrayList<BikeData> getBikes(int start, int end) {
        ArrayList<BikeData> list = new ArrayList<BikeData>();

        for (int i = start; i < end; i++) {
            String modelNumber = String.valueOf(i);
            BikeData bikeData = this.getBike(modelNumber);
            if (bikeData != null) {
                list.add(bikeData);
            }
        }

        return list;
    }


    /**
     * Returns bike matching modelNumber. Null if not found.
     *
     * @param modelNumber
     * @return
     */
    public BikeData getBike(String modelNumber) {
        Condition hashKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.EQ)
                .withAttributeValueList(new AttributeValue().withS(modelNumber));

        Map<String, Condition> keyConditions = new HashMap<String, Condition>();
        keyConditions.put("ModelNumber", hashKeyCondition);

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(TABLE_NAME)
                .withKeyConditions(keyConditions);

        QueryResult result = client.query(queryRequest);
        if (result.getItems().isEmpty()) {
            return null;
        }
        Map<String, AttributeValue> item = result.getItems().get(0);
        if (item == null) {
            return null;
        }
        AttributeValue bikeNameAttribute = item.get("Name");
        String bikeName = (bikeNameAttribute == null ? "" : bikeNameAttribute.getS());

        AttributeValue descriptionAttribute = item.get("Description");
        String description = (descriptionAttribute == null ? "" : descriptionAttribute.getS());

        AttributeValue priceAttribute = item.get("Price");
        int price = (priceAttribute == null ? -1 : Integer.valueOf(priceAttribute.getN()));

        AttributeValue quantityAttribute = item.get("Quantity");
        int quantity = (quantityAttribute == null ? -1 : Integer.valueOf(quantityAttribute.getN()));

        BikeData bike = new BikeData(modelNumber, bikeName, description, price, quantity);

        return bike;
    }

    public String orderBike(String modelNumber, int quantity, String orderID) {
        BikeData bikeData = this.getBike(modelNumber);

        // check if model number is valid
        if (bikeData == null) {
            return "not found";
        }

        // check if sufficient stock
        if (bikeData.getQuantity() < quantity) {
            return "insufficient";
        }


        // update inventory
        try {
            HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
            key.put("ModelNumber", new AttributeValue().withS(modelNumber));

            Map<String, AttributeValue> expressionAttributeValues = new HashMap<String, AttributeValue>();
            expressionAttributeValues.put(":val1", new AttributeValue().withN(String.valueOf(quantity)));

            ReturnValue returnValues = ReturnValue.NONE;

            UpdateItemRequest updateItemRequest = new UpdateItemRequest()
                    .withTableName(TABLE_NAME)
                    .withKey(key)
                    .withUpdateExpression("set Quantity = Quantity - :val1")
                    .withConditionExpression("Quantity >= :val1")
                    .withExpressionAttributeValues(expressionAttributeValues)
                    .withReturnValues(returnValues);

            UpdateItemResult result = client.updateItem(updateItemRequest);

            // Order successfully placed, otherwise errors would be thrown
            //TODO: do something to complete the order after random delay

            return String.valueOf(bikeData.getPrice());
        } catch (ConditionalCheckFailedException cse) {
            // Condition not met, meaning insufficent stock.
            cse.printStackTrace();
            return "insufficient";
        } catch (AmazonServiceException ase) {
            ase.printStackTrace();
            return "not found";
        }


    }


}
