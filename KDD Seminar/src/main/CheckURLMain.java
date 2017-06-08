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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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

                if(!current.isNull("api")) {
                    api.setURI(current.getJSONObject("api").getString("value"));
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

                    /*
                    Check Homepage -> ProgrammableWeb
                    try {
                        //System.out.println("Check URL: \"" + api.getHomepage() + "\"");
                        String title = Jsoup.connect(api.getHomepage()).get().getElementsByTag("title").get(0).data();
                    } catch (Exception e) {
                        //e.printStackTrace();
                        api.setHomepage("");
                        System.out.println("No hp for " + api.getName());
                    }*/

                    try {

                        Document page = Jsoup.connect(api.getBlog()).get();

                        String body =Jsoup.parse(page.html()).text().replaceAll("[^A-Za-z0-9 ]","");
                        System.out.println(body.length() + " chars found");
                        api.setBody(body);

                        bodyList.add(api);

                        String keywords = page.select("meta[name=keywords]").get(0).attr("content").replaceAll("[^A-Za-z0-9 ]","");
                        api.setKeywords(keywords);



                        JSON_API.counter++;
                    } catch (Exception e) {
                        //e.printStackTrace();
                        api.setBlog("");
                        //System.out.println("No blog for " + api.getName());
                    }

                    /* if Homepage is set and Blog is empty -> try to get link from ProgrammableWeb
                    if(api.getBlog()==null || api.getBlog().equals("") && api.getHomepage()!=null && !api.getHomepage().equals("")) {

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
        //Do something if enter is pressed

        Scanner scanner = new Scanner(System.in);
        String readString = scanner.nextLine();
        System.out.println(readString);
        if (readString.equals("")) {

            System.out.println("Start creating JSON... " + bodyList.size());

            //Write all BodyAPIs to JSON
            try {
                PrintWriter writer = new PrintWriter("KDD Seminar/data_complete.json", "UTF-8");
                createJSONHead(writer);

                Iterator<JSON_API> iterator1 = bodyList.iterator();

                int counter1 = 0;

                while(iterator1.hasNext()) {

                    JSON_API api = iterator1.next();

                    writer.println("{ \"name\": { \"type\": \"literal\", \"value\": \"" + api.getName() +"\" }\t, \"api\": { \"type\": \"uri\", \"value\": \"" + api.getURI() + "\" }\t, \"homepage\": { \"type\": \"uri\", \"value\": \"" + api.getHomepage() + "\" }\t, \"blog\": { \"type\": \"uri\", \"value\": \"" + api.getBlog() + "\" }\t, \"endpoint\": { \"type\": \"literal\", \"value\": \"" + api.getEndpoint() + "\" }, \"keywords\": { \"type\": \"literal\", \"value\": \"" + api.getKeywords() + "\" }, \"blogDescr\": { \"type\": \"literal\", \"value\": \"" + api.getBody() + "\" }},");
                    counter1++;

                }

                System.out.println("Ready! " + counter1);



                //Close JSON
                closeJSON(writer);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        if (scanner.hasNextLine())
            readString = scanner.nextLine();
        else
            readString = null;

    }

    private static void createJSONHead(PrintWriter writer) {

        writer.println("{ \"head\": { \"link\": [], \"vars\": [\"name\", \"api\", \"homepage\", \"blog\", \"endpoint\"] },");
        writer.println("  \"results\": { \"distinct\": false, \"ordered\": true, \"bindings\": [");

    }

    private static void closeJSON(PrintWriter writer) {

        writer.println("] } }");
        writer.close();

    }


}
