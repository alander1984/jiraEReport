package egartech.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.atlassian.jira.database.DatabaseConnection;
import egartech.api.Utils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;




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
            Connection connection = Utils.getCurrentConnection();
            if (connection != null) {
/* mssql query right, checked
                String sql = "select f.stringvalue agreement, month(w.created) as _month, year(w.created) as _year, u.display_name as author, "+
                        "sum(w.timeworked) as spent from jiraissue i   " +
                        "left join (select cfv.issue as issueid, cfv.stringvalue from customfieldvalue cfv " +
                        "left outer join customfield cf on cfv.customfield = cf.id where cf.cfname='Agreement') f on f.issueid=i.id " +
                        "left join worklog w on w.issueid=i.id left join cwd_user u on u.user_name=w.author "+
                        "where u.user_name is not null and w.startdate between ? and ?" +
                        " group by u.display_name,f.stringvalue, month(w.created), year(w.created)";
*/
/*postgresql query
 */
                String sql = "select f.stringvalue agreement, extract(month from w.startdate) as _month, extract(year from w.startdate) as _year, u.display_name as author,"+
                        "sum(w.timeworked) as spent from jiraissue i "+
                "left join (select cfv.issue as issueid, cfv.stringvalue from customfieldvalue cfv "+
                "left outer join customfield cf on cfv.customfield = cf.id where cf.cfname='Agreement') f on f.issueid=i.id "+
                "left join worklog w on w.issueid=i.id left join cwd_user u on u.user_name=w.author "+
                "where u.user_name is not null and w.startdate between ? and ? "+
                "group by u.display_name,f.stringvalue, extract(month from w.startdate), extract(year from w.startdate) "+
                "order by extract(year from w.startdate), extract(month from w.startdate), u.display_name, f.stringvalue";

                try{
                    PreparedStatement statement = connection.prepareStatement(sql);
                    java.sql.Date ssdate = new java.sql.Date(sdate.getTime());
                    java.sql.Date sedate = new java.sql.Date(edate.getTime());
                    System.out.println(ssdate.toString() +" : "+sedate.toString());
                    statement.setDate(1, ssdate);
                    statement.setDate(2, sedate);
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
                        if (startCWindex==irow) {
                            row.createCell(4).setCellValue(100);
                        }
                        row.createCell(5).setCellValue(Math.round(rs.getInt("SPENT")/3600));
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
