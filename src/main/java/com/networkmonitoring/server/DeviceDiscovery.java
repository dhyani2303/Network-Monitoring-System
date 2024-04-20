package com.networkmonitoring.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.networkmonitoring.server.Main.LOGGER;

public class DeviceDiscovery
{
    public boolean discoverDevice(String ipAddress)
    {
        ProcessBuilder processBuilder = new ProcessBuilder("fping", ipAddress);

        try
        {
            Process process = processBuilder.start();

            int exitCode = process.waitFor();

            if(exitCode == 0)
            {
                return true;
            }

        } catch(IOException | InterruptedException e)
        {
           LOGGER.error("{}",e);

        }

        return false;

    }


}
