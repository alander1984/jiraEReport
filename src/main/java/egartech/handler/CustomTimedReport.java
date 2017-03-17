package egartech.handler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.commons.io.*;

/**
 * Created by alander on 17.03.17.
 */
public class CustomTimedReport {

    public void generate(Date sdate, Date edate, OutputStream result) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("monthReport");
        HSSFRow rowhead = sheet.createRow(1);
        rowhead.createCell(0).setCellValue("No.");
        rowhead.createCell(1).setCellValue("Name");
        rowhead.createCell(2).setCellValue("Address");
        rowhead.createCell(3).setCellValue("Email");
        try {
            workbook.write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
