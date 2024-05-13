package com.wave.task_service.Component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wave.task_service.dto.UserDto;

@Component
public class GetClaims 
{
    //@Value("${auth.host}")
    private String authHost = "localhost:8080";

    public UserDto getUser(String token) throws IOException
    {
        URL url = new URLBuilder(String.format("http://%s/jwt-controller/get-user", authHost)).withParam("token", token).build();
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
        return new ObjectMapper().readValue(response.toString(), UserDto.class);
    }
}
