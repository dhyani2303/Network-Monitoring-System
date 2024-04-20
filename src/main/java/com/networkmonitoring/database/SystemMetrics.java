package com.networkmonitoring.database;

import static com.networkmonitoring.Constants.*;
import static com.networkmonitoring.server.Main.LOGGER;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.sql.*;
import java.time.Instant;

public class SystemMetrics
{
    public boolean putData(JSONObject data)
    {
        PreparedStatement preparedStatement = null;

        try (var connection = DatabaseConnection.getConnection())
        {
            String insertQuery = "INSERT INTO system_metrics (host_ip, percent_user, percent_idle, percent_sys, load_average, poll_timestamp, context_switches,total_memory,used_memory,free_memory,total_swapmemory,used_swapmemory,free_swapmemory) VALUES (?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?)";

             preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setString(1, data.getString(IP_ADDRESS));

            BigDecimal percentUser = data.optBigDecimal("%user", BigDecimal.ZERO);

            BigDecimal percentSys = data.optBigDecimal("%sys", BigDecimal.ZERO);

            BigDecimal percentIdle = data.optBigDecimal("%idle", BigDecimal.ZERO);

            BigDecimal loadAverage = data.optBigDecimal("loadAverage", BigDecimal.ZERO);


            preparedStatement.setBigDecimal(2, percentUser);

            preparedStatement.setBigDecimal(3, percentIdle);

            preparedStatement.setBigDecimal(4, percentSys);

            preparedStatement.setBigDecimal(5, loadAverage);




            preparedStatement.setTimestamp(6, Timestamp.from(Instant.now()));

            long contextSwitches = data.optLong("contextSwitches", 0L);

            preparedStatement.setLong(7, contextSwitches);

            preparedStatement.setInt(8,data.getInt("total_memory"));

            preparedStatement.setInt(9,data.getInt("used_memory"));

            preparedStatement.setInt(10,data.getInt("free_memory"));

            preparedStatement.setInt(11,data.getInt("total_swapmemory"));

            preparedStatement.setInt(12,data.getInt("used_swapmemory"));

            preparedStatement.setInt(13,data.getInt("free_swapmemory"));

            int result= preparedStatement.executeUpdate();

            if(result>0)
            {
              return true;
            }


        } catch (SQLException e)
        {
            LOGGER.error("{}",e.getMessage());

        }
        finally
        {
            if(preparedStatement!=null)
            {
                try
                {
                    preparedStatement.close();

                } catch(SQLException e)
                {
                    LOGGER.error("{}",e.getMessage());
                }
            }
        }
        return false;
    }


}
