package com.nopCommerce.factory;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ReadFiles {

    public static final Properties config= new Properties();
    private static FileInputStream fis;
    private static final Logger logger = LogManager.getLogger("ReadFiles.class");

    public static final List<File> excelFiles = new ArrayList<>();


    public static void loadConfigFiles() {
        try {
            fis = new FileInputStream(System.getProperty("user.dir")+"//configure.properties");
            config.load(fis);
            loadExcelFiles();

        } catch (Exception e) {
            logger.error("Error while loading of configure properties ", e);
        }

        finally {
            try {
                if(fis!=null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void loadExcelFiles() {
        int countFiles=0;
        try{
            String testExcelFileDir = Thread.currentThread().getContextClassLoader().getResource("excelFiles").getFile();
            Collection<File> files = FileUtils.listFiles(new File(testExcelFileDir), new String[]{"xlsx"}, true);
            excelFiles.addAll(files);
            countFiles = excelFiles.size();
        }catch (Exception e){
            logger.error("Error while loading excel files: ", e);
        }
        logger.info("No of excel files loaded: {}", countFiles);
    }

    public static String getPath(String fileName){
        return System.getProperty("user.dir")+"//src//test//resources//excelFiles//"+fileName+".xlsx";
    }


}
