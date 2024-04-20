package com.networkmonitoring.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import static com.networkmonitoring.server.Config.*;
import static com.networkmonitoring.server.Main.LOGGER;

public class DatabaseConnection
{
    private DatabaseConnection()
    {

    }
    private static final String URL = "jdbc:mysql://localhost:3306/Network_Monitoring";

    public static  Connection getConnection()
    {
        Connection connection=null;

        try
        {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        } catch(SQLException e)
        {
            LOGGER.error("{}",e.getMessage());
        }

        return connection;

    }


}
