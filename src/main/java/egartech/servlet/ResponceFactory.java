package egartech.servlet;

import egartech.handler.CustomTimedReport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by alander on 17.03.17.
 */
public class ResponceFactory {

    private CustomTimedReport customTimedReport;

    public void formCustomReport(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CustomTimedReport report = new CustomTimedReport();
        Date sdate = null;
        Date edate = null;
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        try {
            sdate = format.parse(req.getParameter("sdate"));
            edate = format.parse(req.getParameter("edate"));
        }
        catch (java.text.ParseException e) {
            resp.sendError(418, "Wrong date format");
        }

        System.out.println("CustomTimed preparing data");
        resp.setContentType("text/xls");
        resp.setHeader("Content-Disposition", "attachment; filename=\"report.xls\"");
        try
        {
            OutputStream outputStream = resp.getOutputStream();
            report.generate(sdate, edate, outputStream);
            outputStream.flush();
            outputStream.close();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }
}
