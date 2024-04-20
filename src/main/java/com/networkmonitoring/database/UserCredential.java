package com.networkmonitoring.database;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.networkmonitoring.Constants.*;
import static com.networkmonitoring.server.Main.LOGGER;

public class UserCredential
{
    public JSONObject createCredentialProfile(JSONObject userData)
    {
        JSONObject responseToSend = new JSONObject();

        PreparedStatement preparedStatement = null;

        try(Connection connection = DatabaseConnection.getConnection())
        {

            String query = "INSERT INTO credential_profiles (user_id,hostname,password,protocol) VALUES (?,?,?,?)";

            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, userData.getInt(USER_ID));

            preparedStatement.setString(2, userData.getString(HOSTNAME));

            preparedStatement.setString(3, userData.getString(DEVICE_PASSWORD));

            preparedStatement.setString(4, userData.getString(PROTOCOL));

            int result = preparedStatement.executeUpdate();

            System.out.println(result);

            if(result > 0)
            {
                responseToSend.put(STATUS, SUCCESS);

            }
            else
            {
                responseToSend.put(STATUS, FAIL);

                LOGGER.error("Error Occurred");

                responseToSend.put(MESSAGE,"Error Occurred");
            }
        } catch(SQLException e)
        {

            responseToSend.put(STATUS, FAIL);

            responseToSend.put(MESSAGE,"Exception occurred");

           LOGGER.error("{}",e.getMessage());

        } finally
        {
            if(preparedStatement != null)
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

        return responseToSend;

    }

    public JSONObject viewCredentialProfile(JSONObject userData)
    {
        PreparedStatement preparedStatement = null;

        var resultJSON = new JSONObject();

        try(Connection connection = DatabaseConnection.getConnection())
        {

            String query = "SELECT * FROM credential_profiles WHERE user_id = ?";

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

                    innerJO.put(HOSTNAME, resultSet.getString(HOSTNAME));

                    innerJO.put(DEVICE_PASSWORD, resultSet.getString(DEVICE_PASSWORD));

                    innerJO.put(PROTOCOL, resultSet.getString(PROTOCOL));

                    resultJSON.put(String.valueOf(++counter), innerJO);
                }


                resultJSON.put(STATUS, SUCCESS);
            }
            else
            {
                resultJSON.put(STATUS, FAIL);

                resultJSON.put(MESSAGE, "You do not have any credential profile");

                LOGGER.info("User with user_id {} does not have credential profile",userData.getInt(USER_ID));


            }


        } catch(SQLException e)
        {
            resultJSON.put(STATUS, FAIL);

            resultJSON.put(MESSAGE, "Some error occurred");

           LOGGER.error("{}",e.getMessage());

        } finally
        {
            if(preparedStatement != null)
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

        return resultJSON;


    }
}
