package egartech.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by alander on 17.03.17.
 */
public class Utils {

    public static Connection getPostgresConnection() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        Connection connection = null;
        try {

            connection = DriverManager.getConnection(
                    "jdbc:postgresql://127.0.0.1:5432/jiradb", "postgres",
                    "postgresql17");

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


}
