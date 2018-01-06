package me.spacegame.databases;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.rowset.serial.SerialRef;

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

    private int m_GameID;

    public boolean connectionEstablished = false;

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
            connectionEstablished = false;
            return;
        }

        m_Host = host;
        m_Database = database;
        m_User = user;
        m_Password = password;

        Connect();
        Init();
    }

    private void Connect()
    {
        try
        {
            m_Connection = DriverManager.getConnection("jdbc:mysql://" + m_Host + "/" + m_Database, m_User, m_Password);
        }
        catch(Exception e)
        {
            System.err.println("[Database] Could not connect to MySQL server.");
            e.printStackTrace();
            connectionEstablished = false;
            return;
        }
        System.out.println("[Database] Successfully connected to the database.");
        connectionEstablished = true;
    }

    private void Init()
    {
        ResultSet rs = Query("SELECT id from games where name = \"spacegame\";");
        try
        {
            while(rs.next())
            {
                m_GameID = rs.getInt(1);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            connectionEstablished = false;
            return;
        }
        System.out.println("[Database] Game ID of this game is " + m_GameID);
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
        catch (Exception e)
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
        catch(Exception e)
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
        if(rs == null)
        {
            System.out.println("[Database] ResultSet does not exist.");
            return;
        }
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
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }
    }

    /**
     * Returns the first value of the ResultSet. Useful for queries like (SELECT id FROM players WHERE name = "test";)
     * @param rs ResultSet to use.
     * @return First value of the ResultSet as String.
     */
    public String FirstValue(ResultSet rs)
    {
        if(rs == null) return "[Database] ResultSet does not exist.";
        String result = "";
        try
        {
            if(rs.next())
            {
                result = rs.getString(1);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "ERROR";
        }
        return result;
    }

    /**
     * Creates a user in the database of none exists by that name and / or returns the Primary Key (ID) of the user.
     * @param username Username.
     * @return Primary Key (ID) of the user.
     */
    public int GetUserID(String username)
    {
        String id = FirstValue(Query("SELECT id FROM players WHERE name = \"" + username + "\" ;"));
        int pid = -1;
        if(id == "")
        {
            int playerID = Update("INSERT INTO players (name, regdate) VALUES (?, now());", username);
            pid = playerID;
        }
        else
        {
            pid = Integer.parseInt(id);
        }

        System.out.println("Player exists with ID: " + pid);
        return pid;
    }

    public void AddScore(int userid, double score)
    {
        Update("INSERT INTO scores (score, player_id, game_id, date) VALUES (?, ?, ?, now());", Double.toString(score), userid + "", m_GameID + "");
    }

    public void CloseConnection()
    {
        try {
            m_Connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Could not close MySQL connection.");
        }
    }

}
