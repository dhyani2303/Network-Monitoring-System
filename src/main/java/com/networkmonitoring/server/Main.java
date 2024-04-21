package com.networkmonitoring.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

import static com.networkmonitoring.Constants.*;

public class Main
{
    public static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args)
    {
        boolean isValid = false;

        Scanner scanner = new Scanner(System.in);

        System.out.println(SEPARATOR_LINE);

        System.out.println(TAB_LINE + TAB_LINE + "Welcome to Motadata");

        while(!isValid)
        {

            System.out.println(SEPARATOR_LINE);

            System.out.println(TAB_LINE + "1. Already having an account?" + NEXT_LINE + TAB_LINE + "2. Register Now!" + NEXT_LINE + TAB_LINE + "3.EXIT");

            System.out.println(SEPARATOR_LINE);

            try
            {

                System.out.println("Enter your choice");


                String choice = scanner.nextLine();

                if(choice != null)
                {
                    switch(choice)
                    {
                        case "1":
                        {
                            Login login = new Login();

                            login.loginUser();

                            isValid = true;
                            break;

                        }
                        case "2":
                        {
                            Registration registration = new Registration();

                            registration.registerUser();

                            isValid = true;

                            break;
                        }
                        case "3":
                        {
                            System.out.println("You are exiting Motadata");

                            return;
                        }
                        default:
                        {
                            System.out.println("Invalid Choice" + NEXT_LINE + "Please select valid option from menu");

                            break;
                        }
                    }
                }
            } catch(NullPointerException e)
            {
                System.out.println("Enter number only");
            }
        }


    }
}
