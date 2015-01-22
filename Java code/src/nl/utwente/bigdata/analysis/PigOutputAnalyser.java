package nl.utwente.bigdata.analysis;

import nl.utwente.bigdata.util.MultilingualismMap;

import java.io.*;

/**
 * Created by JJ on 22-1-2015.
 */
public class PigOutputAnalyser {
    public static void main(String[] args) {
        new PigOutputAnalyser();
    }

    private PigOutputAnalyser() {
        File dataFile = new File("D:\\Documents\\Utwente\\Managing Big Data\\final\\simplePercentagePerCountry\\part-r-00000");
        MultilingualismMap mMap = new MultilingualismMap();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                String[] pieces = line.split("\t");
                mMap.add(pieces[0],pieces[1],Integer.parseInt(pieces[2]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(mMap.toString());
            e.printStackTrace();
        }

        mMap.createPieCharts(4);
        System.out.println(mMap.toString(5));
    }
}
