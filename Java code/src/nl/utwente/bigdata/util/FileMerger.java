package nl.utwente.bigdata.util;

import java.io.*;
import java.util.*;

/**
 * Created by JJ on 18-1-2015.
 */
public class FileMerger {
    File sourcePath;
    File destinationPath;

    public static void main(String[] args) {
        new FileMerger();
    }

    private FileMerger() {
        long startTime = System.currentTimeMillis();
        sourcePath = new File("D:\\BigData\\");
        destinationPath = new File("D:\\BigDataMerged\\");

        ArrayList<File> sourceFiles = new ArrayList<>(Arrays.asList(sourcePath.listFiles()));
        for (int i = 0; i < sourceFiles.size(); i++) {
            if (!sourceFiles.get(i).getName().endsWith(".txt")) {
                sourceFiles.remove(i); //expensive in arraylist, but ok trade off for direct lookup
            }
        }

        //sort files by filename (actually by path)
        Collections.sort(sourceFiles);

        //start merging from this file, start at least from 0
        //last file might not be full, so redo and overwrite it
        int fileIndex = Math.max((destinationPath.listFiles().length - 1) * 10, 0);
        //fileIndex = 5910;
        System.out.println("Merging " + (sourceFiles.size()-fileIndex) + " files");

        StringBuilder stringBuilder = new StringBuilder();
        boolean stop = false;
        while (!stop) {
            File file = sourceFiles.get(fileIndex);
            System.out.print("File " + fileIndex + "(" + file.getName() + ")...");
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                String line;
                //int linecount = 0; //for debugging
                while ((line = in.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }

                in.close();
                fileIndex++;

                //merge files
                if (fileIndex % 10 == 0 || fileIndex == sourceFiles.size()) {
                    String fname = fileIndex / 100 + "." + (fileIndex / 10) % 10 + "M";
                    while (fname.length() < 6) {
                        fname = "0" + fname;
                    }
                    PrintWriter writer = new PrintWriter("D:\\BigDataMerged\\" + fname + ".txt", "UTF-8");
                    writer.append(stringBuilder.toString());
                    writer.close();
                    stringBuilder.setLength(0); //empty the buffer

                    if (fileIndex == sourceFiles.size()) {
                        stop = true;
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            System.out.println("done");
        }
        System.out.println("Time: " + (System.currentTimeMillis() - startTime));
    }
}
