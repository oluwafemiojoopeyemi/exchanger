package com.webfactory.exchanger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.webfactory.exchanger.coindeskclient.ApiProxy;
import com.webfactory.exchanger.dto.CurrentRateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Date;

@SpringBootApplication
@EnableSwagger2
@EnableScheduling
public class ExchangerApplication   implements SchedulingConfigurer{

    @Autowired
    private ApiProxy apiProxy;

    private Gson gson = new Gson();

    @Value("${exchanger.cron}")
    private String cron;

    private static final String USD="USD";

    public static void main(String[] args) {
        SpringApplication.run(ExchangerApplication.class, args);
    }
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(new Runnable() {
            @Override
            public void run() {
                System.out.println("schedule sch>>>>>>>>>>>>>>>>>>>>>>>>>>");
                CurrentRateResponse currentRateResponse = new CurrentRateResponse();

                ResponseEntity<String> responseEntity = apiProxy.getCurrentRateByCurrency(USD);
                if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                    String value = responseEntity.getBody();
                    JsonObject coinDeskCurrentRateResponse = gson.fromJson(value, JsonObject.class);
                    JsonElement bpiElement = coinDeskCurrentRateResponse.get("bpi");
                    JsonObject bpiObject = bpiElement.getAsJsonObject();

                    JsonElement currencyElement = bpiObject.get(USD);
                    JsonObject currencyObject = currencyElement.getAsJsonObject();

                    JsonElement timeElement = coinDeskCurrentRateResponse.get("time");
                    JsonObject timeObject = timeElement.getAsJsonObject();


                    currentRateResponse.setDescription(currencyObject.get("description").getAsString());
                    currentRateResponse.setCurrency(currencyObject.get("code").getAsString());
                    currentRateResponse.setRate(currencyObject.get("rate").getAsString());
                    currentRateResponse.setUpdated(timeObject.get("updated").getAsString());

                    System.out.println("currentRateResponse >>>>>>>>>>>>>>>>>>>>>>>>>>  "+currentRateResponse);

                }


            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {

                CronTrigger trigger = new CronTrigger(cron);
                Date nextExec = trigger.nextExecutionTime(triggerContext);
                return nextExec;
            }
        });
    }



}
