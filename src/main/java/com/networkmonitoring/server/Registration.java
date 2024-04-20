package com.networkmonitoring.server;

import static com.networkmonitoring.Constants.*;
import static com.networkmonitoring.server.Main.LOGGER;

import java.util.Scanner;

import com.networkmonitoring.database.UserAuthentication;
import org.json.JSONObject;


public class Registration
{
    public void registerUser()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter name:");

        String name = scanner.nextLine();

        System.out.println("Enter username");

        String username = scanner.nextLine();

        System.out.println("Enter password:");

        String password = scanner.nextLine();

        System.out.println("Enter the Email Id");

        String emailId = scanner.nextLine();

        JSONObject userObject = new JSONObject();

        userObject.put(NAME,name);

        userObject.put(USERNAME, username);

        userObject.put(PASSWORD, password);

        userObject.put(EMAIL_ID,emailId);

        UserAuthentication userRepository = new UserAuthentication();

       boolean registrationResult= userRepository.register(userObject);

       if(registrationResult)
       {
           System.out.println("User registered successfully.");

           LOGGER.info("a new user got registered");

           Login login = new Login();

           login.loginUser();
       }
       else
       {
           System.out.println("Unable to register the user");
       }
    }
}
