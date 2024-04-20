package com.networkmonitoring.database;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static com.networkmonitoring.Constants.*;
import static com.networkmonitoring.server.Main.LOGGER;

public class UserAuthentication
{
    public boolean register(JSONObject userJson)
    {
        try (Connection connection = DatabaseConnection.getConnection())
        {
            String query = "INSERT INTO users (name,username, password,email_id) VALUES (?,?,?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1,userJson.getString(NAME));

            preparedStatement.setString(2, userJson.getString(USERNAME));

            preparedStatement.setString(3, userJson.getString(PASSWORD));

            preparedStatement.setString(4,userJson.getString(EMAIL_ID));

           int result =  preparedStatement.executeUpdate();


           if(result>0)
           {
               return true;
           }
           else
           {
               return  false;
           }



        } catch (SQLException e)
        {

            LOGGER.error("{}",e.getMessage());

            return false;

        }

    }

    public JSONObject login(JSONObject userJSON)
    {
        JSONObject objectToReturn = new JSONObject();

        int userId;

        String name;

        try(Connection connection = DatabaseConnection.getConnection())
        {
            String query = "SELECT user_id,name FROM users WHERE username = ? AND password = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1,userJSON.getString(USERNAME));

            preparedStatement.setString(2,userJSON.getString(PASSWORD));

            ResultSet result = preparedStatement.executeQuery();

            if(result.next())
            {
                userId = result.getInt(USER_ID);

                name = result.getString(NAME);

                objectToReturn.put(USER_ID,userId);

                objectToReturn.put(NAME,name);

                objectToReturn.put(STATUS,SUCCESS);

            }
            else
            {
                objectToReturn.put(STATUS,FAIL);

            }


        } catch(SQLException e)
        {
            objectToReturn.put(STATUS,FAIL);

            LOGGER.error("{}",e.getMessage());


        }
        return objectToReturn;
    }

}
