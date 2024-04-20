package com.networkmonitoring.server;


import org.json.JSONObject;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.networkmonitoring.Constants.*;
import static com.networkmonitoring.server.Main.LOGGER;

public class LoggedInMenu
{


    static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public  void displayMenu(JSONObject userDetails)
    {
        Scanner scanner = new Scanner(System.in);

        CredentialProfile credentialProfile = new CredentialProfile();

        DiscoveryProfile discoveryProfile = new DiscoveryProfile();


        boolean stop = false;

        while(!stop)
        {

            System.out.println(SEPARATOR_LINE);

            System.out.println("Please select any one option" + NEXT_LINE);

            System.out.println("1. Create Credential Profile");

            System.out.println("2. Create Discovery Profile");

            System.out.println("3. View Credential Profile");

            System.out.println("4. View Discovery Profile");

            System.out.println("5.Provision the device");

            System.out.println("6. Exit");

            System.out.println(SEPARATOR_LINE);

            System.out.println("Enter your choice");

            String choice = scanner.nextLine();


            switch(choice)
            {
                case "1":
                {
                    credentialProfile.createCredentialProfile(userDetails);

                    break;
                }

                case "2":
                {
                    discoveryProfile.createDiscoveryProfile(userDetails);

                    break;
                }

                case "3":
                {
                    credentialProfile.displayCredentialProfile(userDetails);
                    break;


                }
                case "4":
                {
                    discoveryProfile.viewDiscoveryProfile(userDetails);

                    break;
                }
                case "5":
                {
                    //print the table
                  boolean result=  DiscoveryProfile.viewDiscoveryProfile(userDetails);

                  if(result)
                  {

                      System.out.println("Enter discovery Id");

                      var discoveryId = scanner.nextInt();

                      userDetails.put(DISCOVERY_ID,discoveryId);



                      Middleware monitoringAgent = new Middleware(userDetails);

                      executorService.submit(monitoringAgent);
                  }

                    break;


                }

                case "6":
                {
                    System.out.println("Logging Out! Good Bye");

                    LOGGER.info("user with user_id {} logged out",userDetails.getInt(USER_ID));

                    return;
                }
                default:
                {
                    break;
                }
            }


        }
    }
}

