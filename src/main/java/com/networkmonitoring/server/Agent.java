package com.networkmonitoring.server;

import com.networkmonitoring.database.UserDiscovery;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


import static com.networkmonitoring.Constants.*;

import static com.networkmonitoring.server.Main.LOGGER;

public class Agent
{
    private JSONObject resultOfVerification = new JSONObject();

    public static final Logger ALERT_LOGGER = LoggerFactory.getLogger(Agent.class);


    public boolean provisionDevice(JSONObject userDetails)
    {

        LOGGER.info("User with user_id {} entered into provisioning device", userDetails.getInt(USER_ID));


        var discoveryProfile = new UserDiscovery();

        resultOfVerification = discoveryProfile.discoveryProfileExists(userDetails.getInt(DISCOVERY_ID));

        if(resultOfVerification.getString(STATUS).equals(SUCCESS))
        {
            DeviceDiscovery discovery = new DeviceDiscovery();

            var resultOfDiscovery = discovery.discoverDevice(resultOfVerification.getString(IP_ADDRESS));


            if(resultOfDiscovery)
            {
                LOGGER.info("User with user_id {} successfully discovered the device with ipAddress {}", userDetails.getInt(USER_ID), resultOfVerification.getString(IP_ADDRESS));

                 boolean result = startMonitoring(userDetails);

                 if(result)
                 {

                     return true;
                 }
                 else
                 {
                     return false;
                 }

            }
            else
            {
                LOGGER.info("User with user_id {} unable to  discover the device with ipAddress {}", userDetails.getInt(USER_ID), resultOfVerification.getString(IP_ADDRESS));

                System.out.println("Device is down for a while cannot monitor the metrics");

                return false;

            }

        }
        else
        {

            LOGGER.error("User with user_id {} unable to provision because of not having discovery profile", userDetails.getInt(USER_ID));

            System.out.println(resultOfVerification.getString(MESSAGE));

            return false;

        }


    }

    public boolean startMonitoring(JSONObject userDetails)
    {

        LOGGER.info("User with user_id {} entered monitoring for the device with ipAddress {}", userDetails.getInt(USER_ID), resultOfVerification.getString(IP_ADDRESS));

        JSONObject polledData = pollMetrics(userDetails);

        if(!polledData.isEmpty())
        {
            var putData = new com.networkmonitoring.database.SystemMetrics();

            boolean result = putData.putData(polledData);

            if(result)
            {
                LOGGER.info("Polled data of ipAddress {} by user_id {} has been placed in database", resultOfVerification.getString(IP_ADDRESS), userDetails.getInt(USER_ID));
            }
            else
            {

                LOGGER.info("Polled data of ipAddress {} by user_id {} was not placed in database", resultOfVerification.getString(IP_ADDRESS), userDetails.getInt(USER_ID));

            }
            return true;
        }
        else
        {
            System.out.println("Port is closed");

         LOGGER.error("User with user_id {} unable to provision the device with ipAddress {} because the port was closed", userDetails.getInt(USER_ID), resultOfVerification.getString(IP_ADDRESS));

            return false;
        }
    }


    private JSONObject pollMetrics(JSONObject userDetails)
    {
        JSONObject polledData = new JSONObject();

       LOGGER.info("User with user_id {} entered polling data for the device with ipAddress {}", userDetails.getInt(USER_ID), resultOfVerification.getString(IP_ADDRESS));

        try
        {
            var storeData = new ArrayList<String>();

            var command = " vmstat -s | grep 'context' | awk {'print $1'} ; mpstat | tail -1 | awk {'print $5,$7,$14'}; uptime | awk {'print $10'}; free -m | awk {'print $2,$3,$4'} | tail -2";

            ProcessBuilder processBuilder = new ProcessBuilder("sshpass", "-p", resultOfVerification.getString(PASSWORD), "ssh", "-o", "StrictHostKeyChecking=no", resultOfVerification.getString(HOSTNAME) + "@" + resultOfVerification.getString(IP_ADDRESS), command);

            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;

            while((line = reader.readLine()) != null && !line.contains("refused"))
            {

                storeData.add(line);


            }



            int exitCode = process.waitFor();



            if(!storeData.isEmpty() && exitCode == 0)
            {


                polledData.put(IP_ADDRESS, resultOfVerification.getString(IP_ADDRESS));

                parseOutput(storeData, polledData);

               LOGGER.info("User with user_id {} successfully polled data of the device with ipAddress {}", userDetails.getInt(USER_ID), resultOfVerification.getString(IP_ADDRESS));

            }
           LOGGER.info("User with user_id {} unable to poll data of the device with ipAddress {} and exited with exitCode {}", userDetails.getInt(USER_ID), resultOfVerification.getString(IP_ADDRESS), exitCode);

        } catch(IOException | InterruptedException e)
        {
            LOGGER.error("{}", e.getMessage());
        }
        return polledData;
    }

    private void parseOutput(ArrayList output, JSONObject polledData)
    {


        polledData.put("contextSwitches", output.get(0));

        var cpuValues = output.get(1).toString().split(" ");

        var memoryValues = output.get(3).toString().split(" ");

        var swapMemoryValues = output.get(4).toString().split(" ");

        if(cpuValues.length == 3 && memoryValues.length==3 && swapMemoryValues.length==3)
        {

            polledData.put("%user", cpuValues[0]);

            polledData.put("%sys", cpuValues[1]);

            polledData.put("%idle", cpuValues[2]);

            polledData.put("total_memory",memoryValues[0]);

            polledData.put("used_memory",memoryValues[1]);

            polledData.put("free_memory",memoryValues[2]);

            polledData.put("total_swapmemory",swapMemoryValues[0]);

            polledData.put("used_swapmemory",swapMemoryValues[1]);

            polledData.put("free_swapmemory",swapMemoryValues[2]);

            polledData.put("loadAverage", output.get(2));

            String stringValue = (String) output.get(2);

            var doubleValue =Double.parseDouble(stringValue);

            if(doubleValue>1.0)
            {
                ALERT_LOGGER.error("The device with ip address {} has exceeded the the threshold value of the load average of CPU.\n Load average value : {}",resultOfVerification.getString(IP_ADDRESS),output.get(2));
            }
        }


    }


}
