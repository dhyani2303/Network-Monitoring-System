package com.networkmonitoring.database;

import org.json.JSONObject;

import static com.networkmonitoring.Constants.*;
import static com.networkmonitoring.server.Main.LOGGER;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDiscovery
{
    public JSONObject createDiscoveryProfile(JSONObject userData)
    {
        var responseToSend = new JSONObject();

        PreparedStatement preparedStatement = null;


        try(var connection = DatabaseConnection.getConnection())
        {
            var query = "INSERT INTO discovery_profile (user_id,credprofile_id,port,ip_address,device_type,is_provisioned) VALUES (?,?,?,?,?,?)";

            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, userData.getInt(USER_ID));

            preparedStatement.setInt(2, userData.getInt(CREDPROFILE_ID));

            preparedStatement.setInt(3, userData.getInt(PORT));

            preparedStatement.setString(4, userData.getString(IP_ADDRESS));

            preparedStatement.setString(5, userData.getString(DEVICE_TYPE));

            preparedStatement.setBoolean(6, userData.getBoolean(IS_PROVISIONED));


            var result = preparedStatement.executeUpdate();

            if(result > 0)
            {
                responseToSend.put(STATUS, SUCCESS);

            }
            else
            {
                responseToSend.put(STATUS, FAIL);

                responseToSend.put(MESSAGE, "You need to first create credential profile!");
            }
        } catch(SQLException e)
        {
            responseToSend.put(STATUS, FAIL);

            responseToSend.put(MESSAGE, "An error occurred");

            LOGGER.error("{}", e.getMessage());

        } finally
        {
            if(preparedStatement != null)
            {
                try
                {
                    preparedStatement.close();

                } catch(SQLException e)
                {
                    LOGGER.error("{}", e.getMessage());
                }
            }
        }


        return responseToSend;


    }

    public static JSONObject viewDiscoveryProfile(JSONObject userData)
    {
        PreparedStatement preparedStatement = null;

        var resultJSON = new JSONObject();

        try(Connection connection = DatabaseConnection.getConnection())
        {
            var query = "SELECT * FROM discovery_profile WHERE user_id = ?";

            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, userData.getInt(USER_ID));

            ResultSet resultSet = preparedStatement.executeQuery();

            var counter = 0;


            if(resultSet.isBeforeFirst())
            {
                while(resultSet.next())
                {
                    var innerJO = new JSONObject();

                    innerJO.put(CREDPROFILE_ID, resultSet.getInt(CREDPROFILE_ID));

                    innerJO.put(DISCOVERY_ID, resultSet.getInt(DISCOVERY_ID));

                    innerJO.put(PORT, resultSet.getInt(PORT));

                    innerJO.put(IP_ADDRESS, resultSet.getString(IP_ADDRESS));

                    innerJO.put(DEVICE_TYPE, resultSet.getString(DEVICE_TYPE));

                    innerJO.put(IS_PROVISIONED, resultSet.getString(IS_PROVISIONED));

                    resultJSON.put(String.valueOf(++counter), innerJO);
                }


                resultJSON.put(STATUS, SUCCESS);
            }
            else
            {
                resultJSON.put(STATUS, FAIL);

                resultJSON.put(MESSAGE, "You do not have any discovery profile");
            }


        } catch(SQLException e)
        {
            resultJSON.put(STATUS, FAIL);

            LOGGER.error("{}", e.getMessage());

            resultJSON.put(MESSAGE, "Some error occurred");
        } finally
        {
            if(preparedStatement != null)
            {
                try
                {
                    preparedStatement.close();

                } catch(SQLException e)
                {
                    LOGGER.error("{}", e.getMessage());
                }
            }
        }

        return resultJSON;
    }

    public JSONObject discoveryProfileExists(int discoveryId)
    {
        PreparedStatement preparedStatement = null;

        var responseToSend = new JSONObject();

        try(var connection = DatabaseConnection.getConnection())
        {
            var query = " SELECT port,ip_address,hostname,password FROM discovery_profile  JOIN credential_profiles ON discovery_profile.credprofile_id = credential_profiles.credprofile_id WHERE discovery_id=?";

            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, discoveryId);

            var resultSet = preparedStatement.executeQuery();

            if(resultSet.isBeforeFirst())
            {
                while(resultSet.next())
                {

                    var port = resultSet.getInt(PORT);

                    var ipAddress = resultSet.getString(IP_ADDRESS);

                    var hostname = resultSet.getString(HOSTNAME);

                    var password = resultSet.getString(PASSWORD);

                    responseToSend.put(PORT, port);

                    responseToSend.put(IP_ADDRESS, ipAddress);

                    responseToSend.put(HOSTNAME, hostname);

                    responseToSend.put(PASSWORD, password);

                }

                responseToSend.put(STATUS, SUCCESS);

            }
            else
            {
                responseToSend.put(STATUS, FAIL);

                responseToSend.put(MESSAGE, "Discovery Id does not Exist");

            }

        } catch(SQLException e)
        {
            responseToSend.put(STATUS, FAIL);

            responseToSend.put(MESSAGE, "Error occurred while performing request");

            LOGGER.error("{}", e.getMessage());
        } finally
        {
            if(preparedStatement != null)
            {
                try
                {
                    preparedStatement.close();

                } catch(SQLException e)
                {
                    LOGGER.error("{}", e.getMessage());
                }
            }
        }

        return responseToSend;
    }
}
