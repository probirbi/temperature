package com.blockchain.iot.controller;

import com.blockchain.iot.data.TestData;
import com.blockchain.iot.model.Block;
import com.blockchain.iot.model.Sensor;
import com.blockchain.iot.utils.FahrenheitToCelsius;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RestController
public class TemperatureController {

    List<Sensor> sensors = new ArrayList<Sensor>();

    /*@GetMapping("/temperatures")
    public List<Sensor> getTemperatures() {
        return sensors;
    }

    @PostMapping("/temperatures")
    public String saveTemperature(@RequestBody Sensor sensor, HttpServletRequest request) {
        sensors.add(sensor);
        request.getSession().setAttribute("sensors", sensors);
        if (request.getSession().getAttribute("sensors") != null) {
            System.out.println(((List<Sensor>) request.getSession().getAttribute("sensors")).size());
        }

        return "success";
    }*/

    @PostMapping("/evaluate")
    public String evaluate() {

        String result = "";
        for (Sensor sensor : sensors) {
            if (sensor.getTemperatureCelsius() >= -10 && sensor.getTemperatureCelsius() <= 40) {


                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    String url = "http://localhost:8081/blockchain";
                    HttpPost httpPost = new HttpPost(url);
                    httpPost.setHeader("Content-type", "application/json");
                    String json = "{" +
                            "\"hash\":" + "\"" + "" + "\"," +
                            "\"previousHash\":" + "\"" + "" + "\"," +
                            "\"description\":" + "\"" + "Temperature Block" + "\"," +
                            "\"data\":" + "{" +
                            "\"timestamp\":" + "\"" + sensor.getTimeStamp() + "\"," +
                            "\"temperatureCelsius\":" + "\"" + sensor.getTemperatureCelsius() + "\"," +
                            "\"temperatureFahrenheit\":" + "\"" + sensor.getTemperatureFahrenheit() + "\"," +
                            "\"humidity\":" + "\"" + sensor.getHumidity() + "\"" +
                            "} ," +
                            "\"timeStamp\":" + new Date().getTime() + "," +
                            "\"nonce\":" + 0 + "," +
                            "\"node\":" + 2 +
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println(sensor.getTemperatureCelsius() + " Temperature data is verified and found invalid");
            }
        }
        return "node evaluated";
    }

    @PostMapping("/evaluateparkingspace")
    public String evaluateparkingspace() {
        try {
            String url = "http://localhost:8083/evaluate";
            String result = "";
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type", "application/json");

            CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
            CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost);
            HttpEntity entity = closeableHttpResponse.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
                System.out.println(result);
            }
        } catch (Exception e) {
            e.printStackTrace();/**/
        }
        return "node evaluated";
    }

    @PostMapping("/evaluatesmarthome")
    public String evaluatesmarthome() {
        try {
            String url = "http://localhost:8081/evaluate";
            String result = "";
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type", "application/json");

            CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
            CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost);
            HttpEntity entity = closeableHttpResponse.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
                System.out.println(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "node evaluated";
    }

    @GetMapping("/temperature")
    public Block getTemperature(HttpServletRequest request) {

        String requestedBy = request.getParameter("requestedBy");
        Sensor sensor = new Sensor();
        Random random = new Random();
        double rangeMin = -10.00;
        double rangeMax = 40.00;

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        double temperature = rangeMin + (rangeMax - rangeMin) * random.nextDouble();
        double humidity = rangeMin + (rangeMax - rangeMin) * random.nextDouble();

        sensor.setTimeStamp(sdf.format(new Date()));
        sensor.setTemperatureCelsius(temperature);
        sensor.setTemperatureFahrenheit(FahrenheitToCelsius.toFahrenheit(temperature));
        sensor.setHumidity(humidity);

        //sensor.setBlockNumber(random.nextInt()+"");
        sensors.add(sensor);

        Block block = null;
        try {
            String url = "http://localhost:8081/blockchain?create=false";
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type", "application/json");
            String json = "{" +
                    "\"hash\":" + "\"" + "" + "\"," +
                    "\"previousHash\":" + "\"" + "" + "\"," +
                    "\"blockType\":" + "\"" + "SERVICE" + "\"," +
                    "\"blockNumber\":" + "0," +
                    //"\"description\":" + "\"" + "Temperature Block" + "\"," +
                    "\"data\":" + "{" +
                        "\"timestamp\":" + "\"" + sensor.getTimeStamp() + "\"," +
                         "\"temperatureCelsius\":" + decimalFormat.format(sensor.getTemperatureCelsius()) + "," +
                         "\"temperatureFahrenheit\":" + decimalFormat.format(sensor.getTemperatureFahrenheit()) + "," +
                         "\"humidity\":" + decimalFormat.format(sensor.getHumidity()) +
                    "} ," +
                    "\"requestTimeStamp\":" + new Date().getTime() + "," +
                    "\"responseTimeStamp\":" + new Date().getTime() + "," +
                    "\"serviceRequestedBy\":" + "\"" + requestedBy + "\"," +
                            "\"serviceResponseBy\":" + "\"" + "TemperatureNode" + "\"," +
                            "\"ratingDoneBy\":" + "\"" + "" + "\"," +
                            "\"evaluatedBy\":" + "\"" + "" + "\"," +
                            "\"serviceProvidedBy\":" + "\"" + "TemperatureNode" + "\"," +
                            "\"blockCreatedBy\":" + "\"" + requestedBy + "\"," +
                    "\"timeStamp\":" + new Date().getTime() + "," +
                   "\"description\":" + "\"" + "Temperature Block" + "\"," +
                    "\"nonce\":" + 0 + "," +
                    "\"node\":" + 1 + "," +
                    "\"trustScore\":" + null + "," +
                    "\"rating\":" + null + "," +
                            "\"comment\":" + "\"" + "" + "\"" +
                    "}";
            System.out.println(json);
            httpPost.setEntity(new StringEntity(json));

            CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
            CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost);
            HttpEntity responseEntity = closeableHttpResponse.getEntity();

            if (responseEntity != null) {
                String result = EntityUtils.toString(responseEntity);
                if (result != null && !result.equals("") && !result.equals("{}")) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<Block>() {
                    }.getType();
                    block = gson.fromJson(result, type);
                    broadcast(block);
                    sensor.setHash(block.getHash());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return block;
    }

    private void broadcast(Block block) {
        try {
            String url = "http://localhost:8082/broadcast";
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type", "application/json");

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(block);
            httpPost.setEntity(new StringEntity(json));

            CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
            CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost);
            HttpEntity responseEntity = closeableHttpResponse.getEntity();

            if (responseEntity != null) {
                String result = EntityUtils.toString(responseEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

       /* try {
            String url = "http://localhost:8083/broadcast";
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type", "application/json");

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(block);

            httpPost.setEntity(new StringEntity(json));
            CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
            CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost);
            HttpEntity responseEntity = closeableHttpResponse.getEntity();

            if (responseEntity != null) {
                String result = EntityUtils.toString(responseEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @GetMapping("/seeddata")
    public void insertData() {
        TestData.callPost();
    }
}
