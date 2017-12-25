package me.spacegame.databases;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.transform.Result;

/**
 * Created by Felix on 25/12/2017.
 */

public class Database
{

    private Driver m_Driver;
    private Connection m_Connection;

    private String m_Database;
    private String m_Host;
    private String m_User;
    private String m_Password;


    /**
     * Creates a database objects and connects using MySQL.
     * @param host Hostname and port of the server separated by ':' in a single string.
     * @param database Name of the database.
     * @param user Username on the server.
     * @param password Password of the user.
     */
    public Database(String host, String database, String user, String password)
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Could not load JDBC Driver!");
            return;
        }

        m_Host = host;
        m_Database = database;
        m_User = user;
        m_Password = password;

        Connect();
    }

    private void Connect()
    {
        try
        {
            m_Connection = DriverManager.getConnection("jdbc:mysql://" + m_Host + "/" + m_Database, m_User, m_Password);
        }
        catch(SQLException e)
        {
            System.err.println("[Database] Could not connect to MySQL server.");
            e.printStackTrace();
            return;
        }
        System.out.println("[Database] Successfully connected to the database.");
    }

    /**
     * Executes a query (e.g. SELECT) on the server and returns the ResultSet.
     * @param query Query to execute.
     * @return Result of the operation.
     */
    public ResultSet Query(String query)
    {
        ResultSet result;
        try
        {
            Statement statement = m_Connection.createStatement();
            result = statement.executeQuery(query);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    /**
     * Executes an update query (e.g. INSERT, UPDATE) on the server and returns the generated primary key.
     * @param query Query to execute.
     * @param items Values of the wildcards used in the query
     *              (e.g. (INSERT INTO playlist (title, interpret, url) VALUES (?, ?, ?)) contains 3 wildcards instead of the values. The values passed
     *              into this function are replaced with the wildcards in the query).
     * @return Generated Primary Key.
     */
    public int Update(String query, String... items)
    {
        PreparedStatement statement;
        ResultSet result;
        int key = -1;

        try
        {
            statement = m_Connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            if(items.length != 0)
            {
                for(int i = 0; i < items.length; i++)
                {
                    statement.setString(i + 1, items[i]);
                }
            }

            statement.executeUpdate();
            result = statement.getGeneratedKeys();

            if(result.next())
            {
                key = result.getInt(1);
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return -1;
        }

        return key;
    }

    /**
     * Prints the data from a ResultSet to the console.
     * @param rs ResultSet.
     */
    public void PrintResultSet(ResultSet rs)
    {
        try
        {
            int colCount = rs.getMetaData().getColumnCount();
            while(rs.next())
            {
                for(int i = 0; i < colCount; i++)
                {
                    System.out.print(rs.getMetaData().getColumnName(i + 1) + ": " + rs.getString(i + 1) + (i == (colCount-1) ? "" : " | "));
                }
                System.out.println();
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return;
        }
    }


}
