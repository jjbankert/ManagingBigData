package nl.utwente.bigdata.scrape;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.*;
import java.util.*;

/**
 * Created by JJ on 12-1-2015.
 */
public class RawTwitterStore implements RawStreamListener {
	//use your private twitter API keys here
    String consumerKey = "";
    String consumerSecret = "";
    String accessToken = "";
    String accessTokenSecret = "";
    double[][] boundingbox = {{-180,-80},{180,80}}; //entire world

    LinkedList<String> buffer;
    long tupleCounter = 0;
    int fileCounter = 0;

    public RawTwitterStore() {
        buffer = new LinkedList<String>();
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(consumerKey);
        configurationBuilder.setOAuthConsumerSecret(consumerSecret);
        configurationBuilder.setOAuthAccessToken(accessToken);
        configurationBuilder.setOAuthAccessTokenSecret(accessTokenSecret);


        TwitterStream stream = new TwitterStreamFactory(configurationBuilder.build()).getInstance();
        stream.addListener(this);
        stream.filter(new FilterQuery().locations(boundingbox));
    }

    @Override
    public void onMessage(String s) {
        buffer.add(s);
        tupleCounter++;

        if (tupleCounter % 1000 == 0) {
            System.out.println(tupleCounter);

            if (tupleCounter % 10000 == 0) {

                try {
                    PrintWriter writer = new PrintWriter("D:\\BigData\\" + System.currentTimeMillis() + ".txt", "UTF-8");
                    for (String value : buffer) {
                        writer.println(value);
                    }
                    writer.close();

                    //System.out.println("Wrote to " + f.getAbsolutePath());
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                fileCounter++;
                buffer.clear();
            }
        }
    }

    @Override
    public void onException(Exception e) {
        e.printStackTrace();
    }

    public static void main(String[] args) {
        new RawTwitterStore();
    }
}
