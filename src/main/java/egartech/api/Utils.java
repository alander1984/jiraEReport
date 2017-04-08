package egartech.api;

import com.atlassian.jira.component.ComponentAccessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.atlassian.jira.component.ComponentAccessor;
import org.ofbiz.core.entity.ConnectionFactory;
import org.ofbiz.core.entity.DelegatorInterface;
import org.ofbiz.core.entity.GenericEntityException;
import org.ofbiz.core.entity.jdbc.SQLProcessor;

/**
 * Created by alander on 17.03.17.
 */
public class Utils {

    public static Connection getPostgresConnection() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        Connection connection = null;
        try {

            connection = DriverManager.getConnection(
                    "jdbc:postgresql://127.0.0.1:5432/jira", "postgres",
                    "Jira733");

        } catch (SQLException e) {

            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        return connection;
    }


    public static Connection getMsSQLConnection() throws ClassNotFoundException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        Connection connection = null;
        try {

            connection = DriverManager.getConnection(
                    "jdbc:sqlserver://sql3.egar.egartech.com:1433;databaseName=jira6_4_1", "testuser",
                    "Test1234");

        } catch (SQLException e) {

            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        return connection;
    }

    public static Connection getCurrentConnection() throws SQLException,GenericEntityException {

        DelegatorInterface delegator = (DelegatorInterface) ComponentAccessor.getComponent(DelegatorInterface.class);
        String helperName = delegator.getGroupHelperName("default");    // gets the helper (localderby, localmysql, localpostgres, etc.) for your entity group org.ofbiz
        Connection conn = ConnectionFactory.getConnection(helperName);
        System.out.print(conn.getMetaData().getURL());
        return conn;
    }


}
