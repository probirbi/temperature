package com.blockchain.iot.data;

import com.blockchain.iot.util.FahrenheitToCelsius;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class TestData {

    public static void callPost() {
        for (int i = 0 ; i < 10 ; i++) {
           Random random = new Random();
            double rangeMin = -100.00;
            double rangeMax = 100.00;

            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            double temperature = rangeMin + (rangeMax - rangeMin) * random.nextDouble();
            double humidity = rangeMin + (rangeMax - rangeMin) * random.nextDouble();
            try {
                String url = "http://localhost:8082/temperatures";

                String result = "";
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader("Content-type", "application/json");
                String json = "{" +
                        "\"timestamp\":" + "\"" + sdf.format(new Date()) + "\"," +
                        "\"temperatureCelsius\":" + "\"" +  decimalFormat.format(temperature) +"\","+
                        "\"temperatureFahrenheit\":" + "\"" +  decimalFormat.format(FahrenheitToCelsius.toFahrenheit(temperature)) +"\","+
                        "\"humidity\":" + "\"" + decimalFormat.format(humidity) + "\"" +
                        "}";

                System.out.println(json);
                httpPost.setEntity(new StringEntity(json));

                CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
                CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost);
                HttpEntity entity = closeableHttpResponse.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity);
                    System.out.println(result);
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
