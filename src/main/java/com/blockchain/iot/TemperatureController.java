package com.blockchain.iot;

import com.blockchain.iot.data.TestData;
import com.blockchain.iot.model.Block;
import com.blockchain.iot.model.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class TemperatureController {

    @Autowired
    RestTemplate restTemplate;

    List<Sensor> sensors = new ArrayList<Sensor>();

    List<Block> blockChain = new ArrayList<Block>();

    int prefix = 2;

    String prefixString = new String(new char[prefix]).replace('\0', '0');

    @GetMapping("/get-respose-from-2")
    public String getData() {

        String responseFromNode3 = restTemplate.getForObject("http://localhost:8083/get-respose-from-3", String.class);


        return "response from node 2 \n" + responseFromNode3;
    }

    @GetMapping("/temperatures")
    public List<Sensor> getTemperatures() {

        for (int i = 0; i < blockChain.size(); i++) {
            String previousHash = i == 0 ? "0"
                    : blockChain.get(i - 1)
                    .getHash();
            boolean flag = blockChain.get(i)
                    .getHash()
                    .equals(blockChain.get(i)
                            .calculateBlockHash())
                    && previousHash.equals(blockChain.get(i)
                    .getPreviousHash())
                    && blockChain.get(i)
                    .getHash()
                    .substring(0, prefix)
                    .equals(prefixString);
            if (flag) {
                System.out.println("Blocks in the block chain is validated");
            }
        }
        return sensors;
    }

    @PostMapping("/temperatures")
    public String saveTemperature(@RequestBody Sensor sensor) {
        sensors.add(sensor);

        if (blockChain.size() == 0) {
            Block temperatureBlock = new Block("This is iotblockchain2 block", sensor, "0", new Date().getTime());
            temperatureBlock.mineBlock(prefix);
            blockChain.add(temperatureBlock);
        } else {
            Block temperatureBlock = new Block("This is iotblockchain2 block", sensor, blockChain.get(blockChain.size() - 1).getHash(), new Date().getTime());
            temperatureBlock.mineBlock(prefix);
            blockChain.add(temperatureBlock);
        }
        System.out.println("Block No: "+blockChain.size());
        return "success";
    }

    @GetMapping("/seeddata")
    public void insertData() {
        TestData.callPost();
    }
}
