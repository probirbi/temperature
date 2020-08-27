package com.blockchain.iot;

import com.blockchain.iot.data.TestData;
import com.blockchain.iot.model.Sensor;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class TemperatureController {

    List<Sensor> sensors = new ArrayList<Sensor>();

    @GetMapping("/temperatures")
    public List<Sensor> getTemperatures() {
        return sensors;
    }

    @PostMapping("/temperatures")
    public String saveTemperature(@RequestBody Sensor sensor, HttpServletRequest request) {
        sensors.add(sensor);
        request.getSession().setAttribute("sensors",sensors);
        if (request.getSession().getAttribute("sensors") != null) {
            System.out.println(((List<Sensor>) request.getSession().getAttribute("sensors")).size());
        }

        return "success";
    }

    @PostMapping("/evaluate")
    public String evaluate() {

        String result = "";
        for (Sensor sensor : sensors) {
            if (sensor.getTemperatureCelsius() >= -10 && sensor.getTemperatureCelsius() <= 40) {


                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    String url = "http://localhost:8084/blockchain";
                    HttpPost httpPost = new HttpPost(url);
                    httpPost.setHeader("Content-type", "application/json");
                    String json = "{" +
                            "\"hash\":" + "\"" + "" + "\"," +
                            "\"previousHash\":" + "\"" + "" + "\"," +
                            "\"description\":" + "\"" + "Temperature Block" + "\"," +
                            "\"data\":" + "{" +
                            "\"timestamp\":" + "\"" + sensor.getTimestamp() + "\"," +
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
            e.printStackTrace();
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

    @GetMapping("/seeddata")
    public void insertData() {
        TestData.callPost();
    }
}
