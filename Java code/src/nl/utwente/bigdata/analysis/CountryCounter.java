package nl.utwente.bigdata.analysis;

import nl.utwente.bigdata.util.MultilingualismMap;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

/**
 * Created by JJ on 12-1-2015.
 */
public class CountryCounter {
    File basePath;
    MultilingualismMap multiLing;

    public static void main(String[] args) {
        new CountryCounter();
    }

    private CountryCounter() {
        basePath = new File("D:\\BigData\\");
        //basePath = new File("D:\\tmp\\");
        multiLing = new MultilingualismMap();

        int fileCounter = 0;
        long startTime = System.currentTimeMillis();
        for (File file : basePath.listFiles()) {
            fileCounter++;
            System.out.print("File " + fileCounter + "...");
            if (file.getName().endsWith(".txt")) {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                    String line;
                    //int linecount = 0; //for debugging
                    while ((line = in.readLine()) != null) {
                        //System.out.println("line " + ++linecount); //for debugging
                        JSONObject jsonTweet = new JSONObject(line);

                        String countryID;
                        if (jsonTweet.isNull("place")) {
                            countryID = "unknown";
                        } else {
                            JSONObject place = jsonTweet.getJSONObject("place");
                            countryID = place.getString("country_code").toUpperCase();
                        }


                        String lang;
                        if (jsonTweet.isNull("lang")) {
                            lang = "und";
                        } else {
                            lang = jsonTweet.getString("lang").toLowerCase();
                        }
                        multiLing.increment(countryID, lang);
                    }

                    in.close();

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("done");
        }
        System.out.println("Time: " + (System.currentTimeMillis() - startTime));
        System.out.print(multiLing.toString());
    }
}
