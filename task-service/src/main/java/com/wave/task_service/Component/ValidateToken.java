package com.wave.task_service.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ValidateToken
{
    //@Value("${auth.host}")
    private String authHost = "localhost:8080";


    public boolean validate(String token) throws IOException
    {
        URL url = new URLBuilder("http://" + authHost + "/jwt-controller/validate").withParam("token", token).build();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }

        in.close();
        return Boolean.parseBoolean(response.toString());
    }
}
