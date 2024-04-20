package com.networkmonitoring.server;

import com.networkmonitoring.database.UserCredential;
import org.json.JSONObject;

import java.util.Scanner;

import static com.networkmonitoring.Constants.*;
import static com.networkmonitoring.server.Main.LOGGER;

public class CredentialProfile
{
    UserCredential userCredential = new UserCredential();


    public void createCredentialProfile(JSONObject userDetails)
    {

        LOGGER.info("User with user_id {} entered createCredentialProfile functionality",userDetails.getInt(USER_ID));

        String hostname;

        String password;

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the hostname");

        hostname = scanner.nextLine();

        System.out.println("Enter device's password");

        password = scanner.nextLine();

        System.out.println("Enter the protocol");

        var protocol = scanner.nextLine();

        userDetails.put(HOSTNAME, hostname);

        userDetails.put(DEVICE_PASSWORD, password);

        userDetails.put(PROTOCOL,protocol);


        JSONObject result = userCredential.createCredentialProfile(userDetails);

        if(result.getString(STATUS).equals(SUCCESS))
        {
            System.out.println("Credential profile create successfully");

            LOGGER.info("User with user_id {} created credential profile",userDetails.getInt(USER_ID));
        }
        else
        {
            System.out.println(result.getString(MESSAGE));


        }

    }

    public void displayCredentialProfile(JSONObject userDetails)
    {
        LOGGER.info("User with user_id {} entered viewCredentialProfile functionality",userDetails.getInt(USER_ID));

        var result = userCredential.viewCredentialProfile(userDetails);

        if(result.getString(STATUS).equals(SUCCESS))
        {

            LOGGER.info("User with user_id {} viewed credential profile",userDetails.getInt(USER_ID));

            System.out.println(SEPARATOR_LINE);

            System.out.println(TAB_LINE + TAB_LINE + TAB_LINE + TAB_LINE+ "Credential Details");

            System.out.println(SEPARATOR_LINE);

            System.out.println(TAB_LINE +"credProfileId hostname password protocol");


            for(String key : result.keySet())
            {
                if(!key.equals(STATUS))
                {
                    JSONObject profile = result.getJSONObject(key);

                    int credProfileId = profile.getInt(CREDPROFILE_ID);

                    String hostname = profile.getString(HOSTNAME);

                    String password = profile.getString(DEVICE_PASSWORD);

                    String protocol = profile.getString(PROTOCOL);

                    System.out.println(TAB_LINE + credProfileId + TAB_LINE + TAB_LINE +TAB_LINE + " "
                                        + hostname + TAB_LINE + TAB_LINE +  password + TAB_LINE  +protocol);
                }
            }
            System.out.println(SEPARATOR_LINE);

        }
        else
        {
            System.out.println(result.getString(MESSAGE));
        }

    }
}
