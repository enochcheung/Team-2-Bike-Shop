package com.enochc.software648.bikeshop;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Generator {
    public static final String DICT_TXT = "dict.txt";
    private final ArrayList<String> dict;
    private static final String[] states = {"AL","AK","AZ","AR","CA","CO","CT","DE","FL","GA","HI","ID","IL",
            "IN","IA","KS","KY","LA","ME","MD","MA","MI","MN","MS","MO","MT","NE","NV","NH","NJ","NM","NY",
            "NC","ND","OH","OK","OR","PA","RI","SC","SD","TN","TX","UT","VT","VA","WA","WV","WI","WY"};
    private static final long START_DATE = 1230786000000L;    // milliseconds corresponding to 2009/1/1
    private static final long DATE_INTERVAL = 189216000000L;  // milliseconds corresponding to 6 years

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

        String customerId = String.valueOf(random.nextInt(customerCounter));
        String bikeId = String.valueOf(random.nextInt(bikeCounter));
        int price = 1+random.nextInt(2000);
        int quantity = 1+random.nextInt(2);

        long milliseconds = START_DATE+((long) (random.nextDouble()*DATE_INTERVAL));
        Date date = new Date(milliseconds);


        return new OrderData(customerId,bikeId,quantity,price,date);
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

}
