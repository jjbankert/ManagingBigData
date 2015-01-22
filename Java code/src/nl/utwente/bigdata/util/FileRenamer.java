package nl.utwente.bigdata.util;

import java.io.*;
import java.util.*;

/**
 * Created by JJ on 18-1-2015.
 */
public class FileRenamer {
    File sourcePath;

    public static void main(String[] args) {
        new FileRenamer();
    }

    private FileRenamer() {
        sourcePath = new File("D:\\BigDataMerged");

        LinkedList<File> sourceFiles = new LinkedList<>(Arrays.asList(sourcePath.listFiles()));
        for (File file : sourceFiles) {
            String name = file.getName();
            name = name.replaceFirst(",",".");
            while(name.length() < 10)
            {
                name = "0" + name;
            }
            file.renameTo(new File(sourcePath.getPath()+ "\\" + name));
            break;
        }
    }
}