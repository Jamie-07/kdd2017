package main;

import model.JSON_API;
import org.apache.xmlbeans.impl.xb.xsdschema.impl.DocumentationDocumentImpl;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created by Jan-Peter on 04.06.2017.
 */
public class CheckURLMain {

    public static void main(String[] args) {

        ArrayList<JSON_API> list = new ArrayList<JSON_API>();
        ArrayList<JSON_API> bodyList = new ArrayList<JSON_API>();

        try {
            String fileContent = CrawlerMain.readFile("KDD Seminar/data_all.json", Charset.forName("UTF-8"));

            JSONObject file = new JSONObject(fileContent);
            JSONObject results = file.getJSONObject("results");
            JSONArray apis = results.getJSONArray("bindings");

            for(int i=0; i<apis.length(); i++) {

                JSONObject current = apis.getJSONObject(i);

                JSON_API api = new JSON_API();
                list.add(api);

                api.setName(current.getJSONObject("name").getString("value"));
                api.setHomepage(current.getJSONObject("homepage").getString("value"));
                if(!current.isNull("blog")) {
                    api.setBlog(current.getJSONObject("blog").getString("value"));
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Iterator<JSON_API> iterator = list.iterator();

        System.out.println(list.size() + " APIs.");

        int counter = 0;

        while(iterator.hasNext()) {

            JSON_API api = iterator.next();

            new Thread() {

                public void run() {

                    /*try {
                        //System.out.println("Check URL: \"" + api.getHomepage() + "\"");
                        String title = Jsoup.connect(api.getHomepage()).get().getElementsByTag("title").get(0).data();
                    } catch (Exception e) {
                        //e.printStackTrace();
                        api.setHomepage("");
                        System.out.println("No hp for " + api.getName());
                    }*/

                    try {
                        String body = Jsoup.connect(api.getBlog()).get().html();
                        System.out.println(body.length() + " chars found");
                        api.setBody(body);

                        bodyList.add(api);

                        JSON_API.counter++;
                    } catch (Exception e) {
                        //e.printStackTrace();
                        api.setBlog("");
                        //System.out.println("No blog for " + api.getName());
                    }

                    /*if(api.getBlog()==null || api.getBlog().equals("") && api.getHomepage()!=null && !api.getHomepage().equals("")) {

                        //Take Blog from Programmable web
                        try {
                            Document document = Jsoup.connect(api.getHomepage()).get();
                            Elements labels = document.getElementsContainingText("API Portal / Home Page");
                            System.out.println(labels.get(0).nextElementSibling().data() + " <- URL?");

                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }*/

                    //System.out.println(api);

                }

            }.start();

        }

        //Check

        Scanner scanner = new Scanner(System.in);
        String readString = scanner.nextLine();
        System.out.println(readString);
        if (readString.equals("")) {



        }
        if (scanner.hasNextLine())
            readString = scanner.nextLine();
        else
            readString = null;

    }


}
