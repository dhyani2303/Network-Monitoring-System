package com.networkmonitoring.server;

import org.json.JSONObject;

public class Middleware implements Runnable
{
    JSONObject userDetails;

    public Middleware(JSONObject userDetails)
    {
        this.userDetails = userDetails;
    }

    @Override
    public void run()
    {

        while(true)
        {
            Agent agentObj = new Agent();

            boolean continueMonitoring = agentObj.provisionDevice(userDetails);

            if(!continueMonitoring)
            {
                return;
            }
            try
            {
                Thread.sleep(60000);
            } catch(InterruptedException e)
            {
                System.out.println(e);
            }
        }
    }


}
