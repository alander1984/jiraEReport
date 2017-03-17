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
        HSSFRow rowfirstempty = sheet.createRow(0);
        rowfirstempty.createCell(15).setCellValue(0.5);
        HSSFRow rowhead = sheet.createRow(1);
        rowhead.createCell(0).setCellValue("Office");
        rowhead.createCell(1).setCellValue("Period");
        rowhead.createCell(2).setCellValue("Employee");
        rowhead.createCell(3).setCellValue("Name");
        rowhead.createCell(4).setCellValue("Salary");
        rowhead.createCell(5).setCellValue("Active Time");
        rowhead.createCell(6).setCellValue("Sick Days");
        rowhead.createCell(7).setCellValue("Vacation");
        rowhead.createCell(8).setCellValue("Overtime-Weekend");
        rowhead.createCell(9).setCellValue("Travel Pay Per Diem");
        rowhead.createCell(10).setCellValue("Bonus");
        rowhead.createCell(11).setCellValue("Travel Expense");
        rowhead.createCell(12).setCellValue("Gross Salary HRS");
        rowhead.createCell(13).setCellValue("Gross Salary AMT");
        rowhead.createCell(14).setCellValue("Other Salary AMT");
        rowhead.createCell(15).setCellValue("Overhead");
        rowhead.createCell(16).setCellValue("Deposits");
        rowhead.createCell(17).setCellValue("Project");
        Integer irow=3;
        Integer startCWindex=irow;
        String currentWorker = "null";
        try {
            /*
            MSSQL = select s.author,sum(s.timeworked),s.agreement, s._date from (select w.*, cfv.stringvalue as agreement, right(convert(varchar, w.created, 106), 8)  _date from worklog w left join customfieldvalue cfv on w.issueid=cfv.issuewhere w.created between '01.01.2017' and '01.12.2017' ) s group by author,agreement, _date
             */
            Connection connection = Utils.getConnection();
            if (connection != null) {
                String sql = "select s.author,sum(s.timeworked) as spent,s.agreement, s._month, s._year from (\n" +
                        "\tselect w.*, cfv.stringvalue as agreement, extract(month from w.created) as _month, extract(year from w.created) as _year from worklog w left join customfieldvalue cfv on w.issueid=cfv.issue\n" +
                        "\twhere w.created between '01.01.2017' and '01.12.2017') as s\n" +
                        "group by author,agreement, _month, _year order by author";
                try{
                    PreparedStatement statement = connection.prepareStatement(sql);
                    ResultSet rs = statement.executeQuery();
                    while (rs.next()) {
                        System.out.println("1");
                        HSSFRow row = sheet.createRow(irow-1);
                        if (currentWorker.compareTo(rs.getString("AUTHOR"))!=0) {
                            currentWorker = rs.getString("AUTHOR");
                            startCWindex=irow;
                        }
                        else {
                            /*Recount all previous user data*/
                            System.out.println("2");
                            System.out.println("ROW to eq: irow=="+irow.toString()+"  START="+startCWindex.toString());
                            for (int i=startCWindex-1; i<irow-1; i++) {
//                                System.out.println("ROW to eq="+i.toString()+"  START="+startCWindex.toString());
                                HSSFRow crow = sheet.getRow(i);
                                System.out.println("ROUND(+M"+crow.toString()+"/SUM(M"+startCWindex.toString()+":M"+irow.toString()+")*E"+startCWindex.toString()+";1)+I"+crow.toString());
                                System.out.println("4");
                                Integer r = i+1;
                                crow.getCell(13).setCellFormula("ROUND(+M"+r.toString()+"/SUM(M"+startCWindex.toString()+":M"+irow.toString()+")*E"+startCWindex.toString()+",1)+I"+r.toString());
                                System.out.println("5");
                            }
                        }

                        row.createCell(0).setCellValue("Самара");
                        row.createCell(1).setCellValue(rs.getString("_MONTH")+"."+rs.getString("_YEAR"));
                        row.createCell(2).setCellValue("Employee");
                        row.createCell(3).setCellValue(rs.getString("AUTHOR"));
                        row.createCell(4).setCellValue(100);
                        row.createCell(5).setCellValue(rs.getInt("SPENT"));
                        row.createCell(6).setCellValue(0);
                        row.createCell(7).setCellValue(0);
                        row.createCell(8).setCellValue(0);
                        row.createCell(9).setCellValue(0);
                        row.createCell(10).setCellValue(0);
                        row.createCell(11).setCellValue(0);
                        row.createCell(12).setCellFormula("F"+irow.toString()+"+G"+irow.toString()+"+H"+irow.toString());

                        row.createCell(13).setCellFormula("ROUND(+M"+irow.toString()+"/SUM(M"+startCWindex.toString()+":M"+irow.toString()+")*E"+startCWindex.toString()+",1)+I"+irow.toString());

                        row.createCell(14).setCellFormula("SUM(J"+irow.toString()+":L"+irow.toString()+")");
                        row.createCell(15).setCellFormula("+N"+irow.toString()+"*$P$1");
                        row.createCell(17).setCellValue(rs.getString("AGREEMENT"));
                        irow++;
                        System.out.println("ROW="+irow.toString());
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
