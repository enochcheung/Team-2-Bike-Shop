package com.enochc.software648.bikeshop;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Generator {
    public static final String DICT_TXT = "dict.txt";
    private final ArrayList<String> dict;
    private static final String[] states = {"AL","AK","AZ","AR","CA","CO","CT","DE","FL","GA","HI","ID","IL",
            "IN","IA","KS","KY","LA","ME","MD","MA","MI","MN","MS","MO","MT","NE","NV","NH","NJ","NM","NY",
            "NC","ND","OH","OK","OR","PA","RI","SC","SD","TN","TX","UT","VT","VA","WA","WV","WI","WY"};
    private static final long START_DATE = 1230786000000L;    // milliseconds corresponding to 2009/1/1
    private static final long DATE_INTERVAL = 189216000000L;  // milliseconds corresponding to 6 years
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private static final int NUM_CUSTOMERS = 100000;
    private static final int NUM_ORDERS = 10000000;
    private static final int NUM_BIKES = 100000;


    private final int dictLength;
    private Random random = new Random();

    private static int customerCounter=0;
    private static int bikeCounter=0;
    private static int orderCounter=0;

    public Generator() {

        // Load Dictionary
        dict = new ArrayList<String>();
        File file = new File(DICT_TXT);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line=reader.readLine())!=null) {
                dict.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader!= null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        dictLength = dict.size();
    }

    private String randomWord() {
        return dict.get(random.nextInt(dictLength));
    }

    private String randomWordCap() {
        String word = randomWord();
        char c = Character.toUpperCase(word.charAt(0));
        return c+word.substring(1);
    }

    private String randomState() {
        return states[random.nextInt(states.length)];
    }


    private CustomerData randomCustomer(int n) {
        String firstName = randomWordCap();
        String lastName = randomWordCap();
        String street = randomWordCap()+" Street";
        String city = randomWordCap()+" City";
        String state = randomState();
        String zip = (new Integer(random.nextInt(99999))).toString();

        String username = String.valueOf(n);
        String password = username;

        return new CustomerData(firstName,lastName,street,city,state,zip,username,password);
    }

    private BikeData randomBike(int n) {
        String num = String.valueOf(n);

        String modelNumber = num;
        String bikeName = "Bike"+num;
        String description = num;
        int price = 1+random.nextInt(2000);
        int quantity = 1+random.nextInt(9);

        return new BikeData(modelNumber,bikeName,description,price,quantity);
    }

    private OrderData randomOrder(int n) {
        String num = String.valueOf(n);

        String orderID = num;
        String customerId = String.valueOf(random.nextInt(customerCounter));
        String bikeId = String.valueOf(random.nextInt(bikeCounter));
        int price = 1+random.nextInt(2000);
        int quantity = 1+random.nextInt(2);

        long milliseconds = START_DATE+((long) (random.nextDouble()*DATE_INTERVAL));
        String date = DATE_FORMAT.format(new Date(milliseconds));


        return new OrderData(orderID, customerId,bikeId,quantity,price,date);
    }


    /**
     * Generates a list of customers
     * @param numCustomers number of customers to generate
     */
    public ArrayList<CustomerData> generateCustomers(int numCustomers) {
        ArrayList<CustomerData> list = new ArrayList<CustomerData>(numCustomers);
        for (int i= 0; i<numCustomers; i++) {
            list.add(this.randomCustomer(customerCounter));
            customerCounter++;
        }

        return list;
    }

    /**
     * Generates a list of bikes
     * @param numBikes number of bikes to generate
     */
    public ArrayList<BikeData> generateBikes(int numBikes) {
        ArrayList<BikeData> list = new ArrayList<BikeData>(numBikes);
        for (int i= 0; i<numBikes; i++) {
            list.add(this.randomBike(bikeCounter));
            bikeCounter++;
        }

        return list;
    }

    /**
     * Generates a list of orders, with references to previously generated customerIds and bikeIds. Should only be called
     * after some orders have been generated.
     * @param numOrders number of orders to generate
     */
    public ArrayList<OrderData> generateOrders(int numOrders) {
        ArrayList<OrderData> list = new ArrayList<OrderData>(numOrders);
        for (int i= 0; i<numOrders; i++) {
            list.add(this.randomOrder(orderCounter));
            orderCounter++;
        }

        return list;
    }

    /*
    public static void main(String[] args) {
        Generator generator = new Generator();
        ArrayList<CustomerData> customerList = generator.generateCustomers(100);
        ArrayList<BikeData> bikeList = generator.generateBikes(100);
        ArrayList<OrderData> orderList = generator.generateOrders(1000);




        try {
            AmazonDynamoDBClient client = new AmazonDynamoDBClient(new ProfileCredentialsProvider());
            client.setRegion(Region.getRegion(Regions.US_WEST_2));


            String awsTableName = "Customers";

            for (CustomerData customerData : customerList) {
                Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
                item.put("FirstName", new AttributeValue().withS(customerData.getFirstName()));
                item.put("LastName", new AttributeValue().withS(customerData.getLastName()));
                item.put("Address", new AttributeValue().withS(customerData.getAddress()));
                item.put("City", new AttributeValue().withS(customerData.getCity()));
                item.put("State", new AttributeValue().withS(customerData.getState()));
                item.put("Zip", new AttributeValue().withS(customerData.getZip()));
                item.put("Username", new AttributeValue().withS(customerData.getUsername()));
                item.put("Password", new AttributeValue().withS(customerData.getPassword()));


                PutItemRequest itemRequest = new PutItemRequest().withTableName(awsTableName).withItem(item);
                client.putItem(itemRequest);
                item.clear();

                System.out.println("Customer: "+customerData.getUsername());
            }


            awsTableName = "Bikes";
            for (BikeData bikeData : bikeList) {
                Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
                item.put("ModelNumber", new AttributeValue().withS(bikeData.getModelNumber()));
                item.put("Name", new AttributeValue().withS(bikeData.getName()));
                item.put("Description", new AttributeValue().withS(bikeData.getDescription()));
                item.put("Price", new AttributeValue().withN(String.valueOf(bikeData.getPrice())));
                item.put("Quantity", new AttributeValue().withN(String.valueOf(bikeData.getQuantity())));


                PutItemRequest itemRequest = new PutItemRequest().withTableName(awsTableName).withItem(item);
                client.putItem(itemRequest);
                item.clear();

                System.out.println("Bike: "+bikeData.getModelNumber());
            }

            awsTableName = "Orders";
            for (OrderData orderData : orderList) {
                Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
                item.put("OrderID", new AttributeValue().withS(orderData.getOrderID()));
                item.put("BikeID", new AttributeValue().withS(orderData.getBikeId()));
                item.put("CustomerID", new AttributeValue().withS(orderData.getCustomerId()));
                item.put("Price", new AttributeValue().withN(String.valueOf(orderData.getPrice())));
                item.put("Quantity", new AttributeValue().withN(String.valueOf(orderData.getQuantity())));
                item.put("Date", new AttributeValue().withS(orderData.getDate()));




                PutItemRequest itemRequest = new PutItemRequest().withTableName(awsTableName).withItem(item);
                client.putItem(itemRequest);
                item.clear();

                System.out.println("Order: "+orderData.getOrderID());
            }


        } catch (AmazonServiceException e) {
            e.printStackTrace();
        }


    }
    */

}
