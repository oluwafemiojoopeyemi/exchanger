package com.webfactory.exchanger.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.webfactory.exchanger.coindeskclient.ApiProxy;
import com.webfactory.exchanger.dto.CurrentRateResponse;
import com.webfactory.exchanger.dto.HistoryRateResponse;
import com.webfactory.exchanger.service.ExchangeService;
import com.webfactory.exchanger.util.DateUtil;
import com.webfactory.exchanger.util.RegexValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class ExchangeServiceImpl implements ExchangeService {

    @Autowired
    private ApiProxy apiProxy;

    private Gson gson = new Gson();

    @Override
    public ResponseEntity<CurrentRateResponse> getCurrentRateByCurrency(String currency) {
        CurrentRateResponse currentRateResponse = new CurrentRateResponse();
        try {

            if (currency.isBlank()) {
                currentRateResponse.setDescription("Invalid Currency value");
                return new ResponseEntity<CurrentRateResponse>(currentRateResponse, HttpStatus.BAD_REQUEST);

            }

            ResponseEntity<String> responseEntity = apiProxy.getCurrentRateByCurrency(currency);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                String value = responseEntity.getBody();
                JsonObject coinDeskCurrentRateResponse = gson.fromJson(value, JsonObject.class);
                JsonElement bpiElement = coinDeskCurrentRateResponse.get("bpi");
                JsonObject bpiObject = bpiElement.getAsJsonObject();

                JsonElement currencyElement = bpiObject.get(currency);
                JsonObject currencyObject = currencyElement.getAsJsonObject();

                JsonElement timeElement = coinDeskCurrentRateResponse.get("time");
                JsonObject timeObject = timeElement.getAsJsonObject();


                currentRateResponse.setDescription(currencyObject.get("description").getAsString());
                currentRateResponse.setCurrency(currencyObject.get("code").getAsString());
                currentRateResponse.setRate(currencyObject.get("rate").getAsString());
                currentRateResponse.setUpdated(timeObject.get("updated").getAsString());
                return new ResponseEntity<>(currentRateResponse, HttpStatus.OK);


            }


        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);


        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

    }


    @Override
    public ResponseEntity<HistoryRateResponse> getCurrentRateByDate(String startDate, String endDate) {
        HistoryRateResponse historyRateResponse = new HistoryRateResponse();
        try {

            if (!RegexValidator.validateDate(startDate)) {

                historyRateResponse.setDescription("Invalid Start Date");
                return new ResponseEntity<>(historyRateResponse, HttpStatus.BAD_REQUEST);

            }

            if (!RegexValidator.validateDate(endDate)) {

                historyRateResponse.setDescription("Invalid End Date");
                return new ResponseEntity<>(historyRateResponse, HttpStatus.BAD_REQUEST);


            }

            LocalDate start = DateUtil.getLocalDate(startDate);
            LocalDate end = DateUtil.getLocalDate(endDate);

            if (end.isBefore(start)) {

                historyRateResponse.setDescription("End Date should be after Start Date");
                return new ResponseEntity<>(historyRateResponse, HttpStatus.BAD_REQUEST);

            }


            ResponseEntity<String> responseEntity = apiProxy.getRatesBetweenDates(startDate, endDate);
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                System.out.println("result  >>>>>>>>>>>>>    " + responseEntity.getBody());
                JsonObject jsonObject = gson.fromJson(responseEntity.getBody(), JsonObject.class);
                JsonElement bpiElement = jsonObject.get("bpi");
                JsonObject bpiObject = bpiElement.getAsJsonObject();
                Map<String, String> map = new HashMap<>();
                historyRateResponse.setRate(map);
                for (Map.Entry<String, JsonElement> entry : bpiObject.entrySet()) {
                    System.out.println("Key = " + entry.getKey() + " Value = " + entry.getValue());
                    historyRateResponse.getRate().put(entry.getKey(), entry.getValue().getAsString());

                }
                historyRateResponse.setDescription(jsonObject.get("disclaimer").getAsString());
                JsonElement timeElement = jsonObject.get("time");
                JsonObject timeObject = timeElement.getAsJsonObject();
                historyRateResponse.setUpdated(timeObject.get("updatedISO").getAsString());
                return new ResponseEntity<>(historyRateResponse, HttpStatus.OK);

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);


        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
