package com.nopCommerce.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelFiles {

    private static final Logger logger = LogManager.getLogger("ExcelFiles.class");

    private static final ThreadLocal<FileInputStream> fi = new ThreadLocal<>();
    private static final ThreadLocal<FileOutputStream> fo = new ThreadLocal<>();
    private static final ThreadLocal<XSSFWorkbook> wb = new ThreadLocal<>();
    private static final ThreadLocal<XSSFSheet> ws = new ThreadLocal<>();
    private static final ThreadLocal<XSSFRow> row = new ThreadLocal<>();
    private static final ThreadLocal<XSSFCell> cell = new ThreadLocal<>();


    public static int getRowCount(String xlfile, String xlsheet) {
        try {
            fi.set(new FileInputStream(xlfile));
            wb.set(new XSSFWorkbook(fi.get()));
            ws.set(wb.get().getSheet(xlsheet));
            return ws.get().getLastRowNum();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            closeResources();
        }
    }

    public static int getCellCount(String xlfile, String xlsheet, int rownum){
        try {
            fi.set(new FileInputStream(xlfile));
            wb.set(new XSSFWorkbook(fi.get()));
            ws.set(wb.get().getSheet(xlsheet));
            row.set(ws.get().getRow(rownum));

            return row.get().getLastCellNum();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            closeResources();
        }
    }

    public static String getCellData(String xlfile, String xlsheet, int rownum, int colnum) {
        try {
            fi.set(new FileInputStream(xlfile));
            wb.set(new XSSFWorkbook(fi.get()));
            ws.set(wb.get().getSheet(xlsheet));
            row.set(ws.get().getRow(rownum));
            cell.set(row.get().getCell(colnum));

            DataFormatter formatter = new DataFormatter();
            return formatter.formatCellValue(cell.get());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            closeResources();
        }
    }

    // Set Cell Data
    public static void setCellData(String xlfile, String xlsheet, int rownum, int colnum, String data) {
        try {
            fi.set(new FileInputStream(xlfile));
            wb.set(new XSSFWorkbook(fi.get()));
            ws.set(wb.get().getSheet(xlsheet));
            row.set(ws.get().getRow(rownum));
            cell.set(row.get().getCell(colnum));

            if (cell.get() == null) {
                cell.set(row.get().createCell(colnum));
            }
            cell.get().setCellValue(data);

            fo.set(new FileOutputStream(xlfile));
            wb.get().write(fo.get());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            closeResources();
        }
    }

    // Close all resources after each operation to ensure no conflicts in parallel execution
    private static void closeResources() {

        try {
            if (fi.get() != null) fi.get().close();
            if (fo.get() != null) fo.get().close();
            if (wb.get() != null) wb.get().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
