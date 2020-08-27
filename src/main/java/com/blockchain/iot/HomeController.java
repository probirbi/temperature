package com.blockchain.iot;

import com.blockchain.iot.model.Sensor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.List;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String getData(HttpServletRequest request, Model model) {

       // System.out.println("get data");
        try {
            String url = "http://localhost:8082/temperatures";
            String result = "";
            HttpGet httpGet = new HttpGet(url);

            CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
            CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpGet);
            HttpEntity entity = closeableHttpResponse.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
                System.out.println(result);
                Gson gson = new Gson();
                Type type = new TypeToken<List<Sensor>>(){}.getType();
                List<Sensor> sensors = gson.fromJson(result, type);
                model.addAttribute("sensors",sensors);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "home";
    }
}
