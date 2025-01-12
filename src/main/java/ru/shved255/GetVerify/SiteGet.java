package ru.shved255.GetVerify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

public class SiteGet {

    public boolean Get(String url) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            return statusCode >= 200 && statusCode < 300;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
	
    public boolean isDataPresent(String searchValue, String url) {
        try {
            String jsonResponse = sendGetRequest(url); 
            JSONObject jsonObject = new JSONObject(jsonResponse);
            if(jsonObject.has("data")) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                for(int i = 0; i < dataArray.length(); i++) {
                    if(dataArray.getString(i).equals(searchValue)) {
                        return true; 
                    }
                }
            } else {
                System.out.println("Field 'data' not found in JSON response.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; 
    }
    
    public String sendGetRequest(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();     
        return response.toString();
    }
}
