package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Jan-Peter on 19.05.2017.
 */
public class API {

    private static HashMap<String, API> list = new HashMap<String, API>();
    private HashMap<String,String> properties;

    public API() {
        this.properties = new HashMap<String,String>();
    }


    public static API getOrCreate(String name) {

        if(list.containsKey(name)) {

            return list.get(name);

        } else {

            API api = new API();

            list.put(name, api);

            return new API();

        }


    }

    public void setProperty(RFDEntry entry) {

        if(properties.containsKey(entry.getPredicate())) {

            String value = properties.get(entry.getPredicate()) + ", " + entry.getObject();

            properties.remove(entry.getPredicate());
            properties.put(entry.getPredicate(), value);

        } else {

            properties.put(entry.getPredicate(), entry.getObject());

        }

        //System.out.println(entry.getSubject() + ": Set " + entry.getPredicate() + " with value \"" + entry.getObject() + "\"");

    }

    public static HashMap<String, API> getList() {
        return list;
    }

    public HashMap<String, String> getProperties() {
        return properties;
    }

    public static void createCSV() {

        String head = constructHead();

        Iterator<Map.Entry<String,API>> apiIterator = list.entrySet().iterator();

        int counter = 0;

        while(apiIterator.hasNext()) {

            Map.Entry<String, API> entry = apiIterator.next();

            if(counter < entry.getValue().getProperties().size()) {

                counter = entry.getValue().getProperties().size();

            }

        }

        System.out.println(counter);



    }

    private static String constructHead() {

        return "";


    }
}
