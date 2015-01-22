package nl.utwente.bigdata.util;

import org.jfree.data.general.DefaultPieDataset;

import java.text.NumberFormat;
import java.util.*;

/**
 * Created by JJ on 13-1-2015.
 */
public class MultilingualismMap {
    private HashMap<String, Bag<String, Integer>> lingMap;
    private Bag<String, Integer> countriesMap;
    private int tweetCount;

    public MultilingualismMap() {
        tweetCount = 0;
        countriesMap = new Bag<>();
        lingMap = new HashMap<>();
    }

    public void increment(String country, String lang) {
        //increment number of tweets
        tweetCount++;

        //count tweets per country
        countriesMap.increment(country);

        //count tweets per language per country
        if (!lingMap.containsKey(country)) {
            lingMap.put(country, new Bag<>());
        }
        lingMap.get(country).increment(lang);
    }

    public void add(String country, String lang, Integer count) {
        //count tweets
        tweetCount += count;

        //count tweets per country
        if (!countriesMap.containsKey(country)) {
            countriesMap.put(country, 0);
        }
        countriesMap.put(country, countriesMap.get(country) + count);

        //count tweets per country per language
        if (!lingMap.containsKey(country)) {
            lingMap.put(country, new Bag<>());
        }
        lingMap.get(country).add(lang, count);
    }

    public void createPieCharts(int maxItemsPerCountry) {
        for (Map.Entry<Object, Integer> entry : MapUtil.sortByValue(countriesMap).entrySet()) {
            DefaultPieDataset dataset = new DefaultPieDataset();

            int countryTotal = entry.getValue();
            int countryRemaining = countryTotal;
            int limitCounter = 0;

            for (Map.Entry<Object, Integer> countryLangEntry : MapUtil.sortByValue(lingMap.get(entry.getKey())).entrySet()) {

                double perc = ((double) countryLangEntry.getValue()) / countryTotal;
                dataset.setValue(countryLangEntry.getKey().toString(), perc);

                countryRemaining -= countryLangEntry.getValue();
                limitCounter++;
                dataset.setValue("rest", perc);

                if (limitCounter == maxItemsPerCountry) {
                    perc = ((double) countryRemaining) / countryTotal;
                    break;
                }
            }

            PieChartMaker.saveChart(dataset,entry.getKey().toString());
        }
    }


    public String toString(Integer maxItemsPerCountry) {
        boolean limitItemsPerCountry = maxItemsPerCountry != null;

        NumberFormat percFormat = NumberFormat.getPercentInstance();
        percFormat.setMinimumFractionDigits(2);
        percFormat.setMaximumFractionDigits(2);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n\nResults from " + tweetCount + " tweets\n");
        stringBuilder.append("\nCountry results\n");
        stringBuilder.append(countriesMap.toString());

        stringBuilder.append("\nMultilingualism results\n");
        for (Map.Entry<Object, Integer> entry : MapUtil.sortByValue(countriesMap).entrySet()) {
            stringBuilder.append(entry.getKey() + ": " + entry.getValue() + "\n");
            if (limitItemsPerCountry) {

                int countryTotal = entry.getValue();
                int countryRemaining = countryTotal;
                int limitCounter = 0;

                for (Map.Entry<Object, Integer> countryLangEntry : MapUtil.sortByValue(lingMap.get(entry.getKey())).entrySet()) {

                    double perc = ((double) countryLangEntry.getValue()) / countryTotal;

                    stringBuilder.append("\t" + countryLangEntry.getKey().toString() + ": " + countryLangEntry.getValue() + " (" + percFormat.format(perc) + ")\n");
                    countryRemaining -= countryLangEntry.getValue();
                    limitCounter++;

                    if (limitItemsPerCountry && limitCounter == maxItemsPerCountry) {
                        perc = ((double) countryRemaining) / countryTotal;
                        stringBuilder.append("\tOther: " + countryRemaining + " (" + percFormat.format(perc) + ")\n");
                        break;
                    }
                }

//                String[] languages = lingMap.get(entry.getKey()).toString().split("\n");
//                for (int i = 0; i < languages.length && (!limitItemsPerCountry || i < maxItemsPerCountry); i++) {
//                    stringBuilder.append("\t" + languages[i] + "\n");
//                }
            }
        }
        return stringBuilder.toString();
    }

    public String toString() {
        return toString(null);
    }
}
