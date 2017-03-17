package egartech.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.util.Date;

import com.atlassian.jira.database.DatabaseConnection;
import egartech.api.Utils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.inject.Inject;
import javax.naming.Context;
import javax.sql.DataSource;



/**
 * Created by alander on 17.03.17.
 */
public class CustomTimedReport {


    public void generate(Date sdate, Date edate, OutputStream result) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("monthReport");
        HSSFRow rowhead = sheet.createRow(1);
        rowhead.createCell(0).setCellValue("1");
        rowhead.createCell(1).setCellValue("2");
        rowhead.createCell(2).setCellFormula("B1+B2");
        rowhead.createCell(3).setCellValue("Email");
        try {
            Connection connection = Utils.getConnection();
            if (connection != null) {
                String sql = "SELECT PNAME FROM PROJECT";
                try{
                    PreparedStatement statement = connection.prepareStatement(sql);
                    ResultSet rs = statement.executeQuery();

                    while (rs.next()) {
                        String s = rs.getString("PNAME");
                        System.out.println("PRRRRTRTRTR===="+s);
                    }
                    connection.close();
                }catch(Exception e){
                    System.out.println(e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            workbook.write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
