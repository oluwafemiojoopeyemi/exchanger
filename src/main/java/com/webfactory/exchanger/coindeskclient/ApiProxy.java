package com.webfactory.exchanger.coindeskclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiProxy {

    @Value("${coindesk.basepath}")
    private String basePath;

    public ResponseEntity<String> getCurrentRateByCurrency(String currency){

        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl
                = basePath+"currentprice/"+currency+".json";
        System.out.println("resourceUrl   >>>>>>>>>>>>>   "+resourceUrl);
        ResponseEntity<String> response
                = restTemplate.getForEntity(resourceUrl, String.class);

        return  response;
    }

    public ResponseEntity<String> getRatesBetweenDates(String startDate,String endDate){

        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl
                = basePath+"historical/close.json?start={startDate}&end={endDate}";



        ResponseEntity<String> response
                = restTemplate.getForEntity(resourceUrl, String.class,startDate,endDate);



        return  response;
    }
}
