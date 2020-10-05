package com.blockchain.iot.controller;

import com.blockchain.iot.model.Sensor;
import com.blockchain.iot.model.Services;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    /**Message Sending Tool*/

    List<Services> services = new ArrayList<Services>();

    @GetMapping("/home")
    public String getData(HttpServletRequest request, Model model) {

        // System.out.println("get data");
        try {
            String url = "http://localhost:8081/temperatures";
            String result = "";
            HttpGet httpGet = new HttpGet(url);

            CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
            CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpGet);
            HttpEntity entity = closeableHttpResponse.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
                System.out.println(result);
                if (result != null && !result.equals("") && !result.equals("{}")) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Sensor>>() {
                    }.getType();
                    List<Sensor> sensors = gson.fromJson(result, type);
                    model.addAttribute("sensors", sensors);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        init();
        model.addAttribute("service", services);
        return "home";
    }

    private void init() {
        if (services.size() == 0) {
            Services service = new Services();

            /*service.setNode("Temperature Node");
            service.setServiceName("temperatures");
            service.setServiceProvider("Temperature Node");
            service.setRatingCriteria("evaluationLogicOne");
            services.add(service);
            trust = new Services();
             */

            service.setNode("Smart Home Node");
            service.setServiceName("smartHomes");
            service.setServiceProvider("Smart Home Node");
            service.setRatingCriteria("evaluationLogicTwo");
            services.add(service);

            service = new Services();
            service.setNode("Parking Space Node");
            service.setServiceName("parkingSpaces");
            service.setServiceProvider("Parking Space Node");
            service.setRatingCriteria("evaluationLogicThree");
            services.add(service);
        }
    }

   /* @GetMapping("/smartHomes")
    public String getSmartHomes(HttpServletRequest request, Model model) {
        init();
        // System.out.println("get data");
        try {
            String url = "http://localhost:8083/parkingSpace";
            String result = "";
            HttpGet httpGet = new HttpGet(url);

            CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
            CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpGet);
            HttpEntity responseEntity = closeableHttpResponse.getEntity();
            if (responseEntity != null) {
                result = EntityUtils.toString(responseEntity);
                System.out.println(result);

                Gson gson = new Gson();
                Type type = new TypeToken<ParkingSpace>() {
                }.getType();
                ParkingSpace parkingSpace = gson.fromJson(result, type);
                model.addAttribute("parkingSpace", parkingSpace);
                double rating = doRatingForParkingSpace(parkingSpace);
                updateBlockchain(parkingSpace.getHash(), rating);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/home";
    }*/

    /*@GetMapping("/parkingSpace")
    public String parkingSpace(HttpServletRequest request, Model model) {

        init();
        // System.out.println("get data");
        try {
            String url = "http://localhost:8083/parkingSpace";
            String result = "";
            HttpGet httpGet = new HttpGet(url);

            CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
            CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpGet);
            HttpEntity responseEntity = closeableHttpResponse.getEntity();
            if (responseEntity != null) {
                result = EntityUtils.toString(responseEntity);
                System.out.println(result);

                Gson gson = new Gson();
                Type type = new TypeToken<ParkingSpace>() {
                }.getType();
                ParkingSpace parkingSpace = gson.fromJson(result, type);
                model.addAttribute("parkingSpace", parkingSpace);
                double rating = doRatingForParkingSpace(parkingSpace);
                updateBlockchain(parkingSpace.getHash(), rating);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/home";
    }*/

    private double doRating(Sensor sensor) {
        if (sensor.getTemperatureCelsius() >= 10 && sensor.getTemperatureCelsius() <= 20) {
            return 1.0;
        } else if (sensor.getTemperatureCelsius() > 20 && sensor.getTemperatureCelsius() <= 30) {
            return 0.7;
        } else {
            return 0.5;
        }
    }

/*
    private double doRatingForParkingSpace(ParkingSpace parkingSpace) {
        if (parkingSpace.getParkedSpace() > 400 && parkingSpace.getParkedSpace() <= 500) {
            return 1.0;
        } else if (parkingSpace.getParkedSpace() > 300 && parkingSpace.getParkedSpace() <= 400) {
            return 0.7;
        } else {
            return 0.5;
        }
    }
*/

    private void updateBlockchain(String hash, double rating) {
        try {
            String url = "http://localhost:8081/blockchain/updaterating";
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type", "application/json");
            String json = "{" +
                    "\"hash\":" + "\"" + hash + "\"," +
                    "\"previousHash\":" + "\"" + "" + "\"," +
                    "\"description\":" + "\"" + "" + "\"," +
                    "\"data\":" + "{" +
                    "} ," +
                    "\"timeStamp\":" + "\"" + "" + "\"," +
                    "\"nonce\":" + 0 + "," +
                    "\"node\":" + 0 + "," +
                    "\"rating\":" + rating +
                    "}";
            System.out.println(json);
            httpPost.setEntity(new StringEntity(json));
            CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
            CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost);
            HttpEntity entity = closeableHttpResponse.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                System.out.println(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
