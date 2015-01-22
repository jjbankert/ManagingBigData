package nl.utwente.bigdata.util;

import java.text.NumberFormat;
import java.util.*;

/**
 * Created by JJ on 13-1-2015.
 */
public class Bag<K, V> extends HashMap<Object, Integer> {

    public void increment(K key) {
        int count = containsKey(key) ? get(key) + 1 : 1;
        put(key, count);
    }

    public void add(K key, V count) {
        int newCount = containsKey(key) ? get(key) + (Integer) count : (Integer) count;
        put(key, newCount);
    }

    public Set<Entry<Object, Integer>> sortedByValue() {
        return MapUtil.sortByValue(this).entrySet();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        NumberFormat percFormat = NumberFormat.getPercentInstance();
        percFormat.setMinimumFractionDigits(2);
        percFormat.setMaximumFractionDigits(2);

        int total = 0;
        for (Integer value : values()) {
            total += value;
        }

        for (Entry<Object, Integer> entry : MapUtil.sortByValue(this).entrySet()) {
            double perc = ((double) entry.getValue()) / total;
            stringBuilder.append(entry.getKey().toString() + ": " + entry.getValue() + " (" + percFormat.format(perc) + ")\n");
        }

        return stringBuilder.toString();
    }
}
