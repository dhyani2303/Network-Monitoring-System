package com.networkmonitoring.server;

import com.networkmonitoring.database.UserCredential;
import com.networkmonitoring.database.UserDiscovery;
import org.json.JSONObject;

import java.util.Scanner;

import static com.networkmonitoring.Constants.*;
import static com.networkmonitoring.server.Main.LOGGER;


public class DiscoveryProfile
{
    UserCredential userCredential = new UserCredential();

    static UserDiscovery userDiscovery = new UserDiscovery();

    DeviceDiscovery deviceDiscovery = new DeviceDiscovery();

    public void createDiscoveryProfile(JSONObject userDetails)
    {
        LOGGER.info("User with user_id {} entered createDiscoveryProfile functionality",userDetails.getInt(USER_ID));

        JSONObject credentialVerification = userCredential.viewCredentialProfile(userDetails);

        if(credentialVerification.getString(STATUS).equals(SUCCESS))
        {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter the type of device");

            String deviceType = scanner.nextLine();

            System.out.println("Enter credential Id");

            int credentialId = Integer.parseInt(scanner.nextLine());

            System.out.println("Enter port number");

            int port = Integer.parseInt(scanner.nextLine());

            System.out.println("Enter IP Address");

            String ipAddress = scanner.nextLine();

            boolean discoveryResult = deviceDiscovery.discoverDevice(ipAddress);

            if(discoveryResult)
            {

                System.out.println("Do you want to provision the device. press Y for yes and N for No");

                String toProvision = scanner.nextLine();

                if(toProvision.equals("Y"))
                {
                    userDetails.put(IS_PROVISIONED, TRUE);
                }
                else
                {
                    userDetails.put(IS_PROVISIONED, FALSE);
                }

                userDetails.put(PORT, port);

                userDetails.put(DEVICE_TYPE, deviceType);

                userDetails.put(CREDPROFILE_ID, credentialId);

                userDetails.put(IP_ADDRESS, ipAddress);

                var result = userDiscovery.createDiscoveryProfile(userDetails);

                if(result.getString(STATUS).equals(SUCCESS))
                {
                    System.out.println("Discovery profile has been created successfully");

                    LOGGER.info("User with user_id {} created discovery profile",userDetails.getInt(USER_ID));

                }
                else
                {
                    System.out.println("Unable to create discovery profile");
                }
            }
            else
            {
                System.out.println("Device cannot be discovered at a moment");

                LOGGER.info("User with user_id {} failed to discover the device with ipAddress {}",userDetails.getInt(USER_ID),ipAddress);
            }
        }
        else
        {
            System.out.println(credentialVerification.getString(MESSAGE));
        }
    }

    public static boolean viewDiscoveryProfile(JSONObject userDetails)
    {
        LOGGER.info("User with user_id {} entered viewDiscoveryProfile functionality",userDetails.getInt(USER_ID));

        var result = userDiscovery.viewDiscoveryProfile(userDetails);

        if(result.getString(STATUS).equals(SUCCESS))
        {

            System.out.println(SEPARATOR_LINE);

            System.out.println(TAB_LINE + TAB_LINE + TAB_LINE +TAB_LINE+ "Discovery Details" + TAB_LINE);

            System.out.println(SEPARATOR_LINE);

            System.out.println(TAB_LINE + "discoveryId  credProfileId port  ipAddress  deviceType isProvisioned");

            for(String key : result.keySet())
            {
                if(!key.equals(STATUS))
                {
                    JSONObject profile = result.getJSONObject(key);

                    int credProfileId = profile.getInt(CREDPROFILE_ID);

                    int discoveryId = profile.getInt(DISCOVERY_ID);

                    int port = profile.getInt(PORT);

                    String ipAddress = profile.getString(IP_ADDRESS);

                    String deviceType = profile.getString(DEVICE_TYPE);

                    String isProvisioned = profile.getString(IS_PROVISIONED);



                    System.out.println(TAB_LINE +discoveryId + TAB_LINE + TAB_LINE + TAB_LINE + TAB_LINE +
                                        credProfileId + TAB_LINE + TAB_LINE  + TAB_LINE +
                                        port + TAB_LINE  +
                                        ipAddress +TAB_LINE +
                                        deviceType +TAB_LINE + TAB_LINE  + isProvisioned);
                }
            }
            System.out.println(SEPARATOR_LINE);

            LOGGER.info("User with user_id {} viewed discovery profile",userDetails.getInt(USER_ID));

            return true;

        }
        else
        {
            System.out.println(result.getString(MESSAGE));

            return false;
        }

    }

}
