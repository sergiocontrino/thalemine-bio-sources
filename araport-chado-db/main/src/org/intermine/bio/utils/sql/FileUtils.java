package org.intermine.bio.utils.sql;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.intermine.bio.dataconversion.StockProcessor;
import org.intermine.bio.postprocess.PostProcessOperationsTask;

public class FileUtils {

    private static final Logger log = Logger.getLogger(FileUtils.class);

    public static String getSqlFileContents(String fileName) {


        StringBuffer sb = new StringBuffer();
        try {
            ClassLoader cl = ClassLoader.getSystemClassLoader();
            log.info("File Path: " + fileName);
             ClassLoader classLoader =  FileUtils.class.getClassLoader();
             InputStream in =
                 classLoader.getResourceAsStream(fileName);
     //   	InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                sb.append(" " + strLine);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
