package com.networkmonitoring.server;

import static com.networkmonitoring.Constants.*;
import static com.networkmonitoring.server.Main.*;

import com.networkmonitoring.database.UserAuthentication;
import org.json.JSONObject;


import java.util.Scanner;

public class Login
{
    public void loginUser()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter username");

        String username = scanner.nextLine();

        System.out.println("Enter password");

        String password = scanner.nextLine();

        JSONObject userObject = new JSONObject();

        userObject.put(USERNAME, username);

        userObject.put(PASSWORD, password);

        UserAuthentication repository = new UserAuthentication();

        JSONObject loginResult = repository.login(userObject);

        if(loginResult.get(STATUS).equals(SUCCESS))
        {
            LOGGER.info("User with user_id {} logged in",loginResult.getInt(USER_ID));

            System.out.println(SEPARATOR_LINE);

            System.out.println(TAB_LINE + TAB_LINE +"Welcome " + loginResult.getString(NAME));

            LoggedInMenu loggedInMenu = new LoggedInMenu();

            loggedInMenu.displayMenu(loginResult);

        }
        else
        {
            System.out.println("Wrong Credentials");
        }
    }

}
